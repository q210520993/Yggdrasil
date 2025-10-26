package com.c1ok.yggdrasil.util

import com.c1ok.yggdrasil.MiniPlayer
import com.c1ok.yggdrasil.MiniPlayerManager
import net.minestom.server.entity.Player
import java.util.*

fun Player.getMiniPlayer(playerManager: MiniPlayerManager): MiniPlayer? {
    return playerManager.getMiniPlayer(this)
}


fun UUID.getMiniPlayer(playerManager: MiniPlayerManager): MiniPlayer? {
    return playerManager.getPlayerFromUUID(this)
}