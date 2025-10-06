package com.c1ok.bedwars

import com.c1ok.yggdrasil.MiniGame
import com.c1ok.yggdrasil.MiniPlayer
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerBlockPlaceEvent
import net.minestom.server.event.player.PlayerDeathEvent
import net.minestom.server.event.player.PlayerRespawnEvent
import java.util.*

/**
 * 代表着一个bedwarsGame
 * 有一些事件处理器，这是一个BedwarsGame必要的
 */
interface BedwarsGame: MiniGame, SpecialItemManager {

    val generator: Generator

    fun onPlaceBlock(event: PlayerBlockPlaceEvent)
    fun onBrokeBlock(event: PlayerBlockBreakEvent)
    fun onPlayerDeath(event: PlayerDeathEvent)
    fun onPlayerRespawn(event: PlayerRespawnEvent)

    fun addTeam(team: Team)

    fun getTeams(): Collection<Team>

    fun getBedwarsPlayers(): Collection<BedwarsPlayer>

    fun getBedwarsPlayer(uuid: UUID): BedwarsPlayer?

    fun getBedwarsPlayer(miniPlayer: MiniPlayer): BedwarsPlayer? {
        return getBedwarsPlayer(miniPlayer.uuid)
    }

}