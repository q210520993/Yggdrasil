package com.c1ok.yggdrasil

import net.minestom.server.instance.InstanceContainer

/***
 * 确保着游戏世界的刷新
 */
interface InstanceManager {

    /**
     * 刷新CurrentInstance
     */
    fun refrash()


    // 当前的游戏世界
    fun getCurrentInstance(): InstanceContainer

    /**
     * 关闭时
     */
    fun close()

}