package com.c1ok.yggdrasil

/**
 *
 * 它本身应该是存在于MiniGame接口里的
 * 但我觉得不太必要，便把它解耦出来了
 * 这个类必须绑定一个MiniGame
 * @property game MiniGame
 */
interface GameStateMachine {

    /**
     * 它必须绑定一个游戏
     */
    val game: MiniGame

    /**
     * 得到当前的游戏状态
     * @return GameState
     */
    fun getCurrentState(): GameState

    /**
     *
     * 这代表着GameState进入Init状态
     * 它的内部会执行game的Init方法，链式调用下去
     * Init -> Lobby
     */
    fun init()

    /**
     * 这代表着让游戏进入等待大厅状态
     * 它启动的前提必须游戏在Init状态中
     */
    fun startLobby()

    /**
     * 这代表着让游戏开始
     * 它启动的前提游戏必须在Lobby状态中
     */
    fun startGame()

    /**
     *
     * 这代表着让游戏结束
     * 它启动的前提是游戏不在init/restart状态
     *
     */
    fun endGame()

    /**
     *
     * 这代表着重启游戏
     * 前提是游戏在Starting状态中
     *
     */
    fun restartGame()

    /**
     *
     * 这是一个关闭游戏的措施
     * 游戏将会强制被关闭
     *
     */
    fun shutdown()

}