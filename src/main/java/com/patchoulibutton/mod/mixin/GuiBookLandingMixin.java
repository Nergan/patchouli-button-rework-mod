package com.patchoulibutton.mod.mixin;

import com.patchoulibutton.mod.book.CompendiumBook;
import com.patchoulibutton.mod.client.CompendiumIcons;
import com.patchoulibutton.mod.client.CompendiumIconsKt;
import net.minecraft.client.resources.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookLanding;
import vazkii.patchouli.common.book.Book;

@Mixin(value = GuiBookLanding.class, remap = false)
public abstract class GuiBookLandingMixin {
    private GuiBookAccessor pages() {
        return (GuiBookAccessor) (Object) this;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void installCompendiumIcons(CallbackInfo callback) {
        Book book = ((GuiBook) (Object) this).book;
        if (!CompendiumIconsKt.ownsCompendium(book)) {
            return;
        }
        pages().patchouliButtonSetMaxSpreads(CompendiumIcons.INSTANCE.pageCount());
        if (pages().patchouliButtonSpread() >= pages().patchouliButtonMaxSpreads()) {
            pages().patchouliButtonSetSpread(pages().patchouliButtonMaxSpreads() - 1);
        }
        CompendiumIcons.INSTANCE.install((GuiBook) (Object) this, pages().patchouliButtonSpread());
    }

    @Inject(method = "onPageChanged", at = @At("HEAD"), cancellable = true)
    private void turnCompendiumPage(CallbackInfo callback) {
        Book book = ((GuiBook) (Object) this).book;
        if (!CompendiumIconsKt.ownsCompendium(book)) {
            return;
        }
        pages().patchouliButtonSetMaxSpreads(CompendiumIcons.INSTANCE.pageCount());
        CompendiumIcons.INSTANCE.install((GuiBook) (Object) this, pages().patchouliButtonSpread());
        callback.cancel();
    }

    @Redirect(
            method = "drawForegroundElements",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/language/I18n;get(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"
            ),
            remap = true
    )
    private String compendiumHeader(String key, Object[] args) {
        Book book = ((GuiBook) (Object) this).book;
        if (book.id.equals(CompendiumBook.INSTANCE.getID()) && "patchouli.gui.lexicon.categories".equals(key)) {
            return I18n.get("patchoulibutton.book.guides");
        }
        if (args == null || args.length == 0) {
            return I18n.get(key);
        }
        return I18n.get(key, args);
    }
}
