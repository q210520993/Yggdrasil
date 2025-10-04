package com.c1ok.bedwars.simple.block

import com.c1ok.bedwars.Team
import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.utils.Direction

fun placeBed(team: Team) {
    val instance = team.currentGame.instanceManager.getCurrentInstance()
    val direction = team.bedDirection
    val point = team.bedPoint
    val block = team.bedBlock.withHandler(BedHandler(team))
    placeBed(block, direction, point, instance)
}

fun placeBed(bed: Block, direction: Direction, point: Point, instance: Instance, head: BlockHandler? = null, foot: BlockHandler? = null) {
    val bedBlock = bed.withProperty("facing", direction.name.lowercase())
    val footBlock: Block = bedBlock.withProperty("part", "foot").apply {
        if (foot == null) return@apply
        withHandler(foot)
    }
    val headBlock: Block = bedBlock.withProperty("part", "head").apply {
        if (head == null) return@apply
        withHandler(head)
    }
    val bedHeadPosition = point.add(
        direction.normalX().toDouble(),
        direction.normalY().toDouble(),
        direction.normalZ().toDouble()
    )
    instance.setBlock(point, footBlock)
    instance.setBlock(bedHeadPosition, headBlock)
}
