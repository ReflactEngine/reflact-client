package net.reflact.client.network;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.reflact.client.ClientData;
import net.reflact.client.network.packet.CastSpellPacket;
import net.reflact.client.network.packet.ManaUpdatePacket;
import net.reflact.client.network.packet.ReflactPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ClientNetworkManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("ReflactNet");
    private static final Gson gson = new Gson();
    private static final Map<String, Class<? extends ReflactPacket>> registry = new HashMap<>();

    public static void init() {
        register("mana_update", ManaUpdatePacket.class);
        register("sync_item", net.reflact.client.network.packet.S2CSyncItemPacket.class);
        
        ClientPlayNetworking.registerGlobalReceiver(ReflactJsonPayload.ID, (payload, context) -> {
            String data = payload.data();
            int splitIndex = data.indexOf(":");
            if (splitIndex == -1) return;
            
            String id = data.substring(0, splitIndex);
            String json = data.substring(splitIndex + 1);
            
            context.client().execute(() -> handlePacket(id, json));
        });
    }

    public static void register(String id, Class<? extends ReflactPacket> clazz) {
        registry.put(id, clazz);
    }

    private static void handlePacket(String id, String json) {
        Class<? extends ReflactPacket> clazz = registry.get(id);
        if (clazz == null) {
            LOGGER.warn("Unknown packet ID: {}", id);
            return;
        }
        
        try {
            ReflactPacket packet = gson.fromJson(json, clazz);
            processPacket(packet);
        } catch (Exception e) {
            LOGGER.error("Failed to process packet {}: {}", id, e.getMessage());
        }
    }

    private static void processPacket(ReflactPacket packet) {
        if (packet instanceof ManaUpdatePacket manaPacket) {
            ClientData.currentMana = manaPacket.getMana();
        }
        // Add more handlers here
    }

    public static void sendPacket(ReflactPacket packet) {
        String data = packet.getPacketId() + ":" + packet.toJson();
        if (ClientPlayNetworking.canSend(ReflactJsonPayload.ID)) {
            ClientPlayNetworking.send(new ReflactJsonPayload(data));
        }
    }
}