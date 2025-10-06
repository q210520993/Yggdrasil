package com.c1ok.bedwars.simple

import com.c1ok.bedwars.BedwarsGame
import com.c1ok.bedwars.BedwarsPlayer
import com.c1ok.bedwars.Team
import com.c1ok.bedwars.simple.block.placeBed
import com.c1ok.yggdrasil.GameState
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.instance.block.Block
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.utils.Direction
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet

open class SimpleTeam(
    // MiniMessage支持
    final override val name: String,
    final override val displayName: Component,
    // 这个point bed均指床头
    override val bedPoint: Point,
    override val bedBlock: Block,
    override val bedDirection: Direction,
    override val currentGame: BedwarsGame,
    override val respawnPoint: Pos,
    override val id: UUID = UUID.randomUUID(),
    override val minPlayers: Int = 1,
    override val maxPlayers: Int = 4,
    override val priority: Int
): Team {
    override val players: MutableSet<BedwarsPlayer> = CopyOnWriteArraySet()

    protected var isBedDestroyed: Boolean = false

    override fun getIsBedDestroy(): Boolean {
        return isBedDestroyed
    }

    override fun setBedDestroy(bed: Boolean) {
        isBedDestroyed = bed
    }

    override fun getIsWipedOut(): Boolean {
        return players.all { it.spectator }
    }

    override fun createBed() {
        placeBed(this)
    }

    override fun destroyBed() {
        setBedDestroy(true)
        currentGame.getBedwarsPlayers().forEach {
            val minestomPlayer = it.miniPlayer.getOrigin() ?: return@forEach
            val component = displayName.append(Component.text("的床被破坏"))
            minestomPlayer.showTitle(Title.title(component, Component.empty(), Title.DEFAULT_TIMES))
        }
    }

    override val sharedInventory: Inventory = Inventory(InventoryType.CHEST_3_ROW, displayName)

    override fun containsPlayer(player: BedwarsPlayer): Boolean {
        return players.contains(player)
    }

    override fun addPlayer(player: BedwarsPlayer): Boolean {
        if (currentGame.gameStateMachine.getCurrentState() != GameState.LOBBY) return false
        if (players.size > maxPlayers) return false
        if (player.getTeam() != null) {
            player.getTeam()?.removePlayer(player)
        }
        player.setTeam(this)
        player.miniPlayer.getOrigin()?.let {
            setPlayerDisplay(it)
        }
        return players.add(player)
    }

    private fun setPlayerDisplay(player: Player) {
        player.displayName = displayName.append(Component.text(player.username))
    }

    override fun removePlayer(player: BedwarsPlayer): Boolean {
        if (currentGame.gameStateMachine.getCurrentState() == GameState.LOBBY) {
            return players.remove(player)
        }
        if (currentGame.gameStateMachine.getCurrentState() == GameState.STARTING) {
            player.spectator = true
            player.miniPlayer.getOrigin()?.gameMode = GameMode.SPECTATOR
            return players.remove(player)
        }
        return false
    }

    override fun getTeamItemStack(): ItemStack {
        return ItemStack.builder(Material.AIR).build()
    }

    override fun onGameStop() {
        players.clear()
    }

    fun getPlayers(isAlive: Boolean): List<BedwarsPlayer> {
        if (isAlive) {
            return players.filter { !it.spectator }.toList()
        }
        return players.toList()
    }

    override fun toString(): String {
        return "" +
                "ID: ${name},isWiped: ${getIsWipedOut()}, " +
                "alivePlayers: ${getPlayers(isAlive = true)}, " +
                "allPlayers: ${getPlayers(isAlive = false).size}" +
                "bedBreaked: $isBedDestroyed"
    }

}