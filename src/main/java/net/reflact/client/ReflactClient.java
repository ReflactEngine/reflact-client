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
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflactClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("reflact-client");
    
    // Keybindings
    private static KeyBinding castFireballKey;

    // Define Custom Payloads (Networking 1.21+ API)
    public record ManaPayload(double mana) implements CustomPayload {
        public static final CustomPayload.Id<ManaPayload> ID = new CustomPayload.Id<>(Identifier.of("reflact", "mana"));
        public static final PacketCodec<RegistryByteBuf, ManaPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.DOUBLE, ManaPayload::mana,
            ManaPayload::new
        );
        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
    }

    public record CastPayload(String spellId) implements CustomPayload {
        public static final CustomPayload.Id<CastPayload> ID = new CustomPayload.Id<>(Identifier.of("reflact", "cast"));
        public static final PacketCodec<RegistryByteBuf, CastPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, CastPayload::spellId,
            CastPayload::new
        );
        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Reflact Client initialized!");

        // 1. Register Payload Types
        PayloadTypeRegistry.playS2C().register(ManaPayload.ID, ManaPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CastPayload.ID, CastPayload.CODEC);

        // 2. HUD
        HudRenderCallback.EVENT.register(new ReflactHud());

        // 3. Networking Receiver
        ClientPlayNetworking.registerGlobalReceiver(ManaPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                ClientData.currentMana = payload.mana();
            });
        });

        // 4. Keybinds (FIXED for 1.21.11)
        // Category now requires an Identifier object, e.g. "reflact:general"
        castFireballKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.cast_fireball",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                new KeyBinding.Category(Identifier.of("reflact", "general")) // Fixed here
        ));

        // 5. Input Handling
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (castFireballKey.wasPressed()) {
                if (ClientPlayNetworking.canSend(CastPayload.ID)) {
                    ClientPlayNetworking.send(new CastPayload("fireball"));
                }
            }
        });
    }
}
