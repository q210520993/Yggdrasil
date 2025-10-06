package com.c1ok.bedwars.exp

import com.c1ok.bedwars.BaseBedwarsMachine
import com.c1ok.bedwars.BedwarsPlayer
import com.c1ok.bedwars.Team
import com.c1ok.bedwars.instance.SimpleInstance
import com.c1ok.bedwars.simple.SimpleBedwarsGame
import com.c1ok.yggdrasil.GameStateMachine
import com.c1ok.yggdrasil.InstanceManager
import com.c1ok.yggdrasil.MiniPlayer
import com.c1ok.yggdrasil.base.Bound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.entity.EntityDeathEvent
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerBlockPlaceEvent
import net.minestom.server.event.player.PlayerDeathEvent
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
        return super.init()
    }

    open fun addTeams() {}

    override val gameStateMachine: GameStateMachine by lazy {
        BaseBedwarsMachine(this)
    }

}