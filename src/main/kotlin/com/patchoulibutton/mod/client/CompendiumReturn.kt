package com.patchoulibutton.mod.client

import com.patchoulibutton.mod.book.CompendiumBook
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import vazkii.patchouli.api.PatchouliAPI
import vazkii.patchouli.client.book.gui.GuiBook

/**
 * Книга, открытая из сборки, должна уметь вернуться в неё.
 * [pending] держится, пока игрок листает гайд. [external] — гайд не из Patchouli.
 */
object CompendiumReturn {
    var pending: Boolean = false
    var external: Boolean = false

    fun openPatchouli(id: ResourceLocation) {
        pending = true
        external = false
        PatchouliAPI.get().openBookGUI(id)
    }

    fun markExternal() {
        pending = true
        external = true
    }

    fun openCompendium() {
        pending = false
        external = false
        val minecraft = Minecraft.getInstance()
        minecraft.player?.closeContainer()
        PatchouliAPI.get().openBookGUI(CompendiumBook.ID)
    }

    fun onOpening(screen: Screen?) {
        if (screen is GuiBook) {
            external = false
            if (screen.book.ownsCompendium()) pending = false
            return
        }
        if (screen == null || !external) {
            pending = false
            external = false
        }
    }

    fun backButton(screen: Screen): Button? {
        if (!external || screen is GuiBook) return null
        return Button.builder(Component.translatable("patchoulibutton.screen.back")) {
            openCompendium()
        }.bounds(4, 4, 140, 20).build()
    }
}
