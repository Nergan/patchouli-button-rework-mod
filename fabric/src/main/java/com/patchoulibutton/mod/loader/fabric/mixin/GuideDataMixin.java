package com.patchoulibutton.mod.loader.fabric.mixin;

import com.patchoulibutton.mod.loader.fabric.GuideData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class GuideDataMixin implements GuideData {
    @Unique
    private final CompoundTag patchoulibutton$data = new CompoundTag();

    @Override
    public CompoundTag patchouliButtonData() {
        return this.patchoulibutton$data;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void patchouli$save(CompoundTag tag, CallbackInfo callback) {
        if (!((Object) this instanceof Player)) return;
        tag.put("patchoulibutton", this.patchoulibutton$data.copy());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void patchouli$load(CompoundTag tag, CallbackInfo callback) {
        if (!((Object) this instanceof Player)) return;
        if (tag.contains("patchoulibutton")) {
            CompoundTag saved = tag.getCompound("patchoulibutton");
            for (String key : saved.getAllKeys()) {
                this.patchoulibutton$data.put(key, saved.get(key));
            }
        }
    }
}
