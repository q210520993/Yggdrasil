package com.c1ok.yggdrasil.base

import com.c1ok.yggdrasil.MiniGame
import com.c1ok.yggdrasil.MiniPlayer
import net.minestom.server.instance.Instance
import java.util.*

interface GameManager<T: MiniGame> {

    fun getGame(uuid: UUID): T?

    fun playerIsInGame(player: MiniPlayer): Boolean

    fun isGameInstance(instance: Instance): Boolean

    fun getInstanceGame(instance: Instance): T?

    fun getGameInstance(game: T): Instance

    fun getGames(): List<T>

}