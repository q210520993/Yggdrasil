package com.c1ok.bedwars.games.train

import com.c1ok.bedwars.exp.ExpBedwarsGame
import com.c1ok.bedwars.exp.bound.SidebarUpdater
import com.c1ok.bedwars.instance.SimpleInstance
import com.c1ok.bedwars.simple.SimpleTeam
import com.c1ok.bedwars.simple.bounds.BedCreator
import com.c1ok.bedwars.simple.bounds.PlayerTeleport
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.instance.block.Block
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.utils.Direction

class Train: ExpBedwarsGame(
    Pos(-562.0, 60.0, 1533.0),
    2, 32,
    Component.text("火车"),
    SimpleInstance {
        return@SimpleInstance MinecraftServer.getInstanceManager().createInstanceContainer(AnvilLoader("train"))
    }
) {

    override fun registerBounds() {
        addBound(BedCreator(this), PlayerTeleport(this), SidebarUpdater(this))
    }

    override fun addTeams() {
        val team1 = object : SimpleTeam(
            "红队",
            MiniMessage.miniMessage().deserialize("<red>红色方"),
            Pos(-699.0, 43.0, 1533.0),
            Block.RED_BED, Direction.EAST,
            this,
            Pos(-680.4, 43.0, 1533.0),
            priority = 1,
            minPlayers = 1,
            maxPlayers = 8,
        ) {
            override fun getTeamItemStack(): ItemStack {
                return ItemStack.builder(Material.RED_WOOL).build()
            }
        }
        val team2 = object: SimpleTeam("蓝队",
            MiniMessage.miniMessage().deserialize("<blue>蓝色方"),
            Pos(-425.0,43.0,1533.0),
            Block.BLUE_BED, Direction.WEST,
            this,
            Pos(-444.0,43.0,1533.0),
            priority = 2,
            minPlayers = 1,
            maxPlayers = 8
        ) {
            override fun getTeamItemStack(): ItemStack {
                return ItemStack.builder(Material.BLUE_WOOL).build()
            }
        }
        val team3 = object: SimpleTeam("绿队",
            MiniMessage.miniMessage().deserialize("<green>绿色方"),
            Pos(-562.0,43.0,1396.0),
            Block.GREEN_BED, Direction.SOUTH,
            this,
            Pos(-562.0,43.0,1415.0),
            priority = 3,
            minPlayers = 1,
            maxPlayers = 8
        ) {
            override fun getTeamItemStack(): ItemStack {
                return ItemStack.builder(Material.GREEN_WOOL).build()
            }
        }
        val team4 = object: SimpleTeam("黄队",
            MiniMessage.miniMessage().deserialize("<yellow>黄色方"),
            Pos(-562.0,43.0,1670.0),
            Block.YELLOW_BED, Direction.NORTH,
            this,
            Pos(-562.0,43.0,1651.0),
            priority = 3,
            minPlayers = 1,
            maxPlayers = 8
        ) {
            override fun getTeamItemStack(): ItemStack {
                return ItemStack.of(Material.YELLOW_WOOL)
            }
        }
        teams.add(team1)
        teams.add(team2)
        teams.add(team4)
        teams.add(team3)
    }

}