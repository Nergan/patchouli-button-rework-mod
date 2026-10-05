package com.patchoulibutton.mod.event

import com.patchoulibutton.mod.book.CompendiumBook
import com.patchoulibutton.mod.network.OpenExternalGuidePayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.CreativeModeTabs
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import vazkii.patchouli.common.book.BookRegistry
import vazkii.patchouli.common.item.ItemModBook

object ModSetup {
    fun init(modBus: IEventBus, modContainer: ModContainer) {
        modBus.register(this)
        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.patchoulibutton.mod.client.ClientModEvents.init(modBus, modContainer)
        }
    }

    @SubscribeEvent
    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        event.registrar("1").playToServer(
            OpenExternalGuidePayload.TYPE,
            OpenExternalGuidePayload.STREAM_CODEC,
            { payload, context ->
                context.enqueueWork {
                    val player = context.player() as? ServerPlayer ?: return@enqueueWork
                    OpenExternalGuidePayload.handle(payload, player)
                }
            },
        )
    }

    @SubscribeEvent
    fun addToCreativeTab(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey != CreativeModeTabs.TOOLS_AND_UTILITIES) return
        if (BookRegistry.INSTANCE.books[CompendiumBook.ID] == null) return
        event.accept(ItemModBook.forBook(CompendiumBook.ID))
    }
}
