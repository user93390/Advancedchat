package dev.seasnail1.chatcontrol;

import dev.seasnail1.chatcontrol.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class ChatControl implements ModInitializer {
    public static final Logger log = LoggerFactory.getLogger("chatControl");
    public final String version = FabricLoader.getInstance().getModContainer("chat-control").get().getMetadata().getVersion().getFriendlyString();

    private static Config config;

    @Override
    public void onInitialize() {
        try {
            log.info("Loading ChatControl {}...", version);
            config = new Config();

            config.init();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    getConfig().saveConfig();
                } catch (IOException e) {
                    log.error("Failed to save configs!");
                }
            }));
        } catch (Exception e) {
            log.error("Failed to initilize: Full stack trace below:");

            log.info("//////////////// Copy and paste this into the discord /////////////////////");
            log.warn("getStackTrace:{}", Arrays.toString(e.getStackTrace()));
            log.warn("getCause:{}", String.valueOf(e.getCause()));
            log.warn("getMessage:{}", e.getMessage());
            log.warn("getLocalizedMessage:{}", e.getLocalizedMessage());
            log.warn("getSuppressed:{}", Arrays.toString(e.getSuppressed()));
            log.warn("getClass:{}", e.getClass());

            System.exit(1);
        }
    }

    public static Config getConfig() {
        return config;
    }
}
