package com.c1ok.bedwars.simple.listener

import com.c1ok.bedwars.Bedwars
import com.c1ok.bedwars.BedwarsGame
import com.c1ok.bedwars.ResourceEntity
import com.c1ok.bedwars.entity.Trader
import com.c1ok.bedwars.utils.getBedwarsPlayer
import com.c1ok.yggdrasil.GameState
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.item.PickupItemEvent
import net.minestom.server.event.player.*
import xyz.xenondevs.invui.window.Window

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

        getBedwarsEventChild().addListener(PlayerDisconnectEvent::class.java) {
            val player = Bedwars.instance.playerManager.getPlayerFromUUID(it.player.uuid) ?: return@addListener
            val game = (player.game as? BedwarsGame) ?: return@addListener
            game.getBedwarsPlayer(it.player.uuid) ?: return@addListener
            game.removePlayer(player)
        }

        getBedwarsEventChild().addListener(PlayerEntityInteractEvent::class.java) {
            val entity = it.target
            val gamePlayer = it.player.getBedwarsPlayer() ?: return@addListener
            val game = gamePlayer.game
            if (game.getCurrentState() != GameState.STARTING) return@addListener
            val team = gamePlayer.getTeam() ?: return@addListener
            if (entity is Trader) {
                Window.single()
                    .setGui(team.shop)
                    .setTitle(team.displayName)
                    .open(it.player)
            }
        }

        getBedwarsEventChild().addListener(PickupItemEvent::class.java) {
            val itemEntity = it.itemEntity
            if (itemEntity !is ResourceEntity)  {
                return@addListener
            }
            val gamePlayer = (it.livingEntity as? Player)?.getBedwarsPlayer() ?: return@addListener
            if(!itemEntity.resourceType.onCollect(gamePlayer, itemEntity)) {
                it.isCancelled = true
            }
        }

    }

    private fun getBedwarsEventChild(): EventNode<Event> {
        return Bedwars.instance.eventNode
    }

}