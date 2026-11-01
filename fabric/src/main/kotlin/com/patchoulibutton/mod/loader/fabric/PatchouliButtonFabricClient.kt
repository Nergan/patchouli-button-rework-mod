package com.patchoulibutton.mod.loader.fabric

import com.patchoulibutton.mod.GuideSettings
import com.patchoulibutton.mod.client.CompendiumReturn
import com.patchoulibutton.mod.client.InventoryBookButton
import com.patchoulibutton.mod.network.OpenExternalGuidePayload
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents

class PatchouliButtonFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        GuideSettings.sendOpenGuide = { ClientPlayNetworking.send(OpenExternalGuidePayload(it)) }
        ClientTickEvents.START_CLIENT_TICK.register { client ->
            if (client.screen == null) CompendiumReturn.onNoScreen()
        }
        ScreenEvents.BEFORE_INIT.register { _, screen, _, _ ->
            CompendiumReturn.onOpening(screen)
        }
        ScreenEvents.AFTER_INIT.register { _, screen, _, _ ->
            CompendiumReturn.attachBackButton(screen)
            ScreenEvents.afterRender(screen).register { current, graphics, mouseX, mouseY, partialTick ->
                CompendiumReturn.renderBackButton(current, graphics, mouseX, mouseY, partialTick)
                InventoryBookButton.render(current, graphics, mouseX, mouseY)
            }
            ScreenMouseEvents.allowMouseClick(screen).register { current, mouseX, mouseY, button ->
                !CompendiumReturn.clickBackButton(current, mouseX, mouseY, button) &&
                    !InventoryBookButton.click(current, mouseX, mouseY, button)
            }
        }
    }
}
