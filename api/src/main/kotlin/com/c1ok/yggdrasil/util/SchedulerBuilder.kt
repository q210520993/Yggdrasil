package com.c1ok.yggdrasil.util

import net.minestom.server.timer.ExecutionType
import net.minestom.server.timer.Scheduler
import net.minestom.server.timer.Task
import net.minestom.server.timer.TaskSchedule
import java.time.Duration
import java.time.temporal.TemporalUnit
import java.util.function.Supplier

class SchedulerBuilder {
    private var scheduler: Scheduler
    private var executionType = ExecutionType.TICK_START
    private var delay = TaskSchedule.immediate()
    private var repeat = TaskSchedule.stop()
    private var repeatOverride = false
    private var innerTask: Supplier<TaskSchedule>
    private var condition: Supplier<Boolean> = Supplier { true }
    private var conditionFalseTask: Runnable = Runnable {}


    constructor(scheduler: Scheduler, innerTask: Supplier<TaskSchedule>) {
        this.scheduler = scheduler
        this.innerTask = innerTask
    }

    constructor(scheduler: Scheduler, runnable: Runnable) {
        this.scheduler = scheduler
        this.innerTask = Supplier {
            runnable.run()
            return@Supplier TaskSchedule.stop()
        }
    }

    fun repeat(schedule: TaskSchedule): SchedulerBuilder {
        this.repeat = schedule
        this.repeatOverride = true
        return this
    }

    fun delay(taskSchedule: TaskSchedule): SchedulerBuilder {
        this.delay = taskSchedule
        return this
    }

    fun condition(supplier: Supplier<Boolean>): SchedulerBuilder {
        this.condition = supplier
        return this
    }

    fun conditionFalseTask(task: Runnable): SchedulerBuilder {
        this.conditionFalseTask = task
        return this
    }

    fun executionType(executionType : ExecutionType) : SchedulerBuilder {
        this.executionType = executionType
        return this
    }

    fun delay(duration: Duration): SchedulerBuilder {
        return delay(TaskSchedule.duration(duration))
    }

    fun delay(time: Long, unit: TemporalUnit): SchedulerBuilder {
        return delay(Duration.of(time, unit))
    }

    fun repeat(duration: Duration): SchedulerBuilder {
        return repeat(TaskSchedule.duration(duration))
    }

    fun repeat(time: Long, unit: TemporalUnit): SchedulerBuilder {
        return repeat(Duration.of(time, unit))
    }

    fun schedule(): Task {
        val innerTask = this.innerTask
        val delay = this.delay
        val repeat = this.repeat
        val repeatOverride = this.repeatOverride
        val executionType = this.executionType
        val condition = this.condition
        val conditionFalseTask = this.conditionFalseTask
        return scheduler.submitTask(object : Supplier<TaskSchedule> {
            var first: Boolean = true

            override fun get(): TaskSchedule {
                if (!condition.get()) {
                    conditionFalseTask.run()
                    return TaskSchedule.stop()
                }
                if (first) {
                    first = false
                    return delay
                }
                val schedule = innerTask.get()
                if (repeatOverride) {
                    return repeat
                }
                return schedule
            }
        }, executionType)
    }

}