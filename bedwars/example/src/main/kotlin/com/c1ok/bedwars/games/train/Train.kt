package com.c1ok.bedwars.games.train

import com.c1ok.bedwars.exp.ExpBedwarsGame
import com.c1ok.bedwars.exp.bound.SidebarUpdater
import com.c1ok.bedwars.exp.generator.Diamond
import com.c1ok.bedwars.exp.generator.Gold
import com.c1ok.bedwars.exp.generator.Iron
import com.c1ok.bedwars.instance.SimpleInstance
import com.c1ok.bedwars.invui.guis.SimpleGui
import com.c1ok.bedwars.simple.SimpleTeam
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.BlockVec
import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.instance.block.Block
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.utils.Direction
import xyz.xenondevs.invui.gui.Gui

class Train: ExpBedwarsGame(
    Pos(-562.0, 60.0, 1533.0),
    2, 32,
    Component.text("火车"),
    SimpleInstance {
        return@SimpleInstance MinecraftServer.getInstanceManager().createInstanceContainer(AnvilLoader("train"))
    }
) {

    override fun registerBounds() {
        addBound(TraderCreator(this, listOf(
            Pos(-448.0, 43.0, 1531.0),
            Pos(-560.0, 43.0, 1647.0),
            Pos(-676.0, 43.0, 1531.0),
            Pos(-564.0, 43.0, 1419.0)
        )), SidebarUpdater(this))
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
            val gui = SimpleGui()

            override val shop: Gui = gui.tabGui

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
            val gui = SimpleGui()

            override val shop: Gui = gui.tabGui

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
            val gui = SimpleGui()

            override val shop: Gui = gui.tabGui

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
            val gui = SimpleGui()

            override val shop: Gui = gui.tabGui

            override fun getTeamItemStack(): ItemStack {
                return ItemStack.of(Material.YELLOW_WOOL)
            }
        }
        teams.add(team1)
        teams.add(team2)
        teams.add(team4)
        teams.add(team3)
        generator.addGenerator(Iron("YELLOW_IRON_1", BlockVec(-562.0, 43.0, 1662.0)))
        generator.addGenerator(Iron("YELLOW_IRON_2", BlockVec(-562.0, 43.0, 1655.0)))
        generator.addGenerator(Iron("YELLOW_IRON_3", BlockVec(-562.0, 43.0, 1648.0)))
        generator.addGenerator(Iron("YELLOW_IRON_4", BlockVec(-562.0, 44.0, 1638.0)))
        generator.addGenerator(Iron("YELLOW_IRON_5", BlockVec(-562.0, 39.0, 1586.0)))
        generator.addGenerator(Gold("YELLOW_GOLD_1", BlockVec(-562.0, 39.0, 1578.0)))
        generator.addGenerator(Iron("RED_IRON_1", BlockVec(-691.0, 43.0, 1533.0)))
        generator.addGenerator(Iron("RED_IRON_2", BlockVec(-684.0, 43.0, 1533.0)))
        generator.addGenerator(Iron("RED_IRON_3", BlockVec(-677.0, 43.0, 1533.0)))
        generator.addGenerator(Iron("RED_IRON_4", BlockVec(-667.0, 44.0, 1533.0)))
        generator.addGenerator(Iron("RED_IRON_5", BlockVec(-615.0, 39.0, 1533.0)))
        generator.addGenerator(Gold("RED_GOLD_1", BlockVec(-607.0, 39.0, 1533.0)))
        generator.addGenerator(Iron("GREEN_IRON_1", BlockVec(-562.0, 43.0, 1404.0)))
        generator.addGenerator(Iron("GREEN_IRON_2", BlockVec(-562.0, 43.0, 1411.0)))
        generator.addGenerator(Iron("GREEN_IRON_3", BlockVec(-562.0, 43.0, 1418.0)))
        generator.addGenerator(Iron("GREEN_IRON_4", BlockVec(-562.0, 44.0, 1428.0)))
        generator.addGenerator(Iron("GREEN_IRON_5", BlockVec(-562.0, 39.0, 1480.0)))
        generator.addGenerator(Gold("GREEN_GOLD_1", BlockVec(-562.0, 39.0, 1488.0)))
        generator.addGenerator(Iron("BLUE_IRON_1", BlockVec(-433.0, 43.0, 1533.0)))
        generator.addGenerator(Iron("BLUE_IRON_2", BlockVec(-440.0, 43.0, 1533.0)))
        generator.addGenerator(Iron("BLUE_IRON_3", BlockVec(-447.0, 43.0, 1533.0)))
        generator.addGenerator(Iron("BLUE_IRON_4", BlockVec(-457.0, 44.0, 1533.0)))
        generator.addGenerator(Iron("BLUE_IRON_5", BlockVec(-509.0, 39.0, 1533.0)))
        generator.addGenerator(Gold("BLUE_GOLD_1", BlockVec(-517.0, 39.0, 1533.0)))
        generator.addGenerator(Diamond("CORE_DIAMOND_1", BlockVec(-562.0, 48.0, 1539.0)))
        generator.addGenerator(Diamond("CORE_DIAMOND_2", BlockVec(-562.0, 48.0, 1527.0)))
        generator.addGenerator(Diamond("CORE_DIAMOND_3", BlockVec(-568.0, 48.0, 1533.0)))
        generator.addGenerator(Diamond("CORE_DIAMOND_4", BlockVec(-556.0, 48.0, 1533.0)))
    }

}