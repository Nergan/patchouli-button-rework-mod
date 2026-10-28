package com.patchoulibutton.mod.mixin

import com.patchoulibutton.mod.book.CompendiumBook
import com.patchoulibutton.mod.client.CompendiumIcons
import com.patchoulibutton.mod.client.ownsCompendium
import net.minecraft.client.resources.language.I18n
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.Redirect
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import vazkii.patchouli.client.book.gui.GuiBook
import vazkii.patchouli.client.book.gui.GuiBookLanding

// Процессор аннотаций Kotlin не видит: имена Fabric для init, drawForegroundElements и I18n.get
// прописаны вручную в patchKotlinRefmap (fabric/build.gradle.kts).
@Mixin(value = [GuiBookLanding::class], remap = false)
abstract class GuiBookLandingMixin {
    private fun pages(): GuiBookAccessor = this as GuiBookAccessor

    @Inject(method = ["init"], at = [At("TAIL")], remap = true)
    private fun installCompendiumIcons(callback: CallbackInfo) {
        val book = (this as GuiBook).book
        if (!book.ownsCompendium()) return
        pages().patchouliButtonSetMaxSpreads(CompendiumIcons.pageCount())
        if (pages().patchouliButtonSpread() >= pages().patchouliButtonMaxSpreads()) {
            pages().patchouliButtonSetSpread(pages().patchouliButtonMaxSpreads() - 1)
        }
        CompendiumIcons.install(this as GuiBook, pages().patchouliButtonSpread())
    }

    @Inject(method = ["onPageChanged"], at = [At("HEAD")], cancellable = true)
    private fun turnCompendiumPage(callback: CallbackInfo) {
        val book = (this as GuiBook).book
        if (!book.ownsCompendium()) return
        pages().patchouliButtonSetMaxSpreads(CompendiumIcons.pageCount())
        CompendiumIcons.install(this as GuiBook, pages().patchouliButtonSpread())
        callback.cancel()
    }

    @Redirect(
        method = ["drawForegroundElements"],
        at = At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/language/I18n;get(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
        ),
        remap = true,
    )
    private fun compendiumHeader(key: String, args: Array<Any>?): String {
        val book = (this as GuiBook).book
        if (book.id == CompendiumBook.ID && key == "patchouli.gui.lexicon.categories") {
            return I18n.get("patchoulibutton.book.guides")
        }
        if (args == null || args.isEmpty()) return I18n.get(key)
        return I18n.get(key, *args)
    }
}
