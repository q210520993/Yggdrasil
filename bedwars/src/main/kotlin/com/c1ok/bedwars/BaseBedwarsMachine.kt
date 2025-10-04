package com.c1ok.bedwars

import com.c1ok.bedwars.simple.SimpleBedwarsGame
import com.c1ok.yggdrasil.GameState
import com.c1ok.yggdrasil.base.BaseGameStateMachine
import java.util.concurrent.atomic.AtomicReference

open class BaseBedwarsMachine(override val game: SimpleBedwarsGame): BaseGameStateMachine(game) {

    private val state_: AtomicReference<GameState> = AtomicReference()

    override fun getGameEndCondition(): Boolean {
        return  GAME_TIME.getTime() > 0 &&
                getCurrentState() == GameState.STARTING &&
                game.getTeams().filter { !it.getIsWipedOut() }.size > 1 &&
                game.getPlayers().isNotEmpty()
    }

    override fun setState(state: GameState) {
        state_.set(state)
    }

    override fun getCurrentState(): GameState {
        return state_.get()
    }

}