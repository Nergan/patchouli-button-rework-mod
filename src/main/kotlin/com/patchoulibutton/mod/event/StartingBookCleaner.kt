package com.patchoulibutton.mod.event

import com.patchoulibutton.mod.config.ServerConfig
import com.patchoulibutton.mod.util.PatchouliGuideItems
import com.patchoulibutton.mod.util.StartingBookSweep
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import java.util.UUID

/**
 * Снимает книги `patchouli:guide_book`, полевой справочник Critters and Crawlers
 * и атлас More Critters,
 * которые появились в первые [StartingBookSweep.WINDOW_TICKS] тиков после входа.
 * Книга сборки и то, что уже лежало в инвентаре, остаются.
 */
object StartingBookCleaner {
    private val sweeps = HashMap<UUID, Sweep>()

    private class Sweep(val baseline: Map<String, Int>, var ticksLeft: Int)

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity as? ServerPlayer ?: return
        sweeps.remove(player.uuid)
        if (!ServerConfig.CONFIG.clearStartingBooks.get()) return
        sweeps[player.uuid] = Sweep(PatchouliGuideItems.counts(player), StartingBookSweep.WINDOW_TICKS)
    }

    @SubscribeEvent
    fun onLogout(event: PlayerEvent.PlayerLoggedOutEvent) {
        sweeps.remove(event.entity.uuid)
    }

    @SubscribeEvent
    fun onTick(event: PlayerTickEvent.Post) {
        val player = event.entity as? ServerPlayer ?: return
        val sweep = sweeps[player.uuid] ?: return
        val extra = StartingBookSweep.excess(sweep.baseline, PatchouliGuideItems.counts(player))
        PatchouliGuideItems.removeExcess(player, extra)
        sweep.ticksLeft--
        if (sweep.ticksLeft <= 0) sweeps.remove(player.uuid)
    }
}
