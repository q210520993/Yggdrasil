package com.c1ok.yggdrasil.base

import com.c1ok.yggdrasil.GameState
import com.c1ok.yggdrasil.MiniGame
import com.c1ok.yggdrasil.MiniPlayer
import com.c1ok.yggdrasil.util.SchedulerBuilder
import net.minestom.server.timer.Task
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet

abstract class BaseMiniGame: MiniGame {

    /** 是否第一次启动 */
    protected var isFirst = true
    protected val bounds: MutableList<Bound> = CopyOnWriteArrayList()

    protected val tasks: MutableList<Task> = CopyOnWriteArrayList()

    override fun registerTask(task: Task) {
        tasks.add(task)
    }

    override fun unregisterTask(task: Task) {
        tasks.remove(task)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    /**
     * 用来添加一些东西
     * @param bound Array<out Bound>
     */
    fun addBound(vararg bound: Bound) {
        bounds.addAll(bound)
        bounds.sortedBy { it.priority }
    }

    fun removeBound(id: String) {
        bounds.removeIf { it.id == id }
    }

    override fun init(): CompletableFuture<Boolean> {
        try {
            // 刷新世界
            instanceManager.refrash()
            gameStateMachine.init()
            // 做一个任务用来定时清理已经结束的任务
            SchedulerBuilder(instanceManager.getCurrentInstance().scheduler(), Runnable {
                tasks.removeIf {
                    !it.isAlive
                }
            }).repeat(Duration.ofSeconds(1)).condition {
                gameStateMachine.getCurrentState() == GameState.STARTING
                        || gameStateMachine.getCurrentState() == GameState.INITIALIZING
            }.schedule()
            bounds.forEach {
                it.onInit(isFirst)
            }
            val future = when(isFirst) {
                true -> CompletableFuture.completedFuture(true)
                false -> CompletableFuture.completedFuture(false)
            }
            isFirst = false
            return future
        } catch (e: Exception) {
            logger.error(e.message, e)
            shutdown()
            return CompletableFuture.completedFuture(false)
        }
    }

    override fun onEnd(): CompletableFuture<Void> {
        bounds.forEach {
            it.onEnd()
        }
        return CompletableFuture.completedFuture(null)
    }

    override fun onLobby(): CompletableFuture<Void> {
        bounds.forEach {
            it.onLobby()
        }
        return CompletableFuture.completedFuture(null)
    }

    override fun onStart(): CompletableFuture<Void> {
        bounds.forEach {
            it.onGameStart()
        }
        return CompletableFuture.completedFuture(null)
    }

    override fun onRestart(): CompletableFuture<Void> {
        bounds.forEach {
            it.onRebuild()
        }
        return CompletableFuture.completedFuture(null)
    }

    override fun shutdown(): CompletableFuture<Void> {
        gameStateMachine.shutdown()
        bounds.forEach {
            it.onClose()
        }
        return CompletableFuture.completedFuture(null)
    }

}