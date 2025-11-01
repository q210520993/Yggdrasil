package com.c1ok.bedwars.invui

import com.c1ok.bedwars.utils.getBedwarsPlayer
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import net.minestom.server.inventory.click.Click.LeftShift
import xyz.xenondevs.invui.item.Click
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.impl.SimpleItem
import java.util.function.Consumer
import java.util.function.Predicate

class CommodityItem private constructor(
    itemProvider: ItemProvider,
    clickHandler: Consumer<Click>,
): SimpleItem(itemProvider, clickHandler) {

    constructor(itemProvider: ItemProvider, predicate: Predicate<Click>, afterBuy: Consumer<Click>): this(itemProvider, Consumer {
        if (predicate.test(it)) {
            afterBuy.accept(it)
        }
    })

    /**
     * @param itemProvider ItemProvider
     * @param givenItemProvider ItemProvider 在Predict成功后给予玩家的物品
     * @param resourceType String 需要的资源类型
     * @param singlePrice Int 需要消耗的资源类型
     * 此方法会根据clickType来进行一个匹配
     * 例如如果玩家是shiftLeft，则会进行一个最大的物品堆的处理
     * @constructor
     */
    constructor(displayProvider: ItemProvider, givenItemProvider: ItemProvider, resourceType: String, singlePrice: Int): this(displayProvider,
        Predicate<Click> {
            val bedwarsPlayer = it.player.getBedwarsPlayer() ?: return@Predicate false
            val clickType = it.clickType
            val price: Int = when(clickType) {
                is net.minestom.server.inventory.click.Click.Left -> {
                    singlePrice
                }

                is LeftShift -> {
                    singlePrice * givenItemProvider.get().maxStackSize()
                }

                else -> {
                    return@Predicate false
                }
            }
            if (bedwarsPlayer.getResource(resourceType) < price) {
                it.player.sendMessage("你不足以购买此物品")
                return@Predicate false
            }
            bedwarsPlayer.addResource(resourceType, -price)
            return@Predicate true

    } , {
            when(it.clickType) {
                is net.minestom.server.inventory.click.Click.Left -> {
                    val item = givenItemProvider.get()
                    it.player.inventory.addItemStack(item)
                }

                is LeftShift -> {
                    val _item = givenItemProvider.get()
                    val item = _item.withAmount(_item.maxStackSize())
                    it.player.inventory.addItemStack(item)
                }
                else -> {

                }
            }
    })
}