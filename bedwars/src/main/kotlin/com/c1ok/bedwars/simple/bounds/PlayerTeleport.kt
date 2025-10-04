package com.c1ok.bedwars.simple.bounds

import com.c1ok.bedwars.BedwarsGame
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOriginUnsafe
import com.c1ok.yggdrasil.base.Bound

/**
 * 当游戏开始时，传送所有玩家
 * 它的优先级必须比分配队伍的要大
 * 否则可能会出现报错
 */
class PlayerTeleport(val game: BedwarsGame): Bound {

    override val priority: Int = 1

    override val id: String = "PlayerTeleport"

    override fun onGameStart() {
        game.getBedwarsPlayers().forEach {
            it.miniPlayer.getOriginUnsafe().teleport(it.getTeam()?.respawnPoint)
        }
    }

}