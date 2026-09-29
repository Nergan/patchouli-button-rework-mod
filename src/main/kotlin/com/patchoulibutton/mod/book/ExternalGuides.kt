package com.patchoulibutton.mod.book

import com.patchoulibutton.mod.PatchouliButtonMod
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.LevelAccessor
import net.neoforged.fml.ModList

/**
 * Гайды, которые не являются книгами Patchouli, но должны попасть в книгу сборки
 * и в зачистку стартовых книг.
 */
object ExternalGuides {
    val CNC_FIELD_GUIDE: ResourceLocation = ResourceLocation.fromNamespaceAndPath("cnc", "field_guide")
    val MORE_CRITTERS_ATLAS: ResourceLocation = ResourceLocation.fromNamespaceAndPath("more_critters", "critter_atlas")

    private class Guide(
        val item: ResourceLocation,
        val modId: String,
        val opener: String,
    )

    private val guides = listOf(
        Guide(CNC_FIELD_GUIDE, "cnc", "net.imasillylittleguy.cnc.procedures.FieldGuideOpenProcedure"),
        Guide(MORE_CRITTERS_ATLAS, "more_critters", "com.morecritters.mod.procedures.CritterAtlasRightclickedProcedure"),
    )

    fun loaded(): List<ResourceLocation> = guides.filter { it.present() }.map { it.item }

    fun isExternalGuide(itemId: ResourceLocation): Boolean = guides.any { it.item == itemId && it.present() }

    fun open(player: ServerPlayer, id: String) {
        val guide = guides.find { it.item.toString() == id && it.present() } ?: return
        try {
            val method = Class.forName(guide.opener).getMethod(
                "execute",
                LevelAccessor::class.java,
                Double::class.javaPrimitiveType,
                Double::class.javaPrimitiveType,
                Double::class.javaPrimitiveType,
                Entity::class.java,
            )
            method.invoke(null, player.level(), player.x, player.y, player.z, player)
        } catch (exception: ReflectiveOperationException) {
            PatchouliButtonMod.LOGGER.warn("Could not open guide {}", id, exception)
        }
    }

    private fun Guide.present(): Boolean = ModList.get().isLoaded(modId)
}
