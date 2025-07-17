package dev.seasnail1.advancedchat.events;

import meteordevelopment.orbit.ICancellable;
import net.minecraft.text.Text;

public class MessageReceiveEvent implements ICancellable {
    private boolean cancelled = false;
    private boolean modified = false;
    private Text message;

    public MessageReceiveEvent(Text message) {
        this.message = message;
    }

    public Text getMessage() {
        return message;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public void setMessage(Text message) {
        this.message = Text.of(message);
        setModified(true);
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }
}
