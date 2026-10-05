package com.patchoulibutton.mod.event

import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent

object NeoForgeHooks {
    fun register() {
        NeoForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity as? ServerPlayer ?: return
        StartingBookCleaner.onLogin(player)
        CompendiumGift.onLogin(player)
    }

    @SubscribeEvent
    fun onLogout(event: PlayerEvent.PlayerLoggedOutEvent) {
        StartingBookCleaner.onLogout(event.entity.uuid)
    }

    @SubscribeEvent
    fun onTick(event: PlayerTickEvent.Post) {
        val player = event.entity as? ServerPlayer ?: return
        StartingBookCleaner.onTick(player)
    }

    @SubscribeEvent
    fun onClone(event: PlayerEvent.Clone) {
        CompendiumGift.onClone(event.original, event.entity)
    }
}
