package com.patchoulibutton.mod.client

import com.patchoulibutton.mod.GuideSettings
import com.patchoulibutton.mod.book.CompendiumBook
import com.patchoulibutton.mod.mixin.ContainerScreenAccessor
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import vazkii.patchouli.api.PatchouliAPI
import vazkii.patchouli.common.item.ItemModBook

object InventoryBookButton {
    private const val SIZE = 18

    fun render(screen: Screen, graphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        if (!GuideSettings.showBookButton()) return
        val inventory = screen as? InventoryScreen ?: return
        val accessor = inventory as ContainerScreenAccessor
        val x = accessor.getScreenLeft() + GuideSettings.buttonX()
        val y = accessor.getScreenTop() + GuideSettings.buttonY()
        val hovered = hit(mouseX, mouseY, x, y)
        if (hovered) {
            graphics.fill(x, y, x + SIZE, y + SIZE, 0x80FFFFFF.toInt())
        }
        graphics.renderItem(bookStack(), x + 1, y + 1)
        if (hovered) {
            graphics.renderTooltip(
                Minecraft.getInstance().font,
                Component.translatable("patchoulibutton.screen.button"),
                mouseX,
                mouseY,
            )
        }
    }

    fun click(screen: Screen, mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!GuideSettings.showBookButton()) return false
        val inventory = screen as? InventoryScreen ?: return false
        if (button != 0) return false
        val accessor = inventory as ContainerScreenAccessor
        val x = accessor.getScreenLeft() + GuideSettings.buttonX()
        val y = accessor.getScreenTop() + GuideSettings.buttonY()
        if (!hit(mouseX.toInt(), mouseY.toInt(), x, y)) return false
        PatchouliAPI.get().openBookGUI(CompendiumBook.ID)
        return true
    }

    private fun bookStack(): ItemStack {
        val stack = ItemModBook.forBook(CompendiumBook.ID)
        return if (stack.isEmpty) net.minecraft.world.item.Items.BOOK.defaultInstance else stack
    }

    private fun hit(mouseX: Int, mouseY: Int, x: Int, y: Int): Boolean {
        return mouseX >= x && mouseX < x + SIZE && mouseY >= y && mouseY < y + SIZE
    }
}
