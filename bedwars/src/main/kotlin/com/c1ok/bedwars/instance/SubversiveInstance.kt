package com.c1ok.bedwars.instance

import com.c1ok.yggdrasil.InstanceManager
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.utils.validate.Check


/***
 *
 * 该世界的并不是复用的，而是每一次游戏的重启都会重新创建一个世界
 *
 * @property origin InstanceContainer
 * @property instance InstanceContainer
 * @constructor
 */
class SubversiveInstance(val origin: InstanceContainer): InstanceManager {

    private lateinit var instance: InstanceContainer

    override fun getCurrentInstance(): InstanceContainer {
        Check.isTrue(::instance.isInitialized, "游戏世界尚未进行初始化，无法进行更新")
        return instance
    }

    override fun refrash() {
        if (::instance.isInitialized) {
            MinecraftServer.getInstanceManager().unregisterInstance(instance)
        }
        val new = origin.copy()
        if (!new.isRegistered) {
            MinecraftServer.getInstanceManager().registerInstance(new)
        }
        instance = new
    }

    override fun close() {
        if (::instance.isInitialized) {
            MinecraftServer.getInstanceManager().unregisterInstance(instance)
        }
    }

}