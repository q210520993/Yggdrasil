package com.c1ok.bedwars.simple

import com.c1ok.bedwars.BedwarsGame
import com.c1ok.bedwars.BedwarsPlayer
import com.c1ok.bedwars.Team
import com.c1ok.yggdrasil.GameState
import com.c1ok.yggdrasil.MiniPlayer
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import com.c1ok.yggdrasil.base.BaseMiniGame
import com.c1ok.yggdrasil.util.Reason
import com.c1ok.yggdrasil.util.Result
import net.minestom.server.coordinate.Pos
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CopyOnWriteArraySet

abstract class SimpleBedwarsGame(
    protected val waitingPos: Pos
): BedwarsGame, BaseMiniGame() {

    val teams: MutableList<Team> = mutableListOf()
    protected val bedwarsPlayers: MutableSet<BedwarsPlayer> = CopyOnWriteArraySet()

    protected abstract val bedwarsPlayerCreator: BedwarsPlayerCreator

    @FunctionalInterface
    interface BedwarsPlayerCreator {
        fun create(bedWarsGame: SimpleBedwarsGame, miniPlayer: MiniPlayer): BedwarsPlayer
    }

    override fun init(): CompletableFuture<Boolean> {
        val origin = super.init()
        initTeams()
        return origin
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
            playerB.refreshPlayer()
            return Result(true, Reason.Success)
        }
        return Result(false, Reason.Failed("idk"))
    }

    @Synchronized
    override fun removePlayer(player: MiniPlayer): Result<Boolean> {
        val minePlayer = player.getOrigin() ?: let {
            bedwarsPlayers.removeIf { it.miniPlayer == player }
            return Result(false, Reason.Failed("玩家不存在"))
        }
        val bedwarsPlayer = bedwarsPlayers.firstOrNull { it.miniPlayer == player } ?: return Result(false, Reason.Failed("玩家不存在在这场游戏中"))
        bedwarsPlayer.storedInventory.with(minePlayer)
        // TODO Set Player Instance To Lobby
        // minePlayer.setInstance(lobby)
        return Result(true, Reason.Success)
    }

    override fun onEnd(): CompletableFuture<Void> {
        clearTeam()
        return super.onEnd()
    }

}