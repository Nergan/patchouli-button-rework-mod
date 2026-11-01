package com.patchoulibutton.mod.client

import com.patchoulibutton.mod.GuideSettings
import com.patchoulibutton.mod.network.OpenExternalGuidePayload
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.network.PacketDistributor

object ClientModEvents {
    fun init(@Suppress("UNUSED_PARAMETER") modBus: IEventBus, modContainer: ModContainer) {
        GuideSettings.sendOpenGuide = { id ->
            PacketDistributor.sendToServer(OpenExternalGuidePayload(id))
        }
        modContainer.registerExtensionPoint(
            IConfigScreenFactory::class.java,
            IConfigScreenFactory { container, parent: Screen -> ConfigurationScreen(container, parent) },
        )
        NeoForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onRender(event: ScreenEvent.Render.Post) {
        CompendiumReturn.renderBackButton(event.screen, event.guiGraphics, event.mouseX, event.mouseY, event.partialTick)
        InventoryBookButton.render(event.screen, event.guiGraphics, event.mouseX, event.mouseY)
    }

    @SubscribeEvent
    fun onClick(event: ScreenEvent.MouseButtonPressed.Pre) {
        val screen = event.screen
        if (CompendiumReturn.clickBackButton(screen, event.mouseX, event.mouseY, event.button) ||
            InventoryBookButton.click(screen, event.mouseX, event.mouseY, event.button)
        ) {
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onScreenOpening(event: ScreenEvent.Opening) {
        CompendiumReturn.onOpening(event.newScreen)
    }

    @SubscribeEvent
    fun onScreenInit(event: ScreenEvent.Init.Post) {
        CompendiumReturn.attachBackButton(event.screen)
    }

    @SubscribeEvent
    fun onClientTick(@Suppress("UNUSED_PARAMETER") event: ClientTickEvent.Pre) {
        if (Minecraft.getInstance().screen == null) CompendiumReturn.onNoScreen()
    }
}
