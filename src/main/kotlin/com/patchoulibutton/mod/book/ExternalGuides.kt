package com.patchoulibutton.mod.book

import com.patchoulibutton.mod.GuideSettings
import com.patchoulibutton.mod.PatchouliButton
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelAccessor
/**
 * Гайды, которые не являются книгами Patchouli, но должны попасть в книгу сборки
 * и в зачистку стартовых книг.
 */
object ExternalGuides {
    val CNC_FIELD_GUIDE: ResourceLocation = ResourceLocation.fromNamespaceAndPath("cnc", "field_guide")
    val MORE_CRITTERS_ATLAS: ResourceLocation = ResourceLocation.fromNamespaceAndPath("more_critters", "critter_atlas")
    val ANIMAL_DICTIONARY: ResourceLocation = ResourceLocation.fromNamespaceAndPath("alexsmobs", "animal_dictionary")

    /**
     * [procedure] — процедура MCreator, которая открывает гайд с сервера.
     * [clientProxy] — класс мода со статическим `PROXY`, чей `openBookGUI(ItemStack)` открывает экран на клиенте.
     */
    private class Guide(
        val item: ResourceLocation,
        val modId: String,
        val procedure: String? = null,
        val clientProxy: String? = null,
    )

    private val guides = listOf(
        Guide(CNC_FIELD_GUIDE, "cnc", procedure = "net.imasillylittleguy.cnc.procedures.FieldGuideOpenProcedure"),
        Guide(MORE_CRITTERS_ATLAS, "more_critters", procedure = "com.morecritters.mod.procedures.CritterAtlasRightclickedProcedure"),
        Guide(ANIMAL_DICTIONARY, "alexsmobs", clientProxy = "com.github.alexthe666.alexsmobs.AlexsMobs"),
    )

    fun loaded(): List<ResourceLocation> = guides.filter { it.present() }.map { it.item }

    fun isExternalGuide(itemId: ResourceLocation): Boolean = guides.any { it.item == itemId && it.present() }

    fun opensOnClient(itemId: ResourceLocation): Boolean = guides.any { it.item == itemId && it.clientProxy != null && it.present() }

    fun openOnClient(itemId: ResourceLocation) {
        val guide = guides.find { it.item == itemId && it.present() } ?: return
        val owner = guide.clientProxy ?: return
        try {
            val proxy = Class.forName(owner).getField("PROXY").get(null)
            proxy.javaClass.getMethod("openBookGUI", ItemStack::class.java)
                .invoke(proxy, ItemStack(BuiltInRegistries.ITEM.get(itemId)))
        } catch (exception: ReflectiveOperationException) {
            PatchouliButton.LOGGER.warn("Could not open guide {}", itemId, exception)
        }
    }

    fun open(player: ServerPlayer, id: String) {
        val guide = guides.find { it.item.toString() == id && it.present() } ?: return
        val procedure = guide.procedure ?: return
        try {
            val method = Class.forName(procedure).getMethod(
                "execute",
                LevelAccessor::class.java,
                Double::class.javaPrimitiveType,
                Double::class.javaPrimitiveType,
                Double::class.javaPrimitiveType,
                Entity::class.java,
            )
            method.invoke(null, player.level(), player.x, player.y, player.z, player)
        } catch (exception: ReflectiveOperationException) {
            PatchouliButton.LOGGER.warn("Could not open guide {}", id, exception)
        }
    }

    private fun Guide.present(): Boolean = GuideSettings.isModLoaded(modId)
}
