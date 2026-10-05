package com.patchoulibutton.mod.event

import com.patchoulibutton.mod.GuideSettings
import com.patchoulibutton.mod.util.PatchouliGuideItems
import com.patchoulibutton.mod.util.StartingBookSweep
import net.minecraft.server.level.ServerPlayer
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

    fun onLogin(player: ServerPlayer) {
        sweeps.remove(player.uuid)
        if (!GuideSettings.clearStartingBooks()) return
        sweeps[player.uuid] = Sweep(PatchouliGuideItems.counts(player), StartingBookSweep.WINDOW_TICKS)
    }

    fun onLogout(playerId: UUID) {
        sweeps.remove(playerId)
    }

    fun onTick(player: ServerPlayer) {
        val sweep = sweeps[player.uuid] ?: return
        val extra = StartingBookSweep.excess(sweep.baseline, PatchouliGuideItems.counts(player))
        PatchouliGuideItems.removeExcess(player, extra)
        sweep.ticksLeft--
        if (sweep.ticksLeft <= 0) sweeps.remove(player.uuid)
    }
}
