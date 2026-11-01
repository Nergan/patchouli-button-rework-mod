package com.patchoulibutton.mod.client

import com.patchoulibutton.mod.book.CompendiumBook
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
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

    // The button is drawn over the screen rather than added to it: the Citadel book of
    // Alex's Mobs calls clearWidgets() on its first frame and on every page turn.
    private var overlayScreen: Screen? = null
    private var overlay: Button? = null

    fun attachBackButton(screen: Screen) {
        val button = backButton(screen)
        overlayScreen = if (button != null) screen else null
        overlay = button
    }

    fun renderBackButton(screen: Screen, graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (screen !== overlayScreen) return
        overlay?.render(graphics, mouseX, mouseY, partialTick)
    }

    fun clickBackButton(screen: Screen, mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (screen !== overlayScreen) return false
        return overlay?.mouseClicked(mouseX, mouseY, button) == true
    }

    /**
     * Called on a client tick with no screen open. Closing a guide with Esc sets the screen to null,
     * and neither loader reports that to [onOpening], so the next inventory or chat screen
     * would still get the back button.
     */
    fun onNoScreen() {
        if (!pending && !external && overlay == null) return
        pending = false
        external = false
        overlayScreen = null
        overlay = null
    }
}
