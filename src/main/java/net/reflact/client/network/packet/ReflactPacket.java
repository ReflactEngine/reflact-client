package net.reflact.client.network.packet;

import com.google.gson.Gson;

/**
 * Client-side mirror of ReflactPacket.
 */
public abstract class ReflactPacket {
    protected static final Gson gson = new Gson();

    public abstract String getPacketId();

    public String toJson() {
        return gson.toJson(this);
    }
}
