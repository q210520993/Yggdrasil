package com.c1ok.bedwars

import com.c1ok.bedwars.simple.SimpleBedwarsGame
import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.block.Block
import net.minestom.server.inventory.Inventory
import net.minestom.server.item.ItemStack
import net.minestom.server.utils.Direction
import java.util.*

interface Team {

    // 队伍唯一标识符
    val id: UUID

    // 队伍名称（帽子绿，姨妈红...）
    val name: String

    // 队伍展示名称在·
    val displayName: Component

    // 最小玩家数
    val minPlayers: Int get() = 1

    // 最大玩家数（推荐设置默认值）
    val maxPlayers: Int get() = 4

    // 床的位置
    val bedPoint: Point

    //床
    val bedBlock: Block

    // 床的方向
    val bedDirection: Direction

    // 当前游戏实例
    val currentGame: SimpleBedwarsGame

    val respawnPoint: Pos

    val priority: Int

    val players: Set<BedwarsPlayer>

    // 床是否被摧毁（改名更明确）- 改进
    var isBedDestroyed: Boolean

    // 队伍是否被团灭
    var isWipedOut: Boolean

    fun createBed()
    fun destroyBed()

    // 队伍共享库存（更名明确用途）- 改进
    val sharedInventory: Inventory

    // 玩家管理方法
    fun containsPlayer(player: BedwarsPlayer): Boolean
    fun addPlayer(player: BedwarsPlayer): Boolean
    fun removePlayer(player: BedwarsPlayer): Boolean

    // 选择界面的物品
    fun getTeamItemStack(): ItemStack

    fun onGameStop()
}