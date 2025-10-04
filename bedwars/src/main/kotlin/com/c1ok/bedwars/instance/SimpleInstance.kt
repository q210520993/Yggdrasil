package com.c1ok.bedwars.instance

import com.c1ok.yggdrasil.InstanceManager
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.utils.validate.Check
import java.util.function.Supplier

class SimpleInstance(val instanceGetter: Supplier<InstanceContainer>): InstanceManager {

    private lateinit var instance: InstanceContainer

    companion object {
        /***
         *
         * @param path String 世界文件的地址
         * @return ReuseInstance
         */
        fun of(path: String): ReuseInstance {
            val anvilLoader = AnvilLoader(path)
            val instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer(anvilLoader)
            return ReuseInstance(instanceContainer, true)
        }
    }

    override fun getCurrentInstance(): InstanceContainer {
        Check.isTrue(::instance.isInitialized, "游戏世界尚未进行初始化")
        return instance
    }

    @Synchronized
    override fun refrash() {
        if (::instance.isInitialized) {
            MinecraftServer.getInstanceManager().unregisterInstance(instance)
        }
        val new = instanceGetter.get()
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