package net.reflact.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.reflact.client.hud.ReflactHud;
import net.reflact.common.network.packet.CastSpellPacket;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflactClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("reflact-client");
    
    // Keybindings
    private static KeyBinding castFireballKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Reflact Client initialized!");

        // 1. Register Payload Types
        PayloadTypeRegistry.playS2C().register(net.reflact.client.network.ReflactJsonPayload.ID, net.reflact.client.network.ReflactJsonPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(net.reflact.client.network.ReflactJsonPayload.ID, net.reflact.client.network.ReflactJsonPayload.CODEC);

        // 2. HUD
        net.reflact.client.hud.ReflactHud.initialize();
        net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback.EVENT.register(new net.reflact.client.hud.RpgTooltipRenderer());

        // 3. Networking Receiver
        net.reflact.client.network.ClientNetworkManager.init();

        // 4. Keybinds (FIXED for 1.21.11)
        castFireballKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.cast_fireball",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                new KeyBinding.Category(Identifier.of("reflact", "general"))
        ));

        // 5. Input Handling
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (castFireballKey.wasPressed()) {
                net.reflact.client.network.ClientNetworkManager.sendPacket(new CastSpellPacket("fireball"));
            }
        });
    }
}
