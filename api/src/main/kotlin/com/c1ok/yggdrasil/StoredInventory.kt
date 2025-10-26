package com.c1ok.yggdrasil

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.instance.Instance
import net.minestom.server.item.ItemStack
import java.util.UUID

/**
 *
 * 这个类用于克隆玩家的一些数据
 *
 * @property armor Array<ItemStack>
 * @property displayName Component?
 * @property foodLevel Int
 * @property inventory Array<ItemStack>
 * @property gamemode GameMode
 * @property xp Float
 * @property level Int
 * @constructor
 */
open class StoredInventory(
    var armor: Array<ItemStack>,
    var displayName: Component?,
    var foodLevel: Int = 0,
    var inventory: Array<ItemStack>,
    var gamemode: GameMode,
    var xp: Float = 0f,
    var level: Int = 0
) {
    /**
     * 应用数据到玩家身上
     * @param player Player
     * @param clearInventory Boolean 是否清理玩家的背包
     */
    open fun with(player: Player, clearInventory: Boolean = true) {
        player.inventory.clear()
        inventory.withIndex().forEach {
            player.inventory.setItemStack(it.index, it.value)
        }
        player.exp = xp
        player.level = level
        player.displayName = displayName
        player.food = foodLevel
        player.helmet = armor[0]
        player.chestplate = armor[1]
        player.leggings = armor[2]
        player.boots = armor[3]
        player.gameMode = gamemode
    }
}