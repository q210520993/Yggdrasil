package com.c1ok.bedwars.invui

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

}