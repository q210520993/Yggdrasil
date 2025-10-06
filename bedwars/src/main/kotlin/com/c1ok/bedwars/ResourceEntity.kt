package com.c1ok.bedwars

import net.minestom.server.entity.ItemEntity
import net.minestom.server.event.EventDispatcher
import net.minestom.server.event.entity.EntityItemMergeEvent
import net.minestom.server.instance.EntityTracker
import net.minestom.server.item.ItemStack
import net.minestom.server.utils.MathUtils
import net.minestom.server.utils.time.Cooldown
import net.minestom.server.utils.time.TimeUnit
import java.time.Duration

open class ResourceEntity(val resourceType: SpawnResourceType): ItemEntity(ItemStack.of(resourceType.material)) {

    /**
     * The last time that this item has checked his neighbors for merge
     */
    private var lastMergeCheck: Long = 0

    /**
     * Used to slow down the merge check delay
     */
    private var mergeDelay: Duration = Duration.of(10, TimeUnit.SERVER_TICK)

    override fun update(time: Long) {
        if (isMergeable && isPickable && (!Cooldown.hasCooldown(time, lastMergeCheck, mergeDelay))
        ) {
            lastMergeCheck = time

            instance.entityTracker.nearbyEntities(
                position, mergeRange.toDouble(),
                EntityTracker.Target.ITEMS
            ) { itemEntity: ItemEntity ->
                if (itemEntity === this) return@nearbyEntities
                if (!itemEntity.isPickable || !itemEntity.isMergeable) return@nearbyEntities

                val itemStackEntity = itemEntity.itemStack
                val canStack = itemStack.isSimilar(itemStackEntity)

                if (!canStack) return@nearbyEntities
                val totalAmount = itemStack.amount() + itemStackEntity.amount()
                if (!MathUtils.isBetween(
                        totalAmount,
                        0,
                        itemStack.maxStackSize()
                    )
                ) return@nearbyEntities
                val result = itemStack.withAmount(totalAmount)
                val entityItemMergeEvent =
                    EntityItemMergeEvent(this, itemEntity, result)
                EventDispatcher.callCancellable(
                    entityItemMergeEvent
                ) {
                    itemStack = entityItemMergeEvent.result
                    itemEntity.remove()
                }
            }
        }
    }
}