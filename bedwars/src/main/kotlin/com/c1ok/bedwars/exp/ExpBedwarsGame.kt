package com.c1ok.bedwars.exp

import com.c1ok.bedwars.BaseBedwarsMachine
import com.c1ok.bedwars.BedwarsPlayer
import com.c1ok.bedwars.Team
import com.c1ok.bedwars.instance.SimpleInstance
import com.c1ok.bedwars.simple.SimpleBedwarsGame
import com.c1ok.bedwars.simple.specialitems.SelectTeam
import com.c1ok.bedwars.utils.setSpecial
import com.c1ok.yggdrasil.*
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import com.c1ok.yggdrasil.base.Bound
import com.c1ok.yggdrasil.util.Reason
import com.c1ok.yggdrasil.util.Result
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.entity.EntityDeathEvent
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerBlockPlaceEvent
import net.minestom.server.event.player.PlayerDeathEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.scoreboard.Sidebar
import net.minestom.server.timer.Task
import java.util.*
import java.util.concurrent.CompletableFuture

open class ExpBedwarsGame(
    waitingPos: Pos,
    override val minPlayers: Int,
    override val maxPlayers: Int,
    override val displayName: Component,
    override val instanceManager: InstanceManager,
): SimpleBedwarsGame(waitingPos) {

    override val uuid: UUID = UUID.randomUUID()

    override val bedwarsPlayerCreator: BedwarsPlayerCreator = BedwarsPlayerCreator { _, mp ->
        return@BedwarsPlayerCreator ExpPlayer(mp, this)
    }

    override fun addPlayer(player: MiniPlayer): Result<Boolean> {
        val minestomPlayer = player.getOrigin() ?: return Result.createFailed("玩家并不存在")
        val join = super.addPlayer(player)
        if (!join.success) return join
        if (getCurrentState() == GameState.LOBBY) {
            getLobbySidebar().addViewer(minestomPlayer)
            minestomPlayer.gameMode = GameMode.ADVENTURE
            minestomPlayer.inventory?.clear()
            minestomPlayer.inventory?.setItemStack(0, ItemStack.builder(Material.BEACON)
                .customName(Component.text("队伍选择器"))
                .setSpecial("selectTeam")
                .build())
            minestomPlayer.inventory?.setItemStack(8, ItemStack.builder(Material.SLIME_BALL)
                .customName(Component.text("离开游戏"))
                .setSpecial("exitTeam")
                .build())
        }
        player.game = this
        if (getCurrentState() == GameState.STARTING) {
            minestomPlayer.gameMode = GameMode.SURVIVAL
        }
        return Result(true, Reason.Success)
    }

    open val sidebar = Sidebar(Component.text("起床战争", TextColor.color(255, 153, 0))).apply {
        createLine(Sidebar.ScoreboardLine("kb", Component.text(""), 12))
        createLine(Sidebar.ScoreboardLine("header", Component.text("最少2人开始游戏", TextColor.color(170, 170, 170)), 11))
        createLine(Sidebar.ScoreboardLine("spacer1", Component.text(""), 10))

        val currentMap = MiniMessage.miniMessage().deserialize("<green>-当前地图:")
        createLine(Sidebar.ScoreboardLine("currentMap", currentMap, 9))
        val mapName = Component.text("火车", TextColor.color(170,170,170))
        createLine(Sidebar.ScoreboardLine("mapName", mapName, 8))

        createLine(Sidebar.ScoreboardLine("kb1", Component.text(""), 7))

        val playerLabel = MiniMessage.miniMessage().deserialize("<green>-玩家:")
        createLine(Sidebar.ScoreboardLine("playerLabel", playerLabel, 6))
        val playerCount = MiniMessage.miniMessage().deserialize("<color:#AAAAAA><current>/32", Placeholder.unparsed("current", getBedwarsPlayers().size.toString()))
        createLine(Sidebar.ScoreboardLine("playerCount", playerCount, 5))

        createLine(Sidebar.ScoreboardLine("kb2", Component.text(""), 4))

        val countdownLabel = MiniMessage.miniMessage().deserialize("<green>-本局开始倒计时:")
        createLine(Sidebar.ScoreboardLine("countdownLabel", countdownLabel, 3))
        val countdownTimeLine = MiniMessage.miniMessage().deserialize("<color:#AAAAAA>120秒")
        createLine(Sidebar.ScoreboardLine("countdownTime", countdownTimeLine, 2))

        createLine(Sidebar.ScoreboardLine("kb3", Component.text(""), 1))
    }

    override fun getLobbySidebar(): Sidebar {
        return sidebar
    }

    open fun registerBounds() {}

    override fun init(): CompletableFuture<Boolean> {
        registerBounds()
        addTeams()
        if (isFirst) {
            addSpecial(SelectTeam(this))
        }
        return super.init()
    }

    open fun addTeams() {}

    override val gameStateMachine: GameStateMachine by lazy {
        BaseBedwarsMachine(this)
    }

}