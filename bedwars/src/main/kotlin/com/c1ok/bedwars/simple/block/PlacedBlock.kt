package com.c1ok.bedwars.simple.block

import net.kyori.adventure.key.Key
import net.minestom.server.instance.block.BlockHandler

/**
 * 此hanlder是标记玩家放置的方块
 */
open class PlacedBlock: BlockHandler {

    override fun getKey(): Key {
        return Key.key("bedwars:placedblock")
    }

    companion object {
        val instance = PlacedBlock()
    }

}