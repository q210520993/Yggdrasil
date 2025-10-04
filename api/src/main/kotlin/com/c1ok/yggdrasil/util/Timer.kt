package com.c1ok.yggdrasil.util

interface Trigger {

    fun onTimeTrigger(currentTime: Long)

    /**
     * 时间触发监听器接口
     * 这个接口的意思是在某一刻才会触发
     */
    interface OnTimeTriggerListener: Trigger {

        // 触发时机
        val triggerTime: Long

        // 是否是可以重复执行的
        val isRepeating: Boolean

        // 是否已经执行过了
        var triggered: Boolean

    }

    /**
     * 这个接口的意思是在每一刻都会触发
     * BaseGameStateMachine中是一秒
     */
    fun interface EveryTimeTriggerListener: Trigger

}

/**
 *
 * 秒针计时器
 * 它是线程安全的
 * @property default Int
 * @constructor
 */
class DefaultTimer(private val default: Long) {

    private var times = default

    // 存储触发配置的列表，支持多个触发点
    private val triggers = mutableListOf<Trigger>()

    @Synchronized
    fun addTime(time: Long): Long {
        times += time
        checkTriggers()
        return times
    }

    @Synchronized
    fun reduceTime(time: Long): Long {
        times -= time
        checkTriggers()
        return times
    }

    @Synchronized
    fun getTime(): Long {
        return times
    }

    /**
     * 重置计时器
     * @param clearTrigger Boolean 是否清理trigger
     */
    @Synchronized
    fun reset(clearTrigger: Boolean) {
        if (clearTrigger) triggers.clear()
        times = default
    }

    /**
     * 增加一个触发器
     * @param listener OnTimeTriggerListener 增加的触发器
     */
    @Synchronized
    fun addTrigger(listener: Trigger) {
        triggers.add(listener)
        checkTriggerForTarget(listener)
    }

    /**
     * 删除一个触发器
     * @param listener OnTimeTriggerListener 删除的触发器
     */
    @Synchronized
    fun removeTrigger(listener: Trigger) {
        triggers.add(listener)
    }

    private fun checkTriggers() {
        // 遍历所有触发配置的副本以避免并发修改异常
        triggers.forEach {
            checkTriggerForTarget(it)
        }
    }

    /**
     * 检查特定目标时间是否应触发
     */
    private fun checkTriggerForTarget(listener: Trigger) {
        when(listener) {
            is Trigger.OnTimeTriggerListener -> {
                // 检查当前时间是否达到或超过了目标时间
                if (times >= listener.triggerTime) {
                    if (listener.isRepeating) {
                        listener.onTimeTrigger(times)
                    } else {
                        if (!listener.triggered) {
                            listener.onTimeTrigger(times)
                            listener.triggered = true
                        }
                    }
                }
            }
            is Trigger.EveryTimeTriggerListener -> {
                listener.onTimeTrigger(times)
            }
        }
    }

}