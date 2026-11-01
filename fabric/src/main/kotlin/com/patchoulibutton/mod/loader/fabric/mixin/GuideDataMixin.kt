package com.patchoulibutton.mod.loader.fabric.mixin

import com.patchoulibutton.mod.loader.fabric.GuideData
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(LivingEntity::class)
abstract class GuideDataMixin : GuideData {
    @Unique
    private val `patchoulibutton$data` = CompoundTag()

    override fun patchouliButtonData(): CompoundTag = `patchoulibutton$data`

    @Inject(method = ["addAdditionalSaveData"], at = [At("RETURN")])
    private fun `patchouli$save`(tag: CompoundTag, callback: CallbackInfo) {
        if ((this as Any) !is Player) return
        tag.put("patchoulibutton", `patchoulibutton$data`.copy())
    }

    @Inject(method = ["readAdditionalSaveData"], at = [At("RETURN")])
    private fun `patchouli$load`(tag: CompoundTag, callback: CallbackInfo) {
        if ((this as Any) !is Player) return
        if (!tag.contains("patchoulibutton")) return
        val saved = tag.getCompound("patchoulibutton")
        for (key in saved.allKeys) {
            saved.get(key)?.let { `patchoulibutton$data`.put(key, it) }
        }
    }
}
