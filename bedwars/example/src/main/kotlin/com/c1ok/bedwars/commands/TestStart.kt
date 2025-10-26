package com.c1ok.bedwars.commands

import com.c1ok.bedwars.Bedwars
import com.c1ok.yggdrasil.base.BaseGameStateMachine
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player

object TestStart: Command("TestStart") {
    init {
        addSyntax({ sender, context ->
            val player = sender as Player
            val miniPlayer = Bedwars.instance.playerManager.getPlayerFromUUID(player.uuid)
            val game = miniPlayer?.game ?: return@addSyntax
            (game.gameStateMachine as BaseGameStateMachine).forceStarted.set(true)
        })
    }
}