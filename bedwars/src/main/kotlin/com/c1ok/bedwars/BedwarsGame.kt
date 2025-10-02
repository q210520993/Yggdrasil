package com.c1ok.bedwars

import com.c1ok.yggdrasil.MiniGame
import com.c1ok.yggdrasil.MiniPlayer
import net.minestom.server.event.entity.EntityDeathEvent
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerBlockPlaceEvent
import net.minestom.server.event.player.PlayerDeathEvent
import java.util.*

/**
 * 代表着一个bedwarsGame
 * 有一些事件处理器，这是一个BedwarsGame必要的
 */
interface BedwarsGame: MiniGame {
    /**
     * 在初始化时注册队伍
     * 它只能发生在init阶段
     */
    fun initTeams()

    fun onPlaceBlock(event: PlayerBlockPlaceEvent)
    fun onBrokeBlock(event: PlayerBlockBreakEvent)
    fun onPlayerDeath(event: PlayerDeathEvent)
    fun onPlayerKill(event: EntityDeathEvent)

    /**
     * 在结束时清除队伍
     */
    fun clearTeam()

    fun getBedwarsPlayer(uuid: UUID): BedwarsPlayer?

    fun getBedwarsPlayer(miniPlayer: MiniPlayer): BedwarsPlayer? {
        return getBedwarsPlayer(miniPlayer.uuid)
    }

}