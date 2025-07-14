package dev.seasnail1.chatcontrol.events;

import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;

public class MessageReceiveEvent implements ICancellable {
    private boolean cancelled = false;

    private final Text message;
    private final MessageSignatureData signatureData;
    private final MessageIndicator indicator;

    public MessageReceiveEvent(Text message, MessageSignatureData signatureData, MessageIndicator indicator) {
        this.message = message;
        this.signatureData = signatureData;
        this.indicator = indicator;
    }

    public Text getMessage() {
        return message;
    }

    public MessageSignatureData getSignatureData() {
        return signatureData;
    }

    public MessageIndicator getIndicator() {
        return indicator;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
