package com.c1ok.bedwars.utils

import com.c1ok.bedwars.simple.SimpleSpecialManager
import net.minestom.server.item.ItemStack

fun ItemStack.Builder.setSpecial(id: String): ItemStack.Builder {
    return this.set(SimpleSpecialManager.tag, id)
}