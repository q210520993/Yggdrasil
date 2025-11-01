package com.c1ok.yggdrasil

import com.c1ok.yggdrasil.util.Result
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.scoreboard.Sidebar
import net.minestom.server.timer.Task
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 *
 * 代表着一个小游戏的接口
 * 真正的核心在于游戏状态控制器而并非MiniGame
 *
 */
interface MiniGame {

    val maxPlayers: Int

    val minPlayers: Int

    fun getCurrentState(): GameState {
        return gameStateMachine.getCurrentState()
    }

    /**
     * 游戏的uuid
     */
    val uuid: UUID

    /**
     * 游戏的展示名称
     */
    val displayName: Component

    /**
     * 这是一个游戏的核心
     * 它控制着游戏的启动与结束的流程
     */
    val gameStateMachine: GameStateMachine

    /**
     * 这是一个游戏的世界管理器
     * 它操控着游戏的世界刷新，生成等等
     */
    val instanceManager: InstanceManager

    /**
     * 我建议把你的游戏任务都注册到这里
     * 否则可能会出现一些问题
     */
    fun registerTask(task: Task)

    /**
     * 卸载任务
     * @param task Task
     */
    fun unregisterTask(task: Task)

    /**
     * 获取所有在注册表里的任务
     * @return Collection<Task>
     */
    fun getAllRegisteredTasks(): Collection<Task>

    /**
     * 此方法是一个MiniGame激活的方法
     * Boolean代表着这是否是一个刚刚实例化还没有被Init过的MiniGame
     *
     * @return CompletableFuture<Boolean>
     */
    fun init(): CompletableFuture<Boolean>

    /**
     * 依附于 GameStateMachine, 它们都会在状态改变前执行
     */
    fun onStart(): CompletableFuture<Void>
    fun onLobby(): CompletableFuture<Void>
    fun onEnd(): CompletableFuture<Void>

    /**
     * GameStateMachine会在restart的时候执行，这会进行一些清理操作
     */
    fun onRestart(): CompletableFuture<Void>

    /**
     * 这不绑定在GameStateMachine，这是强制关闭一个游戏房间的措施
     */
    fun shutdown(): CompletableFuture<Void>

    /**
     *
     * 添加一个玩家,使玩家进入游戏世界
     * Result是一个Boolean
     * false则是拒绝添加玩家或者出现了一些错误
     *
     * @param player MiniPlayer
     * @return Result<Boolean>
     */
    fun addPlayer(player: MiniPlayer): Result<Boolean>

    /**
     *
     * 删除一个玩家， 使玩家退出游戏世界
     * Result是一个Boolean
     * false则是拒绝删除玩家或者出现了一些错误
     *
     * @param player MiniPlayer
     * @return Result<Boolean>
     */
    fun removePlayer(player: MiniPlayer): Result<Boolean>

    /**
     * @param uuid UUID
     * @return MiniPlayer?
     */
    fun getMiniPlayer(uuid: UUID): MiniPlayer? {
        return getPlayers().firstOrNull { it.uuid == uuid }
    }

    fun getMiniPlayer(player: Player): MiniPlayer? {
        return getMiniPlayer(player.uuid)
    }

    fun getPlayers(): Collection<MiniPlayer>

    /**
     * 得到所有在服务器中的玩家
     * @return Collection<MiniPlayer>
     */
    fun getOnlinePlayer(): Collection<MiniPlayer> {
        return getPlayers().filter { it.isOnline() }
    }

    /**
     * 得到所有不在当前服务器中的玩家，但是在游戏开始时在进行该游戏
     * @return Collection<MiniPlayer>
     */
    fun getOfflinePlayer(): Collection<MiniPlayer> {
        return getPlayers().filter { !it.isOnline() }
    }

    /**
     * 获取到玩家的sidebar
     * @param miniPlayer MiniPlayer
     * @return Sidebar
     */
    fun getPlayerSidebar(miniPlayer: MiniPlayer): Sidebar?

    /**
     * 获取到一种全局的sidebar
     * @param id String
     * @return Sidebar
     */
    fun getGlobalSidebar(id: String): Sidebar?

}