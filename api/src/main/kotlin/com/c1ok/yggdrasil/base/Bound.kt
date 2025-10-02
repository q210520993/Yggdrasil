package com.c1ok.yggdrasil.base

/**
 * 这个接口，代表着一个游戏的伴生类
 */
interface Bound {

    /** 执行优先级 */
    val priority: Int

    /** 组件ID */
    val id: String

    /**
     * @param isFirstInit Boolean 游戏是否是第一次初始化
     */
    fun onInit(isFirstInit: Boolean) {}

    // 在游戏初始化结束后开启等待大厅后执行
    fun onLobby() {}

    // 在游戏开始时调用
    fun onGameStart() {}

    // 在游戏结束时调用
    fun onEnd() {}

    // 在游戏房间关闭时调用
    fun onClose() {}

    // 在游戏重建时调用
    fun onRebuild() {}

}