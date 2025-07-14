package dev.seasnail1.advancedchat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.seasnail1.advancedchat.config.ConfigScreen;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            try {
                // Create a new ConfigScreen instance
                return new ConfigScreen(Text.of("Advanced chat"));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };
    }
}