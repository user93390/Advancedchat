package dev.seasnail1.chatcontrol.config;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.seasnail1.chatcontrol.ChatControl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public static final File CONFIG_FILE = new File("config/chatControl_config.json");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Configuration properties
    public static boolean hideChat = false;
    public static boolean suffix = false;
    public static boolean filterMessages = true;
    public static String[] badWords = new String[]{};
    public static String suffixText = "";

    public void init() {
        try {
            createDefaultConfigIfNeeded();
            loadConfig();
        } catch (Exception ex) {
            ChatControl.getLogger().error("Error initializing config: {}", ex.getMessage());
        }
    }

    private void createDefaultConfigIfNeeded() throws IOException {
        if (!CONFIG_FILE.exists()) {
            CONFIG_FILE.createNewFile();
            ChatControl.getLogger().info("This is your first time using chat control! Welcome");

            Map<String, Object> defaultConfig = Map.of(
                    "hideChat", false,
                    "suffix", false,
                    "suffixText", "",
                    "FilterMessages", true,
                    "filteredMessages", List.of("badword1", "badword2")
            );

            MAPPER.writer(new DefaultPrettyPrinter()).writeValue(CONFIG_FILE, defaultConfig);
        }
    }

    private void loadConfig() throws IOException {
        Map<?, ?> map = MAPPER.readValue(CONFIG_FILE, Map.class);

        hideChat = getBooleanValue(map, "hideChat", false);
        suffix = getBooleanValue(map, "suffix", false);
        filterMessages = getBooleanValue(map, "FilterMessages", true);
        suffixText = getStringValue(map, "suffixText", "");

        if (map.containsKey("filteredMessages") && map.get("filteredMessages") instanceof List<?> list) {
            badWords = list.stream().map(Object::toString).toArray(String[]::new);
        }
    }

    private boolean getBooleanValue(Map<?, ?> map, String key, boolean defaultValue) {
        return map.containsKey(key) && map.get(key) instanceof Boolean ?
                (boolean) map.get(key) : defaultValue;
    }

    private String getStringValue(Map<?, ?> map, String key, String defaultValue) {
        return map.containsKey(key) && map.get(key) instanceof String ?
                (String) map.get(key) : defaultValue;
    }

    public void saveConfig() throws IOException {
        Map<String, Object> configs = new HashMap<>();
        configs.put("suffix", suffix);
        configs.put("suffixText", suffixText);
        configs.put("hideChat", hideChat);
        configs.put("FilterMessages", filterMessages);
        configs.put("filteredMessages", Arrays.asList(badWords));

        MAPPER.writer(new DefaultPrettyPrinter()).writeValue(CONFIG_FILE, configs);
    }

    // Getters
    public boolean isHideChat() { return hideChat; }
    public boolean isSuffix() { return suffix; }
    public boolean isFilteredMessages() { return filterMessages; }
    public String getSuffixText() { return suffixText; }
    public String[] getBadWords() { return badWords; }

    // Setters
    public void setHideChat(boolean value) { hideChat = value; }
    public void setSuffix(boolean value) { suffix = value; }
    public void setFilterMessages(boolean value) { filterMessages = value; }
    public void setSuffixText(String value) { suffixText = value; }
    public void setBadWords(String[] value) { badWords = value; }

    public void setOption(String option, Object value) {
        switch (option.replace(" ", "").toLowerCase()) {
            case "hidechat" -> setHideChat((boolean) value);
            case "suffix" -> setSuffix((boolean) value);
            case "suffixtext" -> setSuffixText((String) value);
            case "filtermessages" -> setFilterMessages((boolean) value);
            case "filteredmessages" -> setBadWords((String[]) value);
            default -> ChatControl.getLogger().warn("Unknown config option: {}", option);
        }
    }
}