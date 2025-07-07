package dev.seasnail1.chatcontrol;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.seasnail1.chatcontrol.config.Config;
import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            File cfg = Config.configFile;
            if (cfg.exists()) {
                if (!Desktop.isDesktopSupported()) {
                    System.err.println("Desktop Support Not Present in the system.");
                    return MinecraftClient.getInstance().currentScreen;
                }
                Desktop desktop = Desktop.getDesktop();

                if (cfg.exists()) {
                    try {
                        desktop.open(cfg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return MinecraftClient.getInstance().currentScreen;
        };
    }
}