package com.c1ok.bedwars.utils

import com.c1ok.bedwars.Bedwars
import com.c1ok.bedwars.BedwarsGame
import com.c1ok.bedwars.BedwarsPlayer
import net.minestom.server.entity.Player

fun Player.getBedwarsPlayer(): BedwarsPlayer? {
    val playerManager = Bedwars.instance.playerManager
    val player = playerManager.getMiniPlayer(this) ?: return null
    val game = (player.game as? BedwarsGame) ?: return null
    return game.getBedwarsPlayer(player)
}