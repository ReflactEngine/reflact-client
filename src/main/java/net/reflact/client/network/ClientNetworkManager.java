package net.reflact.client.network;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.reflact.client.ClientData;
import net.reflact.common.network.PacketRegistry;
import net.reflact.common.network.packet.CastSpellPacket;
import net.reflact.common.network.packet.ManaUpdatePacket;
import net.reflact.common.network.packet.ReflactPacket;
import net.reflact.common.network.packet.S2CSyncItemPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientNetworkManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("ReflactNet");
    private static final Gson gson = new Gson();

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ReflactJsonPayload.ID, (payload, context) -> {
            String data = payload.data();
            int splitIndex = data.indexOf(":");
            if (splitIndex == -1) return;
            
            String id = data.substring(0, splitIndex);
            String json = data.substring(splitIndex + 1);
            
            context.client().execute(() -> handlePacket(id, json));
        });
    }

    private static void handlePacket(String id, String json) {
        Class<? extends ReflactPacket> clazz = PacketRegistry.get(id);
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
            ClientData.currentMana = manaPacket.currentMana();
        } else if (packet instanceof S2CSyncItemPacket syncPacket) {
            net.reflact.client.managers.ClientItemManager.cacheItem(syncPacket.item());
        }
    }

    public static void sendPacket(ReflactPacket packet) {
        String id = PacketRegistry.getId(packet.getClass());
        if (id == null) {
            LOGGER.warn("Tried to send unregistered packet: {}", packet.getClass().getName());
            return;
        }
        String data = id + ":" + gson.toJson(packet);
        if (ClientPlayNetworking.canSend(ReflactJsonPayload.ID)) {
            ClientPlayNetworking.send(new ReflactJsonPayload(data));
        }
    }
}
