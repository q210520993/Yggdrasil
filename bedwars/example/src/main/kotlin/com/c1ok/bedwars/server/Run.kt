package com.c1ok.bedwars.server

import com.c1ok.bedwars.Bedwars
import com.c1ok.bedwars.commands.Test
import com.c1ok.bedwars.games.train.Train
import com.c1ok.bedwars.lobby.lobbyInit
import com.c1ok.bedwars.lobby.lobbyInstance
import com.c1ok.bedwars.simple.BedwarsManager
import com.c1ok.bedwars.simple.SimpleMiniPlayerManager
import com.redstone.beacon.internal.core.terminal.EasyTerminal
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventNode

fun main() {

    EasyTerminal.start()
    val server = MinecraftServer.init()
    lobbyInit()
    val bedwars = Bedwars.Builder(MinecraftServer.process())
        .setLobby(lobbyInstance)
        .setEventNode(EventNode.all("bedwarsGame").setPriority(-8))
        .setGameManager(BedwarsManager())
        .setPlayerManager(SimpleMiniPlayerManager())
        .build()
    bedwars.apply()
    val game1 = Train()
    game1.init()
    bedwars.gameManager.addGameToManager(game1)
    MinecraftServer.getCommandManager().register(Test)
    server.start("127.0.0.1", 25565)
    MinecraftServer.getSchedulerManager().buildShutdownTask {
        EasyTerminal.stop()
    }
}
