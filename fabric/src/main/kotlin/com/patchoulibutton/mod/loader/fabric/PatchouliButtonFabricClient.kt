package com.patchoulibutton.mod.loader.fabric

import com.patchoulibutton.mod.GuideSettings
import com.patchoulibutton.mod.client.CompendiumReturn
import com.patchoulibutton.mod.client.InventoryBookButton
import com.patchoulibutton.mod.network.OpenExternalGuidePayload
import net.fabricmc.api.ClientModInitializer
import com.patchoulibutton.mod.loader.fabric.mixin.ScreenWidgetInvoker
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents

class PatchouliButtonFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        GuideSettings.sendOpenGuide = { ClientPlayNetworking.send(OpenExternalGuidePayload(it)) }
        ScreenEvents.BEFORE_INIT.register { _, screen, _, _ ->
            CompendiumReturn.onOpening(screen)
        }
        ScreenEvents.AFTER_INIT.register { _, screen, _, _ ->
            val button = CompendiumReturn.backButton(screen)
            if (button != null) {
                (screen as ScreenWidgetInvoker).`patchouli$addWidget`(button)
            }
            ScreenEvents.afterRender(screen).register { current, graphics, mouseX, mouseY, _ ->
                InventoryBookButton.render(current, graphics, mouseX, mouseY)
            }
            ScreenMouseEvents.allowMouseClick(screen).register { current, mouseX, mouseY, button ->
                !InventoryBookButton.click(current, mouseX, mouseY, button)
            }
        }
    }
}
