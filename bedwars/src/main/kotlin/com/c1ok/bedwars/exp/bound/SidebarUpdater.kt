package com.c1ok.bedwars.exp.bound

import com.c1ok.bedwars.BedwarsPlayer
import com.c1ok.bedwars.Team
import com.c1ok.bedwars.exp.ExpBedwarsGame
import com.c1ok.yggdrasil.GameState
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import com.c1ok.yggdrasil.base.BaseGameStateMachine
import com.c1ok.yggdrasil.base.Bound
import com.c1ok.yggdrasil.util.SchedulerBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.minestom.server.timer.TaskSchedule
import java.time.Instant
import java.util.*

open class SidebarUpdater(val game: ExpBedwarsGame): Bound {

    override val id: String = "SidebarUpdater"
    override val priority: Int = 5

    override fun onInit(isFirstInit: Boolean) {
        val task = SchedulerBuilder(game.instanceManager.getCurrentInstance().scheduler(), Runnable {
            if (game.gameStateMachine.getCurrentState() == GameState.LOBBY) {
                updateGameLobbySidebar()
            }
            if (game.gameStateMachine.getCurrentState() == GameState.STARTING) {
                game.getBedwarsPlayers().forEach {
                    val player = it.miniPlayer.getOrigin() ?: return@forEach
                    val sidebar = game.getPlayerSidebar(it.miniPlayer) ?: return@forEach
                    val gameStateMachine = game.gameStateMachine as? BaseGameStateMachine ?: return@forEach
                    sidebar.updateLineContent("date", getCurrentDateText())
                    sidebar.updateLineContent("deaths", getDeathsLineContent(it))
                    sidebar.updateLineContent("time", Component.text("剩余时间: ${gameStateMachine.GAME_TIME.getTime()} 秒"))

                    game.getTeams().forEach { team ->
                        sidebar.updateLineContent(
                            team.id.toString(),
                            getTeamLineContent(team)
                        )
                    }
                    if(!sidebar.players.contains(player)) {
                        sidebar.addViewer(player)
                    }
                }
            }
        }).repeat(TaskSchedule.tick(10)).schedule()
        game.registerTask(task)
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
    private fun getDeathsLineContent(bedwarsPlayer: BedwarsPlayer): Component {
        val deathsLabel = Component.text("死亡数: ")
            .color(TextColor.color(0xFFD700)) // 金色
            .decorate(TextDecoration.BOLD)

        val deathsCount = Component.text(bedwarsPlayer.getDeathsCount())
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

    open fun updateGameLobbySidebar() {
        val gameStateMachine = game.gameStateMachine as? BaseGameStateMachine ?: return
        val playerCount = MiniMessage.miniMessage().deserialize("<color:#AAAAAA><current>/32", Placeholder.unparsed("current", game.getPlayers().size.toString()))
        game.getGlobalSidebar("lobby")!!.updateLineContent("playerCount", playerCount)

        val countdownTimeLine = MiniMessage.miniMessage().deserialize("<color:#AAAAAA><time>秒", Placeholder.unparsed("time",
            gameStateMachine.WAITING_TIME.getTime().toString()))
        game.getGlobalSidebar("lobby")!!.updateLineContent("countdownTime", countdownTimeLine)
    }

}