package net.reflact.client.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ReflactJsonPayload(String data) implements CustomPayload {
    public static final CustomPayload.Id<ReflactJsonPayload> ID = new CustomPayload.Id<>(Identifier.of("reflact", "main"));
    public static final PacketCodec<RegistryByteBuf, ReflactJsonPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, ReflactJsonPayload::data,
        ReflactJsonPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
