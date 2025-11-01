package com.c1ok.bedwars.games.train

import com.c1ok.bedwars.BedwarsGame
import com.c1ok.bedwars.entity.Trader
import com.c1ok.yggdrasil.base.Bound
import net.minestom.server.coordinate.Pos

class TraderCreator(
    val game: BedwarsGame,
    val traders: List<Pos>
): Bound {
    override val priority: Int = 1
    override val id: String = "TraderCreator"

    override fun onGameStart() {
        traders.forEach {
            val trader = Trader()
            trader.setInstance(game.instanceManager.getCurrentInstance(), it)
        }
    }

}