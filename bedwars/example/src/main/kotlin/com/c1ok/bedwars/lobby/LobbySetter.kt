package com.c1ok.bedwars.lobby

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.anvil.AnvilLoader

lateinit var lobbyInstance: Instance

fun lobbyInit() {
    instanceSetter()
    listenerSetter()
}

private fun listenerSetter() {

    MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent::class.java) {
        it.spawningInstance = lobbyInstance
    }

    MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent::class.java) {
        if(it.instance == lobbyInstance) {
            it.player.gameMode = GameMode.CREATIVE
            it.player.teleport(Pos(-2.5, 26.0, -40.0))
        }
    }

}

private fun instanceSetter() {
    val instance = MinecraftServer.getInstanceManager().createInstanceContainer()
    instance.chunkLoader = AnvilLoader("lobby")
    lobbyInstance = instance
}

