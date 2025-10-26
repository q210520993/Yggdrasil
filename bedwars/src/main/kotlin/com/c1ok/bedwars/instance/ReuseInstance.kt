package com.c1ok.bedwars.instance

import com.c1ok.bedwars.Bedwars
import com.c1ok.yggdrasil.InstanceManager
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Point
import net.minestom.server.entity.Player
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.validate.Check
import java.util.concurrent.ConcurrentHashMap

/***
 *
 * 该世界的是复用的，而是每一次游戏的重启都会采用这个游戏世界
 *
 * @property origin InstanceContainer
 * @property instance InstanceContainer
 * @property isUseOrigin Boolean 如果为true，哪么currentInstance将采用origin
 * @constructor
 */
class ReuseInstance(val origin: InstanceContainer, val isUseOrigin: Boolean): InstanceManager {

    private lateinit var instance: InstanceContainer
    private val blocks: MutableMap<Point, Block> = ConcurrentHashMap()

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

    fun addBlock(block: Block, point: Point) {
        blocks[point] = block
    }

    fun removeBlock(point: Point) {
        blocks.remove(point)
    }

    fun containBlock(point: Point): Boolean {
        return blocks.containsKey(point)
    }

    fun getBlock(point: Point): Block? {
        return blocks[point]
    }

    override fun getCurrentInstance(): InstanceContainer {
        if (isUseOrigin) {
            return origin
        }
        Check.isTrue(::instance.isInitialized, "游戏世界尚未进行初始化")
        return instance
    }

    /***
     *
     * 因为是复用世界
     * 所以它会进行一些清理操作
     *
     */
    @Synchronized
    override fun refrash() {

        if (isUseOrigin && !::instance.isInitialized) {
            if (!origin.isRegistered) {
                MinecraftServer.getInstanceManager().registerInstance(origin)
            }
            instance = origin
            return
        }
        if (!::instance.isInitialized) {
            val new = origin.copy()
            MinecraftServer.getInstanceManager().registerInstance(new)
            instance = new
            return
        }
        blocks.apply {
            blocks.forEach { (k,v) ->
                getCurrentInstance().setBlock(k, Block.AIR)
            }
            clear()
        }
        getCurrentInstance().entities.filter {
            it !is Player
        }.forEach {
            it.remove()
        }
    }

    override fun close() {
        if (::instance.isInitialized) {
            MinecraftServer.getInstanceManager().unregisterInstance(instance)
        }
    }

}