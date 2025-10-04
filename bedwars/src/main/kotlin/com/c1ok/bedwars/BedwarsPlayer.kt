package com.c1ok.bedwars

import com.c1ok.bedwars.simple.SimpleBedwarsGame
import com.c1ok.yggdrasil.MiniPlayer
import com.c1ok.yggdrasil.StoredInventory
import com.c1ok.yggdrasil.util.Result
import net.minestom.server.scoreboard.Sidebar

/**
 * 它代表着一个开始中的游戏内部的玩家
 * @property miniPlayer MiniPlayer
 */
interface BedwarsPlayer {

    val miniPlayer: MiniPlayer

    val storedInventory: StoredInventory

    val game: SimpleBedwarsGame

    /**
     * 是否为旁观者
     */
    var spectator: Boolean

    /**
     * 这是玩家的个性sidebar
     */
    val sidebar: Sidebar

    /**
     * 设置玩家的队伍
     * @param team Team
     * @return Result<Boolean>
     */
    fun setTeam(team: Team): Result<Boolean>

    /**
     * 设置玩家的队伍
     * @param team Team
     * @return Result<Boolean>
     */
    fun getTeam(): Team?

    /**
     * @param resourceID String
     * @return Int
     */
    fun getResource(resourceID: String): Int

    fun addResource(resourceID: String, count: Int): Result<Boolean>

    /**
     * 得到杀敌数
     * @return Int
     */
    fun getKillsCount(): Int

    /**
     * 得到死亡数
     * @return Int
     */
    fun getDeathsCount(): Int
    /**
     * 得到挖掘床的数量
     * @return Int
     */
    fun getBedBrokensCount(): Int

    /**
     *
     * 刷新玩家的东西和玩家的storedInventory
     *
     */
    fun refreshPlayer()

}