package com.c1ok.bedwars.exp

import com.c1ok.bedwars.Team
import com.c1ok.bedwars.simple.SimpleBedwarsPlayer
import com.c1ok.yggdrasil.MiniPlayer
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import com.c1ok.yggdrasil.util.Reason
import com.c1ok.yggdrasil.util.Result
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.scoreboard.Sidebar
import java.time.Instant
import java.util.*

class ExpPlayer(miniPlayer: MiniPlayer, game: ExpBedwarsGame) : SimpleBedwarsPlayer(miniPlayer, game) {

    override var spectator: Boolean = false

    private var team_: Team? = null

    override val sidebar: Sidebar = buildSidebar()

    /**
     * 构建 Sidebar 初始状态
     */
    private fun buildSidebar(): Sidebar {
        val sidebar = Sidebar(Component.text("起床战争", TextColor.color(255, 255, 85)))
        var index = 1

        // 空行
        sidebar.createLine(Sidebar.ScoreboardLine("air1", Component.text(""), index))
        index++
        sidebar.createLine(Sidebar.ScoreboardLine("bedBroken",
            MiniMessage.miniMessage().deserialize("<color:#AAAAAA>: <green><bedBrokens>", ), index))
        index++
        sidebar.createLine(Sidebar.ScoreboardLine("kills",
            MiniMessage.miniMessage().deserialize("<color:#AAAAAA>: <green><kills>"), index))

        // 添加所有队伍的状态
        game.getTeams().forEach { team ->
            val teamLine = Sidebar.ScoreboardLine(
                team.id.toString(),
                getTeamLineContent(team),
                index++
            )
            sidebar.createLine(teamLine)
        }

        // 添加玩家死亡数显示
        sidebar.createLine(
            Sidebar.ScoreboardLine("deaths", getDeathsLineContent(), index++)
        )

        // 添加时间行（占位，初始为“none”）
        sidebar.createLine(Sidebar.ScoreboardLine("time", Component.text("none"), index))

        return sidebar
    }

    /**
     * 获取队伍状态行内容
     *
     * @param team 队伍实例
     * @return 队伍的状态文本
     */
    private fun getTeamLineContent(team: Team): Component {
        val stateText = getTeamState(team)
        return team.displayName.append(stateText)
    }

    /**
     * 获取当前日期文本
     *
     * @return 表示当前日期的文本组件
     */
    private fun getCurrentDateText(): Component {
        return Component.text(Date.from(Instant.now()).toString())
    }

    /**
     * 获取死亡数显示行内容
     *
     * @return 死亡数的文本组件
     */
    private fun getDeathsLineContent(): Component {
        val deathsLabel = Component.text("死亡数: ")
            .color(TextColor.color(0xFFD700)) // 金色
            .decorate(TextDecoration.BOLD)

        val deathsCount = Component.text(kills)
            .color(TextColor.color(0x00FF00)) // 绿色
            .decorate(TextDecoration.UNDERLINED)

        return deathsLabel.append(deathsCount)
    }

    /**
     * 获取队伍状态：是否床已被摧毁
     *
     * @param team 队伍
     * @return 队伍的状态，绿色勾（未摧毁）或红色叉（已摧毁）
     */
    private fun getTeamState(team: Team): Component {
        return if (!team.getIsBedDestroy()) {
            MiniMessage.miniMessage().deserialize(
                " <green> ✔ <color:#AAAAAA>(<gray>${team.players.filter { !it.spectator }.size}<color:#AAAAAA>)"
            )
        } else {
            // 床已被摧毁
            MiniMessage.miniMessage().deserialize(
                " <red> ✘ <color:#AAAAAA>(<gray>${team.players.filter { !it.spectator }.size}<color:#AAAAAA>)")
        }
    }

    override fun getTeam(): Team? {
        return team_
    }

    override fun getResource(resourceID: String): Int {
        if (resourceID != "level") return 0
        val player = miniPlayer.getOrigin() ?: return 0
        return player.level
    }

    override fun addResource(resourceID: String, count: Int): Result<Boolean> {
        if (resourceID != "level") return Result(false, Reason.Failed("经验模式.."))
        val player = miniPlayer.getOrigin() ?: return Result(false, Reason.Failed("空"))
        player.level += count
        return Result(true, Reason.Success)
    }


}