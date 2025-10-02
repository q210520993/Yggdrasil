package com.c1ok.yggdrasil

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import java.util.*

/**
 *
 * 这代表的是一个服务器的玩家，就像是Minestom的Player对象
 * 我建议每个游戏具体的实现还要有个游戏内部的玩家
 * 比如Bedwars就有一个BedwarsPlayer
 * @property uuid UUID
 * @property game MiniGame?
 */
interface MiniPlayer {

    val uuid: UUID

    /**
     * 玩家当前在游玩的游戏
     */
    var game: MiniGame?

    /**
     * @return Boolean 玩家是否在服务器中
     */
    fun isOnline(): Boolean {
        return getOrigin()?.isOnline ?: false
    }

//    /**
//     *
//     * 强制性离开游戏
//     *
//     * @return Result<Boolean>
//     */
//    fun levelGame()

    companion object {

        /**
         * 它在获取时无报错
         * 但可能会是null
         * @param miniPlayer MiniPlayer
         * @return Player?
         */
        @JvmStatic
        fun MiniPlayer.getOrigin(): Player? {
            return MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid)
        }

        @JvmStatic
        fun MiniPlayer.getOriginUnsafe(): Player {
            return getOrigin()!!
        }

    }

}