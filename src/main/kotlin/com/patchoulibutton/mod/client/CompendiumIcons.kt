package com.patchoulibutton.mod.client

import com.patchoulibutton.mod.GuideSettings
import com.patchoulibutton.mod.book.CompendiumBook
import com.patchoulibutton.mod.book.ExternalGuides
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import vazkii.patchouli.client.book.BookIcon
import vazkii.patchouli.client.book.gui.GuiBook
import vazkii.patchouli.client.book.gui.button.GuiButtonCategory
import vazkii.patchouli.common.book.Book
import vazkii.patchouli.common.book.BookRegistry
import kotlin.math.max

object CompendiumIcons {
    const val PER_PAGE = 16

    fun pageCount(): Int = max(1, (entries().size + PER_PAGE - 1) / PER_PAGE)

    fun install(screen: GuiBook, spread: Int) {
        screen.removeDrawablesIf { it is GuiButtonCategory }
        val guides = entries()
        if (guides.isEmpty()) return
        val page = spread.coerceIn(0, pageCount() - 1)
        val from = page * PER_PAGE
        guides.drop(from).take(PER_PAGE).forEachIndexed { index, guide ->
            val x = GuiBook.RIGHT_PAGE_X + 10 + (index % 4) * 24
            val y = GuiBook.TOP_PADDING + 25 + (index / 4) * 24
            screen.addRenderableWidget(
                GuiButtonCategory(screen, x, y, guide.icon, guide.name) { _ -> guide.open() },
            )
        }
    }

    private fun entries(): List<GuideIcon> {
        val minecraft = Minecraft.getInstance()
        val registries = minecraft.level?.registryAccess() ?: return emptyList()
        val guides = ArrayList<GuideIcon>()
        BookRegistry.INSTANCE.books.values
            .filter { it.id != CompendiumBook.ID }
            .sortedBy { it.id.toString() }
            .forEach { book ->
                guides += GuideIcon(Component.translatable(book.name), book.icon) {
                    CompendiumReturn.openPatchouli(book.id)
                }
            }
        ExternalGuides.loaded().forEach { itemId ->
            val icon = BookIcon.from(itemId.toString(), registries)
            val name = ItemStack(BuiltInRegistries.ITEM.get(itemId)).hoverName
            guides += GuideIcon(name, icon) {
                CompendiumReturn.markExternal()
                GuideSettings.sendOpenGuide(itemId.toString())
            }
        }
        return guides
    }

    private class GuideIcon(
        val name: Component,
        val icon: BookIcon,
        val open: () -> Unit,
    )
}

fun Book.ownsCompendium(): Boolean = id == CompendiumBook.ID
