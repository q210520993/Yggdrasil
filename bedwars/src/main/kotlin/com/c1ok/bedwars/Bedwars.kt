package com.c1ok.bedwars

import com.c1ok.bedwars.simple.SimpleSpecialManager
import com.c1ok.bedwars.simple.listener.SimplePlayerListener
import com.c1ok.yggdrasil.GameManager
import com.c1ok.yggdrasil.MiniPlayerManager
import net.minestom.server.ServerProcess
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.instance.Instance
import net.minestom.server.utils.validate.Check

/**
 *
 * 这是一个Bedwars的核心类
 * 它初始化所有的东西
 *
 * @property eventNode EventNode<Event>
 * @property playerManager MiniPlayerManager
 * @property gameManager GameManager<BedwarsGame>
 * @property minecraftProcess ServerProcess
 * @property lobby Instance
 * @constructor
 */
class Bedwars(
    val eventNode: EventNode<Event>,
    val playerManager: MiniPlayerManager,
    val gameManager: GameManager<BedwarsGame>,
    val minecraftProcess: ServerProcess,
    val lobby: Instance
) {

    companion object {
        lateinit var instance: Bedwars
            private set

        private fun getIsInit(): Boolean {
            return ::instance.isInitialized
        }

    }

    var isSimple: Boolean = true

    /**
     * 得到一个实例后，调用该方法，将把该类作为一个主Bedwars类
     * 它会自动注册一些东西
     */
    fun apply() {
        Check.isTrue(!getIsInit(), "无法再次应用，该Bedwars已经初始化过一次了")
        instance = this
        registerEventNode()
    }

    private fun registerEventNode() {
        minecraftProcess.eventHandler().addChild(eventNode)
        if (isSimple) {
            SimplePlayerListener.register()
            SimpleSpecialManager.registerListenerToGame(eventNode, playerManager)
        }
    }

    class Builder(val process: ServerProcess) {

        private lateinit var eventNode: EventNode<Event>

        private lateinit var playerManager: MiniPlayerManager

        private lateinit var gameManager: GameManager<BedwarsGame>

        private lateinit var lobby: Instance

        /**
         * 是否应用简单模式的模板
         */
        private var isSimple: Boolean = true

        fun setLobby(instance: Instance): Builder {
            this.lobby = instance
            return this
        }

        fun setEventNode(eventNode: EventNode<Event>): Builder {
            this.eventNode = eventNode
            return this
        }

        fun setGameManager(gameManager: GameManager<BedwarsGame>): Builder {
            this.gameManager = gameManager
            return this
        }

        fun setPlayerManager(playerManager: MiniPlayerManager): Builder {
            this.playerManager = playerManager
            return this
        }

        fun setSimple(bool: Boolean): Builder {
            this.isSimple = bool
            return this
        }

        fun build(): Bedwars {
            val bedwars= Bedwars(eventNode, playerManager, gameManager, process, lobby)
            bedwars.isSimple = isSimple
            return bedwars
        }

    }

}