package com.playerfilter.mixin;

import com.playerfilter.PlayerFilterMod;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class ChatHudMixin {

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"), cancellable = true)
    private void onAddMessage(Component message, CallbackInfo ci) {

        if (PlayerFilterMod.filteredPlayer == null) return;

        String filteredPlayer = PlayerFilterMod.filteredPlayer;
        String rawMessage = message.getString();

        boolean isFromFilteredPlayer = rawMessage.startsWith("<" + filteredPlayer + ">")
                || rawMessage.startsWith("[" + filteredPlayer + "]")
                || rawMessage.startsWith(filteredPlayer + ": ")
                || rawMessage.startsWith(filteredPlayer + " ");

        boolean isSystemMessage = !rawMessage.contains("<") && !rawMessage.contains("[")
                && !rawMessage.contains(": ");

        if (!isFromFilteredPlayer && !isSystemMessage) {
            ci.cancel();
        }
    }
}
