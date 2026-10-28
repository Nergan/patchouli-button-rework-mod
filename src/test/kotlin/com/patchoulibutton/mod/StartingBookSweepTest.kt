package com.patchoulibutton.mod

import com.patchoulibutton.mod.util.StartingBookSweep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StartingBookSweepTest {
    @Test
    fun keepsBooksThatWereAlreadyThere() {
        val baseline = mapOf("botania:lexicon" to 1)
        val current = mapOf("botania:lexicon" to 1)
        assertEquals(emptyMap<String, Int>(), StartingBookSweep.excess(baseline, current))
    }

    @Test
    fun removesOnlyTheCopiesThatAppearedAfterLogin() {
        val baseline = mapOf("botania:lexicon" to 1)
        val current = mapOf("botania:lexicon" to 1, "create:ponder" to 2)
        assertEquals(mapOf("create:ponder" to 2), StartingBookSweep.excess(baseline, current))
    }

    @Test
    fun keepsTheSengokuGuideAndHidesTheOldId() {
        assertEquals(true, StartingBookSweep.keepInInventory(StartingBookSweep.SENGOKU_GUIDE))
        assertEquals(false, StartingBookSweep.keepInInventory(StartingBookSweep.SENGOKU_GUIDE_LEGACY))
        assertEquals(false, StartingBookSweep.showInCompendium(StartingBookSweep.SENGOKU_GUIDE_LEGACY))
        assertEquals(true, StartingBookSweep.showInCompendium(StartingBookSweep.SENGOKU_GUIDE))
    }
}
