package com.patchoulibutton.mod

import com.patchoulibutton.mod.config.ClientConfig
import com.patchoulibutton.mod.config.ServerConfig
import com.patchoulibutton.mod.event.CompendiumGift
import com.patchoulibutton.mod.event.ModSetup
import com.patchoulibutton.mod.event.NeoForgeHooks
import com.patchoulibutton.mod.event.StartingBookCleaner
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig

@Mod(PatchouliButton.MOD_ID)
class PatchouliButtonMod(modEventBus: IEventBus, modContainer: ModContainer) {

    init {
        GuideSettings.giveCompendium = { ServerConfig.CONFIG.giveCompendium.get() }
        GuideSettings.clearStartingBooks = { ServerConfig.CONFIG.clearStartingBooks.get() }
        GuideSettings.showBookButton = { ClientConfig.CONFIG.showBookButton.get() }
        GuideSettings.buttonX = { ClientConfig.CONFIG.buttonX.get() }
        GuideSettings.buttonY = { ClientConfig.CONFIG.buttonY.get() }
        GuideSettings.isModLoaded = { ModList.get().isLoaded(it) }
        CompendiumGift.dataOf = { it.persistentData }
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC)
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC)
        ModSetup.init(modEventBus, modContainer)
        NeoForgeHooks.register()
    }
}
