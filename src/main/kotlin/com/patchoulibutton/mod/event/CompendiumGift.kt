package com.patchoulibutton.mod.event

import com.patchoulibutton.mod.GuideSettings
import com.patchoulibutton.mod.book.CompendiumBook
import com.patchoulibutton.mod.util.PatchouliGuideItems
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import vazkii.patchouli.common.item.ItemModBook

object CompendiumGift {
    lateinit var dataOf: (Player) -> net.minecraft.nbt.CompoundTag

    fun onLogin(player: ServerPlayer) {
        if (!GuideSettings.giveCompendium()) return
        val data = dataOf(player)
        if (data.getBoolean(CompendiumBook.GIVEN_FLAG)) return
        if (!PatchouliGuideItems.hasCompendium(player)) {
            val stack = ItemModBook.forBook(CompendiumBook.ID)
            if (stack.isEmpty) return
            if (!player.inventory.add(stack)) {
                player.drop(stack, false)
            }
        }
        data.putBoolean(CompendiumBook.GIVEN_FLAG, true)
    }

    fun onClone(original: Player, clone: Player) {
        if (!dataOf(original).getBoolean(CompendiumBook.GIVEN_FLAG)) return
        dataOf(clone).putBoolean(CompendiumBook.GIVEN_FLAG, true)
    }
}
