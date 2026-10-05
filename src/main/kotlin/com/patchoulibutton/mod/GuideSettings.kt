package com.patchoulibutton.mod

/**
 * Значения, которые NeoForge читает из ModConfigSpec, а Fabric — из своего файла.
 * Загрузчик подставляет лямбды при старте.
 */
object GuideSettings {
    var giveCompendium: () -> Boolean = { true }
    var clearStartingBooks: () -> Boolean = { true }
    var showBookButton: () -> Boolean = { false }
    var buttonX: () -> Int = { 127 }
    var buttonY: () -> Int = { 61 }
    var isModLoaded: (String) -> Boolean = { false }
    var sendOpenGuide: (String) -> Unit = {}
}
