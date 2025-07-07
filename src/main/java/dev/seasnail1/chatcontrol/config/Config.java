package dev.seasnail1.chatcontrol.config;


import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public static final File configFile = new File("config/chatControl_config.json");

    public static boolean hideChat;
    public static String[] badWords = new String[]{};
    public static boolean suffix = false;
    public static String suffixText = "";

    public Config() {}

    public void init() {
        try {
            if(!configFile.exists()) configFile.createNewFile();

            ObjectMapper mapper = new ObjectMapper();

            Map<?, ?> map = mapper.readValue(configFile, Map.class);

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                switch (entry.getKey().toString()) {
                    case "hideChat":
                        hideChat = (boolean) entry.getValue();
                        break;

                    case "filteredMessages":
                        if (entry.getValue() instanceof List<?> list) {
                            badWords = list.stream()
                                    .map(Object::toString)
                                    .toArray(String[]::new);
                            break;
                        }

                    case "suffix":
                        suffix = (boolean) entry.getValue();
                        break;

                    case "suffixText":
                        suffixText = (String) entry.getValue();
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveConfig() throws IOException {
        Map<String, Object> configs = new HashMap<>();
        configs.put("suffix", suffix);
        configs.put("suffixText", suffixText);
        configs.put("hideChat", hideChat);
        configs.put("filteredMessages", badWords);

        ObjectMapper mapper = new ObjectMapper();

        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        writer.writeValue(configFile, configs);
    }
}