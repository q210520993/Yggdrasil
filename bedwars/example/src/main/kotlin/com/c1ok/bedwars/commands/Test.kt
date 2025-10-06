package com.c1ok.bedwars.commands

import com.c1ok.bedwars.inventory.GameInventory
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player

object Test: Command("TestInventory") {
    init {
        addSyntax({ sender, context ->
            val player = sender as Player
            player.openInventory(GameInventory.inventory)
        })
    }
}