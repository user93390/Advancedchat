package dev.seasnail1.advancedchat.mixin;

import dev.seasnail1.advancedchat.ChatControl;
import dev.seasnail1.advancedchat.events.MessageSendEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class NetworkMixin {

    @Inject(method = "sendChatMessage", at = @At("HEAD"))
    private void onSendChatMessage(String message, CallbackInfo ci) {
        MessageSendEvent event = new MessageSendEvent(message);

        if (event.modified) {
            message = event.getMessage();
        }

        event = new MessageSendEvent(message);
        ChatControl.bus.post(event);
    }
}
