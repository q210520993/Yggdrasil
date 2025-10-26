package com.c1ok.yggdrasil.network

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket

/**
 * 自行启用，用来屏蔽玩家的TabList
 * 世界与世界之间的隔离
 * @param event PlayerSpawnEvent
 */
fun onPlayerSpawnNetwork(event: PlayerSpawnEvent) {
    // 复制玩家列表
    val allPlayers = MinecraftServer.getConnectionManager().onlinePlayers.toMutableList()
    val instancePlayers = event.instance.players.toMutableList()
    for (instancePlayer in instancePlayers) {
        allPlayers.remove(instancePlayer)
        instancePlayer.sendPacket(createShowPacket(event.player))
        event.player.sendPacket(createShowPacket(instancePlayer))
    }
    for (allPlayer in allPlayers) {
        allPlayer.sendPacket(createHidePacket(player = event.player))
        event.player.sendPacket(createHidePacket(allPlayer))
    }
}

// 创建一个屏蔽玩家的包
fun createHidePacket(player: Player): PlayerInfoUpdatePacket {
    val entry = PlayerInfoUpdatePacket.Entry(
        player.uuid,
        player.username, listOf(),
        false, player.latency,
        player.gameMode,
        player.displayName, null, 0, false)
    return PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_LISTED, entry)
}

fun createShowPacket(player: Player): PlayerInfoUpdatePacket {
    val entry = PlayerInfoUpdatePacket.Entry(
        player.uuid,
        player.username, listOf(),
        true, player.latency,
        player.gameMode,
        player.displayName, null, 0, false)
    return PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_LISTED, entry)
}