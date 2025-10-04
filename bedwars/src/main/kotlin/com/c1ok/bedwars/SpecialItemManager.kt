package com.c1ok.bedwars

import net.minestom.server.item.ItemStack

/**
 * 主要负责特殊的物品
 */
interface SpecialItemManager {

    fun getSpecialHandler(itemStack: ItemStack): SpecialItem?
    fun getSpecialHandler(id: String): SpecialItem?

    fun isSpecial(itemStack: ItemStack): Boolean

    fun addSpecial(specialItem: SpecialItem): SpecialItem?

    fun setItemToSpecial(itemStack: ItemStack, specialItem: SpecialItem): Boolean
    fun unsafeSetItemToSpecial(itemStack: ItemStack, specialItem: SpecialItem)


}