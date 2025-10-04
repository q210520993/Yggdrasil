package com.c1ok.bedwars.games.train

import com.c1ok.bedwars.exp.ExpBedwarsGame
import com.c1ok.bedwars.instance.SimpleInstance
import com.c1ok.bedwars.simple.bounds.BedCreator
import com.c1ok.bedwars.simple.bounds.PlayerTeleport
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.anvil.AnvilLoader

class Train: ExpBedwarsGame(
    Pos(-562.0, 60.0, 1533.0),
    2, 32,
    Component.text("火车"),
    SimpleInstance {
        return@SimpleInstance MinecraftServer.getInstanceManager().createInstanceContainer(AnvilLoader("train"))
    }
) {

    override fun registerBounds() {
        addBound(BedCreator(this), PlayerTeleport(this))
    }

}