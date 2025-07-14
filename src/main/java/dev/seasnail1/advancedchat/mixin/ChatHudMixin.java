package dev.seasnail1.advancedchat.mixin;

import dev.seasnail1.advancedchat.ChatControl;
import dev.seasnail1.advancedchat.events.MessageReceiveEvent;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;)V", cancellable = true)
    private void onAddMessage(Text message, CallbackInfo ci) {
        MessageReceiveEvent event = new MessageReceiveEvent(message);
        ChatControl.bus.post(event);
        if (event.isCancelled()) {
            ci.cancel();
            return;
        }

        ChatControl.bus.post(event);
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
