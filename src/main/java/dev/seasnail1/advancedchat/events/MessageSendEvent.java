package dev.seasnail1.advancedchat.events;

public class MessageSendEvent {
    private String message;

    public boolean modified = false;

    public MessageSendEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;

        this.modified = true;
    }
}


