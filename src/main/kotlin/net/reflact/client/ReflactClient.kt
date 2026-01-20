package net.reflact.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.reflact.client.config.ReflactConfig
import net.reflact.client.hud.ReflactHud
import org.slf4j.LoggerFactory

class ReflactClient : ClientModInitializer {

    companion object {
        val LOGGER = LoggerFactory.getLogger("reflact-client")
        val CONFIG: ReflactConfig = ReflactConfig.createAndLoad()
    }

    override fun onInitializeClient() {
        LOGGER.info("Reflact Client initialized!")

        // 1. Register Payload Types
        PayloadTypeRegistry.playS2C().register(
            net.reflact.client.network.ReflactJsonPayload.ID,
            net.reflact.client.network.ReflactJsonPayload.CODEC
        )
        PayloadTypeRegistry.playC2S().register(
            net.reflact.client.network.ReflactJsonPayload.ID,
            net.reflact.client.network.ReflactJsonPayload.CODEC
        )

        // 2. HUD
        ReflactHud.initialize()
        net.reflact.client.render.DamageIndicatorRenderer.init()

        // 3. Networking Receiver
        net.reflact.client.network.ClientNetworkManager.init()

        // 4. Input Handling
        net.reflact.client.input.KeyInputHandler.register()
    }
}
