package com.c1ok.bedwars.simple

import com.c1ok.bedwars.BedwarsGame
import com.c1ok.bedwars.Generator
import com.c1ok.bedwars.ResourceEntity
import com.c1ok.bedwars.SpawnResourceType
import com.c1ok.yggdrasil.GameState
import com.c1ok.yggdrasil.util.SchedulerBuilder
import net.minestom.server.timer.TaskSchedule
import net.minestom.server.utils.validate.Check
import java.util.concurrent.ConcurrentHashMap

open class SimpleGenerator(val game: BedwarsGame): Generator {
    val resources = ConcurrentHashMap<String, SpawnResourceType>()
    // 指当前运行tick
    private var currentTime = 0
    var started = false
    var forceStop = false

    override fun start() {
        if (started) return
        forceStop = false
        SchedulerBuilder(game.instanceManager.getCurrentInstance().scheduler(), Runnable {
            currentTime += 10
            val currentTimeUnwarp = currentTime
            resources.filter {
                currentTimeUnwarp % it.value.spawnInterval == 0
            }.forEach {
                val resourceEntity = ResourceEntity(it.value)
                resourceEntity.setInstance(game.instanceManager.getCurrentInstance(), it.value.point)
            }
        }).condition {
            game.gameStateMachine.getCurrentState() == GameState.STARTING && !forceStop
        }.repeat(TaskSchedule.tick(10)).conditionFalseTask {
            started = false
        }.conditionFalseTask {
            currentTime = 0
        }.schedule()
    }

    override fun addGenerator(resourceType: SpawnResourceType) {
        resources[resourceType.id] = resourceType
    }

    override fun resetGenerator(resourceType: SpawnResourceType) {
        Check.isTrue(resources.containsKey(resourceType.id), "你无法重设该generator，因为它不存在")
        resources.remove(resourceType.id)
        addGenerator(resourceType)
    }

    override fun removeGenerator(resourceType: SpawnResourceType) {
        resources.remove(resourceType.id)
    }

    override fun removeGenerator(id: String) {
        resources.remove(id)
    }

    override fun getGenerator(id: String): SpawnResourceType? {
        return resources[id]
    }

    override fun close() {
        started = false
        forceStop = true
    }

}
