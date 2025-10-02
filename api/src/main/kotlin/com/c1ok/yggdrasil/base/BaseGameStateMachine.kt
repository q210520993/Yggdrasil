package com.c1ok.yggdrasil.base

import com.c1ok.yggdrasil.GameState
import com.c1ok.yggdrasil.GameStateMachine
import com.c1ok.yggdrasil.util.DefaultTimer
import com.c1ok.yggdrasil.util.SchedulerBuilder
import net.minestom.server.utils.validate.Check
import org.slf4j.LoggerFactory
import java.time.Duration

abstract class BaseGameStateMachine(override val game: BaseMiniGame): GameStateMachine {

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    /**
     * 等待大厅的等待时间, 单位秒
     */
    val WAITING_TIME = DefaultTimer(120)

    /**
     * 游戏时长，单位秒
     */
    val GAME_TIME = DefaultTimer(3600)

    protected var isWatingClock = false

    override fun init() {
        if (getCurrentState() != GameState.CLOSED && getCurrentState() != GameState.RESTARTING) {
            logger.error("初始化失败，状态不对")
        }
        // TODO Call Game Init Event
        setState(GameState.INITIALIZING)
        startLobby()
    }

    override fun startLobby() {
        // 修正：只允许从 INITIALIZING 或 CLOSED 状态进入 LOBBY
        if (getCurrentState() != GameState.INITIALIZING && getCurrentState() != GameState.CLOSED) {
            logger.error("当前状态为 ${getCurrentState()}，无法进入等待大厅")
            return
        }
        setState(GameState.LOBBY)
        game.onLobby()
        SchedulerBuilder(game.instanceManager.getCurrentInstance().scheduler(),
            Runnable {
                if (game.instanceManager.getCurrentInstance().players.size >= game.minPlayers) {
                    isWatingClock = true
                    WAITING_TIME.reduceTime(1)
                }
                if (game.instanceManager.getCurrentInstance().players.size < game.minPlayers) {
                    isWatingClock = false
                    WAITING_TIME.reset(false)
                }
            })
            .condition {
                WAITING_TIME.getTime() >= 0 && getCurrentState() == GameState.LOBBY
            }
            .conditionFalseTask {
                isWatingClock = false
                WAITING_TIME.reset(true)
                if (getCurrentState() != GameState.LOBBY) return@conditionFalseTask
                startGame()
            }.repeat(Duration.ofSeconds(1)).schedule()
    }

    override fun startGame() {
        if(getCurrentState() != GameState.LOBBY) {
            logger.error("游戏已经被启动了，无法再启动")
            return
        }
        setState(GameState.STARTING)
        game.onStart()
        SchedulerBuilder(game.instanceManager.getCurrentInstance().scheduler(), Runnable {
            GAME_TIME.reduceTime(1)
        }).delay(Duration.ofSeconds(1)).condition {
            getGameEndCondition()
        }.conditionFalseTask {
            GAME_TIME.reset(true)
            endGame()
        }.repeat(Duration.ofSeconds(1)).schedule()
    }

    /**
     * 查看游戏是否该结束了
     * 如果为true的时候，哪么就会立刻结束游戏
     * 这会在Starting中执行
     */
    protected abstract fun getGameEndCondition(): Boolean

    protected abstract fun setState(state: GameState)

    override fun endGame() {
        Check.stateCondition(getCurrentState() != GameState.STARTING, "游戏都没进行，无法直接结算")
        setState(GameState.ENDED)
        game.onEnd().thenAccept {
            restartGame()
        }
    }

    override fun restartGame() {
        Check.stateCondition(getCurrentState() == GameState.RESTARTING, "游戏已经进入重启状态了，无法再次进入")
        setState(GameState.RESTARTING)
        game.restart().thenAccept {
            // 不直接调用 game.init()，而是提交一个延迟任务，目的是延迟到所有任务皆以关闭 :)
            // 这个延迟任务的实现也是有点抽象的，就是每隔一秒都会检查一下有没有任务不是关闭的，如果有，哪就继续，没有的话就进入init了
            SchedulerBuilder(game.instanceManager.getCurrentInstance().scheduler(), Runnable {}).delay(Duration.ofSeconds(1)).condition {
                game.getAllRegisteredTasks().any {
                    it.isAlive
                }
            }.conditionFalseTask {
                game.init()
            }.repeat(Duration.ofSeconds(1)).schedule() // 延迟 几 秒等待所有任务结束 :P
        }
    }

    override fun shutdown() {
    }

}