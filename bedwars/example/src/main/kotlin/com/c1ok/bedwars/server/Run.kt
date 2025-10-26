package com.c1ok.bedwars.server

import com.c1ok.bedwars.Bedwars
import com.c1ok.bedwars.commands.Test
import com.c1ok.bedwars.commands.TestStart
import com.c1ok.bedwars.games.train.Train
import com.c1ok.bedwars.simple.BedwarsManager
import com.c1ok.bedwars.simple.SimpleMiniPlayerManager
import com.redstone.beacon.internal.core.terminal.EasyTerminal
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.anvil.AnvilLoader

fun main() {

    EasyTerminal.start()
    val server = MinecraftServer.init()
    val instance = MinecraftServer.getInstanceManager().createInstanceContainer()
    instance.chunkLoader = AnvilLoader("lobby")
    val bedwars = Bedwars.Builder(MinecraftServer.process())
        .setLobby(instance)
        .setEventNode(EventNode.all("bedwarsGame").setPriority(-8))
        .setGameManager(BedwarsManager())
        .setPlayerManager(SimpleMiniPlayerManager())
        .setRespawnPos(Pos(-2.5, 26.0, -40.0))
        .build()
    listenerSetter()
    bedwars.apply()
    val game1 = Train()
    game1.init()
    bedwars.gameManager.addGameToManager(game1)
    MinecraftServer.getCommandManager().register(Test, TestStart)
    server.start("127.0.0.1", 25565)
    MinecraftServer.getSchedulerManager().buildShutdownTask {
        EasyTerminal.stop()
    }
}

private fun listenerSetter() {

    MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent::class.java) {
        it.spawningInstance = Bedwars.instance.lobby.instance
    }

    MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent::class.java) {
        if(it.instance == Bedwars.instance.lobby.instance) {
            it.player.gameMode = GameMode.CREATIVE
            it.player.teleport(Bedwars.instance.lobby.respawnPos)
        }
    }

}