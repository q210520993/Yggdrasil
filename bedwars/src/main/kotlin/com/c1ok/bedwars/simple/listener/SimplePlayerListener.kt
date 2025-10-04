package com.c1ok.bedwars.simple.listener

import com.c1ok.bedwars.Bedwars
import com.c1ok.bedwars.BedwarsGame
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerBlockPlaceEvent
import net.minestom.server.event.player.PlayerDeathEvent
import net.minestom.server.event.player.PlayerRespawnEvent

object SimplePlayerListener {

    private fun getGame(player1: Player): BedwarsGame? {
        val player = Bedwars.instance.playerManager.getMiniPlayer(player = player1) ?: return null
        val game = (player.game as? BedwarsGame) ?: return null
        return game
    }

    fun register() {

        getBedwarsEventChild().addListener(PlayerBlockPlaceEvent::class.java) {
            val game = getGame(it.player) ?: return@addListener
            game.onPlaceBlock(it)
        }
        getBedwarsEventChild().addListener(PlayerBlockBreakEvent::class.java) {
            val game = getGame(it.player) ?: return@addListener
            game.onBrokeBlock(it)
        }
        getBedwarsEventChild().addListener(PlayerDeathEvent::class.java) {
            val game = getGame(it.player) ?: return@addListener
            game.onPlayerDeath(it)
        }
        getBedwarsEventChild().addListener(PlayerRespawnEvent::class.java) {
            val game = getGame(it.player) ?: return@addListener
            game.onPlayerRespawn(it)
        }

    }

    private fun getBedwarsEventChild(): EventNode<Event> {
        return Bedwars.instance.eventNode
    }

}