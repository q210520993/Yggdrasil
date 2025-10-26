package com.c1ok.bedwars.invui.guis

import net.minestom.server.item.Material
import net.minestom.server.item.enchant.Enchantment
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.TabGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.item.impl.controlitem.TabItem

/**
 * 本质上只是拼插了TabGui而已
 */
class SimpleGui {

    class MyTabItem(private val tab: Int, val origin: ItemBuilder, val after: ItemBuilder) : TabItem(tab) {

        override fun getItemProvider(gui: TabGui): ItemProvider {
            return if (gui.currentTab == tab) {
                origin
            } else {
                after
            }
        }

    }

    val gui1 = Gui.empty(9,3)
    val gui2 = Gui.empty(9,3)

    val border = SimpleItem(ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""))

    val tabGui = TabGui.normal()
        .setStructure(
            "1 2 # # # # # # #",
            "x x x x x x x x x",
            "x x x x x x x x x",
            "x x x x x x x x x")
        .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
        .addIngredient('#', border)
        .addIngredient('1', MyTabItem(0, ItemBuilder(Material.DIAMOND_SWORD), ItemBuilder(Material.DIAMOND_SWORD).addEnchantment(Enchantment.FLAME.asValue(), 1, false)))
        .addIngredient('2', MyTabItem(1 ,ItemBuilder(Material.DIAMOND_BLOCK), ItemBuilder(Material.DIAMOND_BLOCK)
            .addEnchantment(Enchantment.FLAME.asValue(), 1, false)))
        .build()


}