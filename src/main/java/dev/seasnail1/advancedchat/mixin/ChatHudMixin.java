package dev.seasnail1.advancedchat.mixin;

import dev.seasnail1.advancedchat.ChatControl;
import dev.seasnail1.advancedchat.events.MessageReceiveEvent;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Unique
    Map<Text, Boolean> messageCache = new HashMap<>();

    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", cancellable = true)
    private void onAddMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        if (Boolean.TRUE.equals(messageCache.get(message))) {
            ci.cancel();
            return;
        }

        MessageReceiveEvent event = new MessageReceiveEvent(message);
        ChatControl.bus.post(event);

        if (event.isCancelled()) {
            messageCache.put(message, true);
            ci.cancel();
            return;
        }

        if (event.isModified()) {
            ci.cancel();
            // Cast this to ChatHud and call the original method with modified message
            ((ChatHud)(Object)this).addMessage(Text.of(event.getMessage().getString()), signatureData, indicator);
        }
    }

    @Inject(method = "getHeight()I", at = @At("HEAD"), cancellable = true)
    private void getHeight(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ChatControl.getConfig().getChatHeight());
    }

    @Inject(method = "getWidth()I", at = @At("HEAD"), cancellable = true)
    private void GetWidth(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ChatControl.getConfig().getChatWidth());
    }

    @Inject(method = "getChatScale", at = @At("HEAD"), cancellable = true)
    private void getChatScale(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue((double) ChatControl.getConfig().getChatScale());
    }
}
