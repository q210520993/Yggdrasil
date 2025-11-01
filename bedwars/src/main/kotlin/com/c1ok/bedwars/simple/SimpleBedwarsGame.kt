package com.c1ok.bedwars.simple

import com.c1ok.bedwars.*
import com.c1ok.bedwars.instance.ReuseInstance
import com.c1ok.bedwars.simple.block.BedHandler
import com.c1ok.bedwars.simple.block.PlacedBlock
import com.c1ok.yggdrasil.GameState
import com.c1ok.yggdrasil.MiniPlayer
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOriginUnsafe
import com.c1ok.yggdrasil.base.BaseMiniGame
import com.c1ok.yggdrasil.util.Reason
import com.c1ok.yggdrasil.util.Result
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerBlockPlaceEvent
import net.minestom.server.event.player.PlayerDeathEvent
import net.minestom.server.event.player.PlayerRespawnEvent
import net.minestom.server.scoreboard.Sidebar
import net.minestom.server.timer.Task
import net.minestom.server.utils.validate.Check
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet

abstract class SimpleBedwarsGame(
    protected val waitingPos: Pos
): BedwarsGame, BaseMiniGame(), SpecialItemManager by SimpleSpecialManager() {

    companion object {
        private val logger = LoggerFactory.getLogger(SimpleBedwarsGame::class.java)
    }

    protected val teams: MutableList<Team> = CopyOnWriteArrayList()

    protected val bedwarsPlayers: MutableSet<BedwarsPlayer> = CopyOnWriteArraySet()

    protected abstract val bedwarsPlayerCreator: BedwarsPlayerCreator

    override val generator: Generator by lazy {
        SimpleGenerator(this)
    }

    override fun getTeams(): Collection<Team> {
        return teams
    }

    override fun addTeam(team: Team) {
        teams.add(team)
    }


    fun interface BedwarsPlayerCreator {
        fun create(bedWarsGame: SimpleBedwarsGame, miniPlayer: MiniPlayer): BedwarsPlayer
    }

    override fun getBedwarsPlayers(): Collection<BedwarsPlayer> {
        return bedwarsPlayers
    }

    override fun getBedwarsPlayer(uuid: UUID): BedwarsPlayer? {
        return bedwarsPlayers.firstOrNull { it.miniPlayer.uuid == uuid }
    }

    override fun getPlayers(): Collection<MiniPlayer> {
        return bedwarsPlayers.map { it.miniPlayer }
    }

    override fun getAllRegisteredTasks(): Collection<Task> {
        return tasks
    }

    override fun onPlayerDeath(event: PlayerDeathEvent) {
        if (getBedwarsPlayer(event.player.uuid) == null) {
            return
        }
        if (gameStateMachine.getCurrentState() == GameState.LOBBY) {
            event.player.respawn()
        }
        if (gameStateMachine.getCurrentState() != GameState.STARTING) {
            return
        }
        val gamePlayer = getBedwarsPlayer(event.player.uuid) ?: return
        val team = gamePlayer.getTeam() ?: return
        if(team.getIsBedDestroy()) {
            gamePlayer.spectator = true
        }
        (gamePlayer as? SimpleBedwarsPlayer)?.addDeathsCount(1)
    }

    override fun onBrokeBlock(event: PlayerBlockBreakEvent) {
        val gamePlayer = getBedwarsPlayer(event.player.uuid) ?: run {
            event.isCancelled = true
            return
        }
        val handler = event.block.handler() ?: run {
            event.isCancelled = true
            return
        }
        val postion = event.blockPosition
        if (handler is BedHandler) {
            if (!handler.canDestory(gamePlayer)) {
                event.isCancelled = true
            }
            return
        }
        if (handler !is PlacedBlock) {
            event.isCancelled = true
        }
        if (instanceManager is ReuseInstance) {
            val ins = instanceManager as ReuseInstance
            if (ins.containBlock(event.blockPosition)) {
                ins.removeBlock(postion)
            } else {
                event.isCancelled = true
            }
        }
    }

    override fun onPlaceBlock(event: PlayerBlockPlaceEvent) {
        val block = event.block.withHandler(PlacedBlock.instance)
        event.block = block
        if (instanceManager is ReuseInstance) {
            val ins = instanceManager as ReuseInstance
            ins.addBlock(block, event.blockPosition)
        }
    }

    override fun onStart(): CompletableFuture<Void> {
        val void = super.onStart()
        Bedwars.instance.debugRunnable {
            logger.info("游戏{}开始了", this.displayName)
        }
        getTeams().filter { it.getIsWipedOut() }.forEach {
            it.createBed()
        }
        getBedwarsPlayers().forEach {
            val player = it.miniPlayer.getOrigin() ?: return@forEach
            getLobbySidebar().removeViewer(player)
            player.inventory.clear()
            player.gameMode = GameMode.SURVIVAL
        }
        getBedwarsPlayers().forEach {
            it.miniPlayer.getOriginUnsafe().teleport(it.getTeam()?.respawnPoint)
        }
        generator.start()
        return void
    }

    override fun onPlayerRespawn(event: PlayerRespawnEvent) {
        Check.stateCondition(getBedwarsPlayer(event.player.uuid) == null, "玩家没有参与该游戏")
        val gamePlayer = getBedwarsPlayer(event.player.uuid)!!
        if (gamePlayer.spectator) {
            event.player.gameMode = GameMode.SPECTATOR
        }
        event.respawnPosition = gamePlayer.getTeam()?.respawnPoint ?: return
    }

    @Synchronized
    override fun addPlayer(player: MiniPlayer): Result<Boolean> {
        val minestomPlayer = player.getOrigin() ?: return Result(false, Reason.Failed("玩家并不存在"))

        if (gameStateMachine.getCurrentState() == GameState.INITIALIZING) {
            minestomPlayer.sendMessage("房间初始化中，无法进入")
            return Result(false, Reason.Failed("初始化中"))
        }
        if (gameStateMachine.getCurrentState() == GameState.RESTARTING) {
            minestomPlayer.sendMessage("房间重启中，无法进入")
            return Result(false, Reason.Failed("重启中"))
        }
        if (gameStateMachine.getCurrentState() == GameState.ENDED || gameStateMachine.getCurrentState() == GameState.CLOSED) {
            minestomPlayer.sendMessage("房间已经被关闭了，无法进入")
            return Result(false, Reason.Failed("关闭了"))
        }
        if (getPlayers().size > maxPlayers) {
            minestomPlayer.sendMessage("房间满人喽")
            return Result(false, Reason.Failed("人满了"))
        }
        if (gameStateMachine.getCurrentState() == GameState.LOBBY) {
            val playerB = bedwarsPlayerCreator.create(this, player)
            // TODO Call Pre Join Event
            bedwarsPlayers.add(playerB)
            minestomPlayer.setInstance(this.instanceManager.getCurrentInstance(), waitingPos)
            minestomPlayer.teleport(waitingPos)
            getLobbySidebar().addViewer(minestomPlayer)
            playerB.refreshPlayer()
            return Result(true, Reason.Success)
        }
        return Result(false, Reason.Failed("idk"))
    }

    @Synchronized
    override fun removePlayer(player: MiniPlayer): Result<Boolean> {
        val bedwarsPlayer = bedwarsPlayers.firstOrNull { it.miniPlayer == player } ?: return Result(false, Reason.Failed("玩家不存在在这场游戏中"))
        val minePlayer = player.getOrigin() ?: let {
            bedwarsPlayers.removeIf { it.miniPlayer == player }
            bedwarsPlayer.getTeam()?.removePlayer(bedwarsPlayer)
            return Result(false, Reason.Failed("玩家不存在"))
        }
        bedwarsPlayer.storedInventory.with(minePlayer)
        bedwarsPlayer.getTeam()?.removePlayer(bedwarsPlayer)
        if (gameStateMachine.getCurrentState() == GameState.LOBBY) {
            getLobbySidebar().removeViewer(minePlayer)
        }
        player.game = null
        // TODO Set Player Instance To Lobby
         minePlayer.setInstance(Bedwars.instance.lobby.instance, Bedwars.instance.lobby.respawnPos  )
        return Result(true, Reason.Success)
    }

    override fun onEnd(): CompletableFuture<Void> {
        getBedwarsPlayers().onEach {
            removePlayer(it.miniPlayer)
        }
        teams.forEach {
            it.onGameStop()
        }
        teams.clear()
        bedwarsPlayers.clear()
        Bedwars.instance.debugRunnable {
            logger.info("游戏{}结束了", this.displayName)
        }
        generator.close()
        return super.onEnd()
    }

    override fun getPlayerSidebar(miniPlayer: MiniPlayer): Sidebar? {
        return getBedwarsPlayer(miniPlayer)?.sidebar
    }

    override fun getGlobalSidebar(id: String): Sidebar? {
        if (id != "lobby") return null
        return getLobbySidebar()
    }

    override fun onRestart(): CompletableFuture<Void> {
        println("Restart！")
        val res = super.onRestart()
        println("Restart！Successfully")
        return res
    }


    protected abstract fun getLobbySidebar(): Sidebar

}