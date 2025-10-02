package com.c1ok.bedwars.simple

import com.c1ok.yggdrasil.MiniGame
import com.c1ok.yggdrasil.MiniPlayer
import java.util.*

class SimpleMiniPlayer(override val uuid: UUID): MiniPlayer {
    override var game: MiniGame? = null
}