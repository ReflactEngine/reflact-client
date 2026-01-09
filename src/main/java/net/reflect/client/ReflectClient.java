package net.reflect.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("reflect-client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Reflect Client initialized!");
    }
}
