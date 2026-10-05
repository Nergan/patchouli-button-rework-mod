package com.patchoulibutton.mod.loader.fabric

import com.patchoulibutton.mod.loader.fabric.GuideData
import com.patchoulibutton.mod.GuideSettings
import com.patchoulibutton.mod.PatchouliButton
import com.patchoulibutton.mod.book.CompendiumBook
import com.patchoulibutton.mod.event.CompendiumGift
import com.patchoulibutton.mod.event.StartingBookCleaner
import com.patchoulibutton.mod.network.OpenExternalGuidePayload
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.world.item.CreativeModeTabs
import vazkii.patchouli.common.book.BookRegistry
import vazkii.patchouli.common.item.ItemModBook

class PatchouliButtonFabric : ModInitializer {
    override fun onInitialize() {
        PatchouliButton.LOGGER.info("Patchouli Button (Fabric)")
        FabricGuideConfig.bind()
        GuideSettings.isModLoaded = { FabricLoader.getInstance().isModLoaded(it) }
        CompendiumGift.dataOf = { (it as GuideData).patchouliButtonData() }
        PayloadTypeRegistry.playC2S().register(OpenExternalGuidePayload.TYPE, OpenExternalGuidePayload.STREAM_CODEC)
        ServerPlayNetworking.registerGlobalReceiver(OpenExternalGuidePayload.TYPE) { payload, context ->
            context.server().execute {
                OpenExternalGuidePayload.handle(payload, context.player())
            }
        }
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            val player = handler.player
            StartingBookCleaner.onLogin(player)
            CompendiumGift.onLogin(player)
        }
        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            StartingBookCleaner.onLogout(handler.player.uuid)
        }
        ServerTickEvents.END_SERVER_TICK.register { server ->
            for (player in server.playerList.players) {
                StartingBookCleaner.onTick(player)
            }
        }
        net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.COPY_FROM.register { oldPlayer, newPlayer, _ ->
            CompendiumGift.onClone(oldPlayer, newPlayer)
        }
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register {
            if (BookRegistry.INSTANCE.books[CompendiumBook.ID] == null) return@register
            val stack = ItemModBook.forBook(CompendiumBook.ID)
            if (!stack.isEmpty) it.accept(stack)
        }
    }
}
