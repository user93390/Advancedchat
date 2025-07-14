package dev.seasnail1.chatcontrol.mixin;

import dev.seasnail1.chatcontrol.ChatControl;
import dev.seasnail1.chatcontrol.events.MessageReceiveEvent;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", cancellable = true)
    private void onAddMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        MessageReceiveEvent event = new MessageReceiveEvent(message, signatureData, indicator);
        ChatControl.bus.post(event);

        if (event.isCancelled()) {
            ci.cancel();
            return;
        }

        ChatControl.bus.post(event);
    }
}

