package com.c1ok.yggdrasil

import net.minestom.server.entity.Player
import java.util.UUID

interface MiniPlayerManager {

    fun getPlayerFromUUID(uuid: UUID): MiniPlayer?

    fun getMiniPlayer(player: Player): MiniPlayer? {
        return getPlayerFromUUID(player.uuid)
    }

}