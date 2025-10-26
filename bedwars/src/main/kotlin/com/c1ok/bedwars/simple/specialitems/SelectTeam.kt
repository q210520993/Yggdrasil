package com.c1ok.bedwars.simple.specialitems

import com.c1ok.bedwars.BedwarsGame
import com.c1ok.bedwars.Clickable
import com.c1ok.bedwars.utils.getBedwarsPlayer
import com.c1ok.yggdrasil.MiniPlayer.Companion.getOrigin
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window

class SelectTeam(val game: BedwarsGame): Clickable {

    override val tagValue: String = "selectTeam"
    private val border = SimpleItem(ItemBuilder(Material.BLACK_STAINED_GLASS_PANE))

    val items = game.getTeams().map {
        SimpleItem(it.getTeamItemStack()) { event ->
            event.player.getBedwarsPlayer()?.let { gamePlayer->
                val result = it.addPlayer(gamePlayer)
                if (!result.success) {
                    gamePlayer.miniPlayer.getOrigin()?.sendMessage("加入队伍失败，原因为: ${result.reason}")
                }
            }
        }
    }

    val teamGui: Gui = PagedGui.items()
        .setStructure(
            "# # # # # # # # #",
            "# . . . . . . . #",
            "# # # # # # # # #")
        .addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
        .addIngredient('#', border)
        .setContent(items)
        .build()

    val window = Window.single()
        .setTitle("InvUI")
        .setGui(teamGui)


    override fun onClick(player: Player) {
        window.open(player)
    }

}