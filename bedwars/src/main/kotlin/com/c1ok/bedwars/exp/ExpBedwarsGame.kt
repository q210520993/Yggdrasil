package com.c1ok.bedwars.exp

import com.c1ok.bedwars.BaseBedwarsMachine
import com.c1ok.bedwars.BedwarsPlayer
import com.c1ok.bedwars.Team
import com.c1ok.bedwars.instance.SimpleInstance
import com.c1ok.bedwars.simple.SimpleBedwarsGame
import com.c1ok.yggdrasil.GameStateMachine
import com.c1ok.yggdrasil.InstanceManager
import com.c1ok.yggdrasil.MiniPlayer
import com.c1ok.yggdrasil.base.Bound
import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.entity.EntityDeathEvent
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerBlockPlaceEvent
import net.minestom.server.event.player.PlayerDeathEvent
import net.minestom.server.timer.Task
import java.util.*

class ExpBedwarsGame(
    waitingPos: Pos,
    override val minPlayers: Int,
    override val maxPlayers: Int,
    override val displayName: Component,
    override val instanceManager: InstanceManager,
    vararg bounds: Bound
): SimpleBedwarsGame(waitingPos) {

    override val uuid: UUID = UUID.randomUUID()

    override val bedwarsPlayerCreator: BedwarsPlayerCreator = BedwarsPlayerCreator { _, mp ->
        return@BedwarsPlayerCreator ExpPlayer(mp, this)
    }

    init {
        addBound(*bounds)
    }

    override val gameStateMachine: GameStateMachine by lazy {
        BaseBedwarsMachine(this)
    }

    override fun clearTeam() {
        teams.clear()
    }

}