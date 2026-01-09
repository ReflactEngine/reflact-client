package net.reflact.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.reflact.client.hud.ReflactHud;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class ReflactClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("reflact-client");
    
    // Keybindings
    private static KeyBinding castFireballKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Reflact Client initialized!");

        // HUD
        HudRenderCallback.EVENT.register(new ReflactHud());

        // Networking - Listen for Mana Updates
        ClientPlayNetworking.registerGlobalReceiver(Identifier.of("reflact", "mana"), (client, handler, buf, responseSender) -> {
            int readable = buf.readableBytes();
            if (readable > 0) {
                String data = buf.readCharSequence(readable, StandardCharsets.UTF_8).toString();
                try {
                    double mana = Double.parseDouble(data);
                    client.execute(() -> {
                        ClientData.currentMana = mana;
                    });
                } catch (Exception e) {
                    LOGGER.error("Failed to parse mana", e);
                }
            }
        });

        // Keybinds
        castFireballKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.cast_fireball",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.reflact.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (castFireballKey.wasPressed()) {
                sendCastPacket("fireball");
            }
        });
    }

    private void sendCastPacket(String spellId) {
        if (ClientPlayNetworking.canSend(Identifier.of("reflact", "cast"))) {
            net.minecraft.network.PacketByteBuf buf = net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create();
            buf.writeString(spellId);
            ClientPlayNetworking.send(Identifier.of("reflact", "cast"), buf);
        }
    }
}
