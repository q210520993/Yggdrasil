package com.c1ok.bedwars.simple

import com.c1ok.bedwars.BedwarsGame
import com.c1ok.yggdrasil.GameManager
import com.c1ok.yggdrasil.MiniPlayer
import net.minestom.server.instance.Instance
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class BedwarsManager: GameManager<BedwarsGame> {
    private val games = ConcurrentHashMap<UUID, BedwarsGame>()

    override fun getGames(): List<BedwarsGame> {
        return games.values.toList()
    }

    fun addGameToManager(game: BedwarsGame): Boolean {
        if (games.values.contains(game)) {
            return false
        }
        games[game.uuid] = game
        return true
    }

    fun removeGame(game: BedwarsGame) {
        games.remove(game.uuid)
    }

    override fun getInstanceGame(instance: Instance): BedwarsGame? {
        val instances = games.values.map { it.instanceManager.getCurrentInstance() }
        if (!instances.contains(instance)) { return null }
        return games.values.first { it.instanceManager.getCurrentInstance() == instance }
    }

    override fun getGame(uuid: UUID): BedwarsGame? {
        return games[uuid]
    }

    override fun getGameInstance(game: BedwarsGame): Instance {
        return game.instanceManager.getCurrentInstance()
    }

    override fun isGameInstance(instance: Instance): Boolean {
        return games.values.any { it.instanceManager.getCurrentInstance() == instance }
    }

    override fun playerIsInGame(player: MiniPlayer): Boolean {
        return player.game is BedwarsGame
    }

}