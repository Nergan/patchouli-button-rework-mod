package com.patchoulibutton.mod.util

/**
 * Сколько экземпляров каждой книги убрать, если после входа их стало больше,
 * чем было в момент входа.
 */
object StartingBookSweep {
    const val WINDOW_TICKS = 100
    const val SENGOKU_GUIDE = "sengoku:sengoku_guide"
    const val SENGOKU_GUIDE_LEGACY = "packfixes:sengoku_guide"

    fun keepInInventory(bookId: String): Boolean = bookId == SENGOKU_GUIDE

    fun showInCompendium(bookId: String): Boolean = bookId != SENGOKU_GUIDE_LEGACY

    fun excess(baseline: Map<String, Int>, current: Map<String, Int>): Map<String, Int> {
        val extra = LinkedHashMap<String, Int>()
        for ((key, count) in current) {
            val overflow = count - (baseline[key] ?: 0)
            if (overflow > 0) extra[key] = overflow
        }
        return extra
    }
}
