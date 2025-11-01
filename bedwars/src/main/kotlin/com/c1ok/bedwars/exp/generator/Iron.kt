package com.c1ok.bedwars.exp.generator

import com.c1ok.bedwars.BedwarsPlayer
import com.c1ok.bedwars.SpawnResourceType
import net.minestom.server.coordinate.Point
import net.minestom.server.entity.ItemEntity
import net.minestom.server.item.Material

class Iron(
    id: String,
    point: Point
): SpawnResourceType(id, "level", "铁", Material.IRON_INGOT, 20, point) {

    override fun onCollect(player: BedwarsPlayer, itemEntity: ItemEntity): Boolean {
        player.addResource("level", itemEntity.itemStack.amount() * 20)
        return true
    }

}