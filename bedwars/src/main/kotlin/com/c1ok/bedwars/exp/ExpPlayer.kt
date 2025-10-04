package com.c1ok.bedwars.exp

import com.c1ok.bedwars.Team
import com.c1ok.bedwars.simple.SimpleBedwarsPlayer
import com.c1ok.yggdrasil.MiniPlayer
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import com.c1ok.yggdrasil.util.Reason
import com.c1ok.yggdrasil.util.Result
import net.minestom.server.scoreboard.Sidebar

class ExpPlayer(miniPlayer: MiniPlayer, game: ExpBedwarsGame) : SimpleBedwarsPlayer(miniPlayer, game) {

    override var spectator: Boolean = false

    private var team_: Team? = null

    override val sidebar: Sidebar
        get() = TODO("Not yet implemented")

    override fun getTeam(): Team? {
        return team_
    }

    override fun getResource(resourceID: String): Int {
        if (resourceID != "level") return 0
        val player = miniPlayer.getOrigin() ?: return 0
        return player.level
    }

    override fun addResource(resourceID: String, count: Int): Result<Boolean> {
        if (resourceID != "level") return Result(false, Reason.Failed("经验模式.."))
        val player = miniPlayer.getOrigin() ?: return Result(false, Reason.Failed("空"))
        player.level += count
        return Result(true, Reason.Success)
    }


}