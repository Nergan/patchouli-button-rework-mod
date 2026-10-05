package com.patchoulibutton.mod.loader.fabric

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.patchoulibutton.mod.GuideSettings
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Files

object FabricGuideConfig {
    var giveCompendium: Boolean = true
    var clearStartingBooks: Boolean = true
    var showBookButton: Boolean = false
    var buttonX: Int = 127
    var buttonY: Int = 61

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val directory = FabricLoader.getInstance().configDir

    fun bind() {
        load()
        GuideSettings.giveCompendium = { giveCompendium }
        GuideSettings.clearStartingBooks = { clearStartingBooks }
        GuideSettings.showBookButton = { showBookButton }
        GuideSettings.buttonX = { buttonX }
        GuideSettings.buttonY = { buttonY }
    }

    fun load() {
        val path = directory.resolve("patchoulibutton.json")
        if (!Files.isRegularFile(path)) return
        val stored = gson.fromJson(Files.readString(path), Stored::class.java) ?: return
        giveCompendium = stored.giveCompendium
        clearStartingBooks = stored.clearStartingBooks
        showBookButton = stored.showBookButton
        buttonX = stored.buttonX
        buttonY = stored.buttonY
    }

    fun save() {
        Files.createDirectories(directory)
        val stored = Stored(giveCompendium, clearStartingBooks, showBookButton, buttonX, buttonY)
        Files.writeString(directory.resolve("patchoulibutton.json"), gson.toJson(stored))
    }

    private data class Stored(
        val giveCompendium: Boolean = true,
        val clearStartingBooks: Boolean = true,
        val showBookButton: Boolean = false,
        val buttonX: Int = 127,
        val buttonY: Int = 61,
    )
}
