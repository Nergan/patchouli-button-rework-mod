package com.patchoulibutton.mod.loader.fabric

import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

object FabricConfigScreen {
    fun create(parent: Screen): Screen {
        val builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.translatable("patchoulibutton.configuration.title"))
            .setSavingRunnable { FabricGuideConfig.save() }
        val entries = builder.entryBuilder()
        val books = builder.getOrCreateCategory(Component.translatable("patchoulibutton.configuration.books"))
        books.addEntry(
            entries.startBooleanToggle(
                Component.translatable("patchoulibutton.configuration.books.give_compendium"),
                FabricGuideConfig.giveCompendium,
            ).setDefaultValue(true).setSaveConsumer { FabricGuideConfig.giveCompendium = it }.build(),
        )
        books.addEntry(
            entries.startBooleanToggle(
                Component.translatable("patchoulibutton.configuration.books.clear_starting_books"),
                FabricGuideConfig.clearStartingBooks,
            ).setDefaultValue(true).setSaveConsumer { FabricGuideConfig.clearStartingBooks = it }.build(),
        )
        val button = builder.getOrCreateCategory(Component.translatable("patchoulibutton.configuration.button"))
        button.addEntry(
            entries.startBooleanToggle(
                Component.translatable("patchoulibutton.configuration.button.show_book_button"),
                FabricGuideConfig.showBookButton,
            ).setDefaultValue(false).setSaveConsumer { FabricGuideConfig.showBookButton = it }.build(),
        )
        button.addEntry(
            entries.startIntField(
                Component.translatable("patchoulibutton.configuration.button.button_x"),
                FabricGuideConfig.buttonX,
            ).setDefaultValue(127).setMin(-40).setMax(200).setSaveConsumer { FabricGuideConfig.buttonX = it }.build(),
        )
        button.addEntry(
            entries.startIntField(
                Component.translatable("patchoulibutton.configuration.button.button_y"),
                FabricGuideConfig.buttonY,
            ).setDefaultValue(61).setMin(-40).setMax(200).setSaveConsumer { FabricGuideConfig.buttonY = it }.build(),
        )
        return builder.build()
    }
}
