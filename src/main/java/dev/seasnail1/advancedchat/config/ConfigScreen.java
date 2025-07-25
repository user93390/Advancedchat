package dev.seasnail1.advancedchat.config;

import dev.seasnail1.advancedchat.ChatControl;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.io.IOException;

public class ConfigScreen extends Screen {
    private final Config cfg = ChatControl.getConfig();
    private final int startY = 100;
    private final int chatWidth = cfg.getChatWidth();
    private final int chatHeight = cfg.getChatHeight();

    public ConfigScreen(Text title) {
        super(title);
    }

    @Override
    public void init() {
        if (cfg == null) return;
        int y = startY;

        createBoolOption("Hide Chat", cfg.isHideChat(), y);
        y += 24;
        createBoolOption("Suffix", cfg.isSuffix(), y);
        y += 24;
        createBoolOption("translate", cfg.isTranslate(), y);
        y += 24;
        createTextOption("Translation", cfg.getTranslation(), y, cfg::setTranslation, false);
        y += 24;
        createBoolOption("Filter Messages", cfg.isFilteredMessages(), y);
        y += 24;
        createTextOption("Filtered Messages", String.join(", ", cfg.getBadWords()), y, text -> cfg.setBadWords(text.split(",\\s*")), true);
        y += 24;
        createTextOption("Suffix Text", cfg.getSuffixText(), y, cfg::setSuffixText, false);
        y += 24;

        // Integer options
        createIntegerOption("Chat Height", chatHeight, y, value -> {
            try {
                int newValue = Integer.parseInt(value);
                if (newValue > 0) {
                    cfg.setOption("Chat Height", newValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format for Chat Height: " + value);
            }
        });
        y += 25;

        createIntegerOption("Chat Scale", cfg.getChatScale(), y, value -> {
            try {
                int newValue = Integer.parseInt(value);
                if (newValue > 0) {
                    cfg.setOption("Chat Scale", newValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format for Chat Scale: " + value);
            }
        });

        createIntegerOption("Chat Width", chatWidth, y + 25, value -> {
            try {
                int newValue = Integer.parseInt(value);
                if (newValue > 0) {
                    cfg.setOption("Chat Width", newValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format for Chat Height: " + value);
            }
        });

        createSaveButton(y + 125);
    }

    private void createBoolOption(String name, boolean value, int y) {
        if (this.client == null) return;

        ButtonWidget button = ButtonWidget.builder(Text.of(name + ":" + (value ? " True" : " False")), btn -> {
            boolean newValue = !value;
            btn.setMessage(Text.of(name + ": " + (newValue ? "True" : "False")));
            cfg.setOption(name, newValue);
        }).position(this.width / 2 - 75, y).width(150).build();

        button.setTooltip(Tooltip.of(Text.of("The boolean value for " + name)));

        addLabel(name, y);
        this.addDrawableChild(button);
    }

    private void createIntegerOption(String name, Number value, int y, TextConsumer consumer) {
        if (this.client == null) return;

        TextFieldWidget textField = new TextFieldWidget(this.textRenderer, this.width / 2 - 75, y, 150, 20, Text.of(name));
        textField.setText(String.valueOf(value));
        textField.setChangedListener(consumer::accept);

        textField.setTooltip(Tooltip.of(Text.of("The integer value for " + name)));

        addLabel(name, y);
        this.addDrawableChild(textField);
    }

    private void createTextOption(String name, String value, int y, TextConsumer consumer, boolean isArray) {
        if (this.client == null) return;

        TextFieldWidget textField = new TextFieldWidget(this.textRenderer, this.width / 2 - 75, y, 150, 20, Text.of(name));
        textField.setText(value);
        textField.setChangedListener(consumer::accept);

        String tooltipText = isArray ? "The array for " + name + ". use commas to separate values." : "The string value for " + name;
        textField.setTooltip(Tooltip.of(Text.of(tooltipText)));

        addLabel(name, y);
        this.addDrawableChild(textField);
    }

    private void addLabel(String text, int y) {
        TextWidget label = new TextWidget(Text.of(text), this.textRenderer);
        label.setPosition(this.width / 2 - 75 - label.getWidth() - 5, y + 3);
        this.addDrawableChild(label);
    }

    private void createSaveButton(int y) {
        ButtonWidget saveButton = ButtonWidget.builder(Text.of("Save Config"), button -> {
            if (cfg != null) {
                try {
                    cfg.saveConfig();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            this.client.setScreen(null);
        }).position(this.width / 2 - 75, y).width(150).build();

        saveButton.setTooltip(Tooltip.of(Text.of("Save the current configuration to the config file.")));
        this.addDrawableChild(saveButton);
    }

    @FunctionalInterface
    private interface TextConsumer {
        void accept(String text);
    }
}