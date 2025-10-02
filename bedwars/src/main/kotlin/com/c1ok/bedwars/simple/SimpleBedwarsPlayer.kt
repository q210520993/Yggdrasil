package com.c1ok.bedwars.simple

import com.c1ok.bedwars.BedwarsPlayer
import com.c1ok.bedwars.Team
import com.c1ok.yggdrasil.GameState
import com.c1ok.yggdrasil.MiniPlayer
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOriginUnsafe
import com.c1ok.yggdrasil.StoredInventory
import com.c1ok.yggdrasil.util.Reason
import com.c1ok.yggdrasil.util.Result
import net.minestom.server.entity.GameMode
import net.minestom.server.scoreboard.Sidebar

abstract class SimpleBedwarsPlayer(override val miniPlayer: MiniPlayer, override val game: SimpleBedwarsGame):
    BedwarsPlayer {

    protected var playerteam: Team? = null

    protected var kills: Int = 0
    protected var deaths: Int = 0
    protected var brokeBeds: Int = 0

    override val storedInventory: StoredInventory = run {
        val minestomPlayer = miniPlayer.getOriginUnsafe()
        val helmet = minestomPlayer.helmet
        val chestplate = minestomPlayer.chestplate
        val legging = minestomPlayer.leggings
        val boots = minestomPlayer.boots
        val eq = arrayOf(helmet, chestplate, legging, boots)
        val inv = minestomPlayer.inventory.itemStacks
        return@run StoredInventory(
            eq,
            minestomPlayer.displayName,
            minestomPlayer.food, inv,
            minestomPlayer.gameMode,
            minestomPlayer.exp,
            minestomPlayer.level
        )
    }

    @Synchronized
    override fun setTeam(team: Team): Result<Boolean> {
        if (game.gameStateMachine.getCurrentState() == GameState.STARTING) {
            return Result(false, Reason.Failed("开始中无法更换队伍"))
        }
        if (game.gameStateMachine.getCurrentState() == GameState.LOBBY) {
            if (team.addPlayer(this)) {
                if (playerteam != null) {
                    playerteam?.removePlayer(this)
                }
                playerteam = team
                return Result(true, Reason.Success)
            }
        }
        return Result(false, Reason.Failed("Unkonw"))
    }

    override val sidebar: Sidebar
        get() = TODO("Not yet implemented")

    @Synchronized
    fun addKillsCount(count: Int) {
        kills += count
    }

    @Synchronized
    fun addDeathsCount(count: Int) {
        deaths += count
    }

    @Synchronized
    fun addBrokeBedsCount(count: Int) {
        brokeBeds += count
    }

    override fun getKillsCount(): Int {
        return kills
    }

    override fun getDeathsCount(): Int {
        return deaths
    }

    override fun getBedBrokensCount(): Int {
        return brokeBeds
    }

    override fun refreshPlayer() {
        val minestomP = this.miniPlayer.getOrigin() ?: return
        minestomP.exp = 0.0F
        minestomP.closeInventory()
        minestomP.inventory.clear()
        minestomP.gameMode = GameMode.SURVIVAL
    }

}