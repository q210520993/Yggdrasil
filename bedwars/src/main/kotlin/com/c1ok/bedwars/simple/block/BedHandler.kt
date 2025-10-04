package com.c1ok.bedwars.simple.block

import com.c1ok.bedwars.BedwarsPlayer
import com.c1ok.bedwars.Team
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.utils.Direction
import java.util.*

class BedHandler(val team: Team): BlockHandler {

    override fun onDestroy(destroy: BlockHandler.Destroy) {
        team.destroyBed()
        val instance = destroy.instance
        val block = destroy.block
        val pos = destroy.blockPosition
        val isFoot = "foot" == block.getProperty("part")
        val fac_ = block.getProperty("facing") ?: return
        var facing = Direction.valueOf(fac_.uppercase(Locale.getDefault()))

        if (isFoot) {
            facing = facing.opposite()
        }
        val otherPartPosition = pos.add(
            facing.normalX().toDouble(),
            facing.normalY().toDouble(),
            facing.normalZ().toDouble()
        )

        instance.setBlock(pos, Block.AIR)
        instance.setBlock(otherPartPosition, Block.AIR)
    }

    fun canDestory(player: BedwarsPlayer): Boolean {
        if (team.containsPlayer(player)) {
            player.miniPlayer.getOrigin()?.sendMessage(Component.text("你不能破坏自家的床！"))
            return false
        }
        return true
    }

    override fun getKey(): Key {
        return Key.key("bedwars:bedestory")
    }

}