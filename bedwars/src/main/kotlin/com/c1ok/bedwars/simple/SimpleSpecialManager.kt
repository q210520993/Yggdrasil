package com.c1ok.bedwars.simple

import com.c1ok.bedwars.BedwarsGame
import com.c1ok.bedwars.Clickable
import com.c1ok.bedwars.SpecialItem
import com.c1ok.bedwars.SpecialItemManager
import com.c1ok.yggdrasil.*
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerUseItemEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.tag.Tag
import net.minestom.server.tag.TagHandler
import java.util.concurrent.ConcurrentHashMap

class SimpleSpecialManager: SpecialItemManager {

    companion object {
        val tag = Tag.String("special_item_manager")

        private val tagHandler: TagHandler = TagHandler.newHandler()

        private val node: EventNode<Event> = EventNode.all("special_item_simple").setPriority(16)

        /**
         * 注册一个SimpleSpecialManager的触发器
         * @param eventNode EventNode<Event>
         * @param gameManager GameManager<out MiniGame>
         * @param playerManager MiniPlayerManager
         */
        fun registerListenerToGame(eventNode: EventNode<Event>,
                                    playerManager: MiniPlayerManager) {
            eventNode.addChild(node)
            node.addListener(PlayerUseItemEvent::class.java) {
                val player = playerManager.getMiniPlayer(it.player) ?: return@addListener
                val game = player.game as? BedwarsGame ?: return@addListener
                val isSpecial = game.isSpecial(it.itemStack)
                if (!isSpecial) return@addListener
                val special = game.getSpecialHandler(it.itemStack) ?: return@addListener
                if (special is Clickable) {
                    special.onClick(it.player)
                }
            }
        }

    }

    private val specialItems = ConcurrentHashMap<String, SpecialItem>()

    override fun getSpecialHandler(itemStack: ItemStack): SpecialItem? {
        val id = itemStack.getTag(tag) ?: return null
        return specialItems[id]
    }

    override fun getSpecialHandler(id: String): SpecialItem? {
        return specialItems[id]
    }

    override fun isSpecial(itemStack: ItemStack): Boolean {
        return itemStack.hasTag(tag)
    }

    override fun addSpecial(specialItem: SpecialItem): SpecialItem? {
        return specialItems.put(specialItem.tagValue, specialItem)
    }

    override fun setItemToSpecial(itemStack: ItemStack, specialItem: SpecialItem): Boolean {
        if (itemStack.hasTag(tag)) {
            return false
        }
        tagHandler.setTag(tag, specialItem.tagValue)
        return true
    }

    override fun unsafeSetItemToSpecial(itemStack: ItemStack, specialItem: SpecialItem) {
        tagHandler.setTag(tag, specialItem.tagValue)
    }

}