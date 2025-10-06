package com.c1ok.bedwars.inventory

import com.c1ok.bedwars.Bedwars
import com.c1ok.yggdrasil.GameState
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.function.BiPredicate

object GameInventory {
    val inventory = Inventory(InventoryType.CHEST_6_ROW, "起床战争游戏选择界面")
    private fun update() {
        inventory.clear()
        var index = 0
        Bedwars.instance.gameManager.getGames().filter {
            it.gameStateMachine.getCurrentState() == GameState.LOBBY
        }.forEach { te ->
            inventory.setItemStack(index, ItemStack.builder(Material.GREEN_WOOL)
                .customName(Component.text(te.uuid.toString()))
                .build()
            )
            if (index > 53) return@forEach
            index ++
        }
        Bedwars.instance.gameManager.getGames().filter {
            it.gameStateMachine.getCurrentState() == GameState.STARTING
        }.forEach { _ ->
            inventory.setItemStack(index, ItemStack.builder(Material.RED_WOOL).build())
            index ++
        }
    }
    init {
        val node = EventNode.type("click", EventFilter.INVENTORY, BiPredicate { _, inv ->
            return@BiPredicate inventory == inv
        }).addListener(InventoryPreClickEvent::class.java) {
            it.isCancelled = true
            if (it.clickedItem.material() == Material.AIR) return@addListener
            Bedwars.instance.gameManager.getGames().first().addPlayer(Bedwars.instance.playerManager.getMiniPlayer(it.player)!!)
            it.player.closeInventory()
        }
        MinecraftServer.getGlobalEventHandler().addChild(node)
        MinecraftServer.getSchedulerManager().scheduleNextTick {
            update()
        }
    }
}