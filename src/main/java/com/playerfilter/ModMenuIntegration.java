package com.playerfilter;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Player Filter Settings"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

            general.addEntry(entryBuilder
                .startBooleanToggle(Component.literal("Show my own messages"), PlayerConfig.showOwnMessages)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Show your own messages while filter is active"))
                .setSaveConsumer(val -> PlayerConfig.showOwnMessages = val)
                .build()
            );

            general.addEntry(entryBuilder
                .startBooleanToggle(Component.literal("Show server messages"), PlayerConfig.showSystemMessages)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Show server system messages while filter is active"))
                .setSaveConsumer(val -> PlayerConfig.showSystemMessages = val)
                .build()
            );

            return builder.build();
        };
    }
}