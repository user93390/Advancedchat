package dev.seasnail1.advancedchat.config;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.seasnail1.advancedchat.ChatControl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public static final File CONFIG_FILE = new File("config/advancedChat.json");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Configuration properties
    public static boolean hideChat = false;
    public static boolean suffix = false;
    public static boolean filterMessages = true;
    public static boolean translate = false;
    public static String[] badWords = new String[]{};
    public static String suffixText = "";
    public static String translateKey = "your-translate-key-here";
    public static String translation = "sp";
    public static int chatWidth = 300;
    public static int chatHeight = 150;
    public static float chatScale = 1;

    final Map<String, Object> defaultConfig = new HashMap<>();

    {
        defaultConfig.put("hideChat", false);
        defaultConfig.put("suffix", false);
        defaultConfig.put("suffixText", "");
        defaultConfig.put("FilterMessages", true);
        defaultConfig.put("filteredMessages", List.of("badword1", "badword2"));
        defaultConfig.put("chatWidth", 300);
        defaultConfig.put("chatHeight", 150);
        defaultConfig.put("chatScale", 1);
        defaultConfig.put("translate", false);
        defaultConfig.put("translateKey", "your-translate-key-here");
        defaultConfig.put("translation", "sp");
    }

    public void init() {
        try {
            if (!CONFIG_FILE.exists()) {
                CONFIG_FILE.createNewFile();
                ChatControl.getLogger().info("This is your first time using chat control! Welcome");

                MAPPER.writer(new DefaultPrettyPrinter()).writeValue(CONFIG_FILE, defaultConfig);
            }
            loadConfig();
        } catch (Exception ex) {
            ChatControl.getLogger().error("Error initializing config: {}", ex.getMessage());
        }
    }

    private void loadConfig() throws IOException {
        Map<?, ?> map = MAPPER.readValue(CONFIG_FILE, Map.class);

        hideChat = getBooleanValue(map, "hideChat", false);
        suffix = getBooleanValue(map, "suffix", false);
        filterMessages = getBooleanValue(map, "FilterMessages", true);
        suffixText = getStringValue(map, "suffixText");
        translateKey = getStringValue(map, "translateKey");
        chatWidth = getIntValue(map, "chatWidth", 300);
        chatHeight = getIntValue(map, "chatHeight", 150);
        chatScale = getIntValue(map, "chatScale", 1);
        translate = getBooleanValue(map, "translate", false);

        if (map.containsKey("filteredMessages") && map.get("filteredMessages") instanceof List<?> list) {
            badWords = list.stream().map(Object::toString).toArray(String[]::new);
        }
    }

    private int getIntValue(Map<?, ?> map, String key, int defaultValue) {
        return map.containsKey(key) && map.get(key) instanceof Integer ? (int) map.get(key) : defaultValue;
    }

    private boolean getBooleanValue(Map<?, ?> map, String key, boolean defaultValue) {
        return map.containsKey(key) && map.get(key) instanceof Boolean ? (boolean) map.get(key) : defaultValue;
    }

    private String getStringValue(Map<?, ?> map, String key) {
        return map.containsKey(key) && map.get(key) instanceof String ? (String) map.get(key) : "";
    }

    public void saveConfig() throws IOException {
        Map<String, Object> configs = new HashMap<>();
        configs.put("suffix", suffix);
        configs.put("suffixText", suffixText);
        configs.put("hideChat", hideChat);
        configs.put("translate", translate);
        configs.put("translateKey", translateKey);
        configs.put("translation", translation);
        configs.put("FilterMessages", filterMessages);
        configs.put("filteredMessages", List.of(badWords));
        configs.put("chatWidth", chatWidth);
        configs.put("chatHeight", chatHeight);
        configs.put("chatScale", chatScale);


        MAPPER.writer(new DefaultPrettyPrinter()).writeValue(CONFIG_FILE, configs);
    }

    // Getters
    public boolean isHideChat() {
        return hideChat;
    }

    public boolean isSuffix() {
        return suffix;
    }

    public boolean isFilteredMessages() {
        return filterMessages;
    }

    public String getSuffixText() {
        return suffixText;
    }

    public String[] getBadWords() {
        return badWords;
    }

    public int getChatWidth() {
        return chatWidth;
    }

    public int getChatHeight() {
        return chatHeight;
    }

    public float getChatScale() {
        return chatScale;
    }

    public boolean isTranslate() {
        return translate;
    }

    public String getTranslateKey() {
        return translateKey;
    }

    public String getTranslation() {
        return translation;
    }

    // Setters
    public void setHideChat(boolean value) {
        hideChat = value;
    }

    public void setSuffix(boolean value) {
        suffix = value;
    }

    public void setFilterMessages(boolean value) {
        filterMessages = value;
    }

    public void setSuffixText(String value) {
        suffixText = value;
    }

    public void setBadWords(String[] value) {
        badWords = value;
    }

    public void setTranslateKey(String value) {
        translateKey = value;
    }

    public void setTranslation(String value) {
        translation = value;
    }

    public void setOption(String option, Object value) {
        switch (option.replace(" ", "").toLowerCase()) {
            case "hidechat" -> setHideChat((boolean) value);
            case "suffix" -> setSuffix((boolean) value);
            case "suffixtext" -> setSuffixText((String) value);
            case "filtermessages" -> setFilterMessages((boolean) value);
            case "filteredmessages" -> setBadWords((String[]) value);
            case "chatwidth" -> chatWidth = (int) value;
            case "chatheight" -> chatHeight = (int) value;
            case "chatscale" -> chatScale = (int) value;
            case "translate" -> translate = (boolean) value;
            case "translatekey" -> translateKey = (String) value;
            case "translation" -> translation = (String) value;
            default -> ChatControl.getLogger().warn("Unknown config option: {}", option);
        }
    }
}