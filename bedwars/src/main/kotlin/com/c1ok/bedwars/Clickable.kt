package com.c1ok.bedwars

import net.minestom.server.entity.Player

/**
 * 右键时触发
 * 这只是一个例子
 */
interface Clickable: SpecialItem {
    fun onClick(player: Player)
}