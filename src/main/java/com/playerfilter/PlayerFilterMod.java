package com.playerfilter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class PlayerFilterMod implements ClientModInitializer {

    public static String filteredPlayer = null;

    private static final SuggestionProvider<net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource> ONLINE_PLAYERS =
        (context, builder) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.getConnection() != null) {
                for (PlayerInfo info : mc.getConnection().getListedOnlinePlayers()) {
                    String name = info.getProfile().name();
                    if (name.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
                        builder.suggest(name);
                    }
                }
            }
            return builder.buildFuture();
        };

    private static String getSenderFromText(String raw) {
        String stripped = raw.replaceAll("§[0-9a-fk-or]", "").trim();


        if (stripped.startsWith("<")) {
            int close = stripped.indexOf(">");
            if (close != -1) {
                String inside = stripped.substring(1, close).trim();
                String[] parts = inside.split("\\s+");
                return parts[parts.length - 1];
            }
        }

        if (stripped.startsWith("[")) {
            int close = stripped.indexOf("]");
            if (close != -1 && close < 40) {
                String inside = stripped.substring(1, close).trim();
                if (!inside.isEmpty() && inside.length() <= 32) {
                    String[] parts = inside.split("\\s+");
                    return parts[parts.length - 1];
                }
            }
        }


        String[] separators = {
            " \u00bb ",
            " \u00ab ",
            " -> ",
            " >> ",
            " | ",
            " : ",
            ": ",
            " \u2192 ",
            " \u27a4 ",
            " \u2503 ",
            " \u25b6 ",
        };

        int bestIdx = Integer.MAX_VALUE;
        String bestSep = null;

        for (String sep : separators) {
            int idx = stripped.indexOf(sep);
            if (idx != -1 && idx < bestIdx) {
                bestIdx = idx;
                bestSep = sep;
            }
        }

        if (bestSep != null) {
            String possibleName = stripped.substring(0, bestIdx).trim();
            String[] parts = possibleName.split("\\s+");
            String lastName = parts[parts.length - 1];
            if (lastName.length() >= 1 && lastName.length() <= 32) {
                return lastName;
            }
        }

        return null;
    }

    private static String getOwnName() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null ? mc.player.getName().getString() : null;
    }

    @Override
    public void onInitializeClient() {

        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            if (filteredPlayer == null) return true;

            String ownName = getOwnName();
            String senderName = null;

            if (sender != null) {
                senderName = sender.name();
                if (senderName == null || senderName.isEmpty()) {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.getConnection() != null) {
                        PlayerInfo info = mc.getConnection().getPlayerInfo(sender.id());
                        if (info != null) senderName = info.getProfile().name();
                    }
                }
            }

            if (senderName == null) senderName = getSenderFromText(message.getString());

            if (senderName == null) return PlayerConfig.showSystemMessages;
            if (ownName != null && senderName.equalsIgnoreCase(ownName)) return PlayerConfig.showOwnMessages;
            if (senderName.equalsIgnoreCase(filteredPlayer)) return true;
            return false;
        });

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (filteredPlayer == null) return true;
            if (overlay) return true;

            String raw = message.getString();
            String ownName = getOwnName();

            if (raw.contains("Chat filter")) return true;

            String senderName = getSenderFromText(raw);

            if (senderName == null) return PlayerConfig.showSystemMessages;
            if (ownName != null && senderName.equalsIgnoreCase(ownName)) return PlayerConfig.showOwnMessages;
            if (senderName.equalsIgnoreCase(filteredPlayer)) return true;
            return false;
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                ClientCommandManager.literal("chatfilter")
                    .then(
                        ClientCommandManager.argument("player", StringArgumentType.word())
                            .suggests(ONLINE_PLAYERS)
                            .executes(ctx -> {
                                String playerName = StringArgumentType.getString(ctx, "player");
                                filteredPlayer = playerName;
                                ctx.getSource().sendFeedback(
                                    Component.literal("Chat filter enabled: ")
                                        .withStyle(ChatFormatting.GREEN)
                                        .append(Component.literal(playerName).withStyle(ChatFormatting.YELLOW))
                                );
                                return 1;
                            })
                    )
            );

            dispatcher.register(
                ClientCommandManager.literal("chatfilteroff")
                    .executes(ctx -> {
                        filteredPlayer = null;
                        ctx.getSource().sendFeedback(
                            Component.literal("Chat filter disabled.")
                                .withStyle(ChatFormatting.RED)
                        );
                        return 1;
                    })
            );
        });
    }
}
