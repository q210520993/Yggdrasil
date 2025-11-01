package com.c1ok.bedwars.exp.generator

import com.c1ok.bedwars.BedwarsPlayer
import com.c1ok.bedwars.SpawnResourceType
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import net.minestom.server.coordinate.Point
import net.minestom.server.entity.ItemEntity
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class Diamond(
    id: String,
    point: Point
): SpawnResourceType(id, "level", "钻石", Material.IRON_INGOT, 500, point) {

    override fun onCollect(player: BedwarsPlayer, itemEntity: ItemEntity): Boolean {
        val minePlayer = player.miniPlayer.getOrigin() ?: return false
        return minePlayer.inventory.addItemStack(ItemStack.of(Material.DIAMOND))
    }

}