package dev.seasnail1.advancedchat;

import dev.seasnail1.advancedchat.config.Config;
import dev.seasnail1.advancedchat.events.MessageReceiveEvent;
import dev.seasnail1.advancedchat.events.MessageSendEvent;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.util.Arrays;

public class ChatControl implements ModInitializer {
    private static final Logger log = LoggerFactory.getLogger("Advanced-chat");
    public static IEventBus bus;
    private static Config config;
    public final String version = FabricLoader.getInstance().getModContainer("advanced-chat").get().getMetadata().getVersion().getFriendlyString();

    public static Config getConfig() {
        return config;
    }

    public static Logger getLogger() {
        return log;
    }

    @Override
    public void onInitialize() {
        try {
            log.info("Loading ChatControl {}...", version);
            bus = new EventBus();
            config = new Config();
            config.init();
            bus.registerLambdaFactory("dev.seasnail1.advancedchat", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
            bus.subscribe(this);


            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    config.saveConfig();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (Exception e) {
            log.error("Failed to initilize: Full stack trace below...");

            log.info("//////////////// Copy and paste this into the issue tracker /////////////////////");
            log.warn("getStackTrace: {}", Arrays.toString(e.getStackTrace()));
            log.warn("getCause: {}", String.valueOf(e.getCause()));
            log.warn("getMessage: {}", e.getMessage());
            log.warn("getLocalizedMessage: {}", e.getLocalizedMessage());
            log.warn("getSuppressed: {}", Arrays.toString(e.getSuppressed()));
            log.warn("getClass: {}", e.getClass());

            System.exit(1);
        }
    }

    @EventHandler
    public void onMessageReceive(MessageReceiveEvent event) throws MalformedURLException {
        String message = event.getMessage().getString();

        if (config.isFilteredMessages()) {
            for (int i = 0; i < config.getBadWords().length; i++) {
                if (message.contains(config.getBadWords()[i])) {
                    System.out.println("Message blocked: " + message);

                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (config.isTranslate()) {
            // TODO: Implement translation logic
        }
    }

    @EventHandler
    public void onMessageSend(MessageSendEvent event) {
        String message = event.getMessage();

        if (getConfig().isSuffix()) {
            event.setMessage(message + " " + getConfig().getSuffixText());
        }
    }
}
