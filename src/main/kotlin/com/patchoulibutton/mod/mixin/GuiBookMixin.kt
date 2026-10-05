package com.patchoulibutton.mod.mixin

import com.patchoulibutton.mod.client.CompendiumReturn
import com.patchoulibutton.mod.client.ownsCompendium
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import vazkii.patchouli.client.book.gui.GuiBook

@Mixin(value = [GuiBook::class], remap = false)
abstract class GuiBookMixin {
    @Inject(method = ["canSeeBackButton"], at = [At("RETURN")], cancellable = true, remap = false)
    private fun showCompendiumReturn(callback: CallbackInfoReturnable<Boolean>) {
        if (callback.returnValue) return
        val book = (this as GuiBook).book
        if (CompendiumReturn.pending && !book.ownsCompendium()) {
            callback.returnValue = true
        }
    }

    @Inject(method = ["back"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun returnToCompendium(playSound: Boolean, callback: CallbackInfo) {
        val book = (this as GuiBook).book
        if (!CompendiumReturn.pending || book.ownsCompendium()) return
        val contents = book.contents ?: return
        if (contents.guiStack.isNotEmpty()) return
        CompendiumReturn.openCompendium()
        callback.cancel()
    }
}
