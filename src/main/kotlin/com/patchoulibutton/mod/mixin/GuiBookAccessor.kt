package com.patchoulibutton.mod.mixin

import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor
import vazkii.patchouli.client.book.gui.GuiBook

@Mixin(value = [GuiBook::class], remap = false)
interface GuiBookAccessor {
    @Accessor("spread", remap = false)
    fun patchouliButtonSpread(): Int

    @Accessor("spread", remap = false)
    fun patchouliButtonSetSpread(value: Int)

    @Accessor("maxSpreads", remap = false)
    fun patchouliButtonMaxSpreads(): Int

    @Accessor("maxSpreads", remap = false)
    fun patchouliButtonSetMaxSpreads(value: Int)
}
