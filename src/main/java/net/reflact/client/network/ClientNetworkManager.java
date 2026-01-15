package net.reflact.client.network;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.reflact.client.managers.ClientItemManager;
import net.reflact.client.network.packet.S2CSyncItemPacket;

public class ClientNetworkManager {
    private static final Gson gson = new Gson();

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ReflactJsonPayload.ID, (payload, context) -> {
            String data = payload.data();
            
            // Parse "ID:JSON"
            int splitIndex = data.indexOf(":");
            if (splitIndex == -1) return;

            String idStr = data.substring(0, splitIndex);
            String json = data.substring(splitIndex + 1);

            int packetId = Integer.parseInt(idStr);
            PacketType type = PacketType.values()[packetId];

            context.client().execute(() -> handlePacket(type, json));
        });
    }

    private static void handlePacket(PacketType type, String json) {
        switch (type) {
            case S2C_SYNC_ITEM:
                S2CSyncItemPacket packet = gson.fromJson(json, S2CSyncItemPacket.class);
                System.out.println("Received RPG Item: " + packet.getItem().getDisplayName());
                ClientItemManager.cacheItem(packet.getItem());
                break;
            default:
                break;
        }
    }
}
