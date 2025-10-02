package com.c1ok.bedwars.simple

import com.c1ok.yggdrasil.MiniPlayer
import com.c1ok.yggdrasil.MiniPlayerManager
import net.minestom.server.MinecraftServer
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class SimpleMiniPlayerManager: MiniPlayerManager {

    private val players: MutableMap<UUID, MiniPlayer> = ConcurrentHashMap()

    @Synchronized
    override fun getPlayerFromUUID(uuid: UUID): MiniPlayer? {
        if (players.containsKey(uuid)) return players[uuid]
        if (MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid) == null)
            return null
        if (players[uuid] == null) {
            val player = SimpleMiniPlayer(uuid)
            players[uuid] = player
            return player
        }
        return null
    }

}