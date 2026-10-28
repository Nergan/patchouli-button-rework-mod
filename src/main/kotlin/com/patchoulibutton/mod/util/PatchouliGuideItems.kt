package com.patchoulibutton.mod.util

import com.patchoulibutton.mod.book.CompendiumBook
import com.patchoulibutton.mod.book.ExternalGuides
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import vazkii.patchouli.common.item.PatchouliDataComponents

object PatchouliGuideItems {
    private val guideBookId: ResourceLocation = ResourceLocation.fromNamespaceAndPath("patchouli", "guide_book")

    fun isGuideBook(stack: ItemStack): Boolean {
        if (stack.isEmpty) return false
        val id = BuiltInRegistries.ITEM.getKey(stack.item)
        return id == guideBookId || ExternalGuides.isExternalGuide(id)
    }

    fun hasCompendium(player: Player): Boolean {
        val inventory: Inventory = player.inventory
        for (slot in 0 until inventory.containerSize) {
            if (isCompendium(inventory.getItem(slot))) return true
        }
        return isCompendium(player.containerMenu.carried)
    }

    fun bookKey(stack: ItemStack): String? {
        if (stack.isEmpty) return null
        val itemId = BuiltInRegistries.ITEM.getKey(stack.item)
        if (ExternalGuides.isExternalGuide(itemId)) return itemId.toString()
        if (itemId != guideBookId) return null
        val id = bookId(stack) ?: return guideBookId.toString()
        if (id == CompendiumBook.ID) return null
        return id.toString()
    }

    private fun isCompendium(stack: ItemStack): Boolean = bookId(stack) == CompendiumBook.ID

    fun counts(player: Player): Map<String, Int> {
        val counts = LinkedHashMap<String, Int>()
        forEachGuide(player) { key, stack ->
            counts[key] = (counts[key] ?: 0) + stack.count
        }
        return counts
    }

    fun removeExcess(player: Player, excess: Map<String, Int>) {
        if (excess.isEmpty()) return
        val left = HashMap(excess)
        forEachGuide(player) { key, stack ->
            val remaining = left[key] ?: return@forEachGuide
            if (remaining <= 0) return@forEachGuide
            val take = minOf(remaining, stack.count)
            stack.shrink(take)
            left[key] = remaining - take
        }
        player.containerMenu.broadcastChanges()
    }

    private fun forEachGuide(player: Player, action: (String, ItemStack) -> Unit) {
        val inventory: Inventory = player.inventory
        for (slot in 0 until inventory.containerSize) {
            val stack = inventory.getItem(slot)
            val key = bookKey(stack) ?: continue
            action(key, stack)
        }
        val carried = player.containerMenu.carried
        val carriedKey = bookKey(carried)
        if (carriedKey != null) action(carriedKey, carried)
    }

    private fun bookId(stack: ItemStack): ResourceLocation? {
        if (stack.isEmpty || BuiltInRegistries.ITEM.getKey(stack.item) != guideBookId) return null
        return stack.get(PatchouliDataComponents.BOOK)
    }
}
