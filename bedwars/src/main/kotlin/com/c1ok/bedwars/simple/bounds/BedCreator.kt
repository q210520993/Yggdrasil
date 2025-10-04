package com.c1ok.bedwars.simple.bounds

import com.c1ok.bedwars.BedwarsGame
import com.c1ok.yggdrasil.base.Bound

class BedCreator(val game: BedwarsGame): Bound {

    override val priority: Int = 0
    override val id: String = "BedCreator"

    override fun onGameStart() {
        game.getTeams().filter { it.getIsWipedOut() }.forEach {
            it.createBed()
        }
    }

}