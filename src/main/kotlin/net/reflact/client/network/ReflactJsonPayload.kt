package net.reflact.client.network

import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

data class ReflactJsonPayload(val dataBytes: ByteArray) : CustomPayload {

    companion object {
        val ID = CustomPayload.Id<ReflactJsonPayload>(Identifier.of("reflact", "main"))
        val CODEC: PacketCodec<RegistryByteBuf, ReflactJsonPayload> = object : PacketCodec<RegistryByteBuf, ReflactJsonPayload> {
            override fun decode(buf: RegistryByteBuf): ReflactJsonPayload {
                val bytes = ByteArray(buf.readableBytes())
                buf.readBytes(bytes)
                return ReflactJsonPayload(bytes)
            }

            override fun encode(buf: RegistryByteBuf, value: ReflactJsonPayload) {
                buf.writeBytes(value.dataBytes)
            }
        }
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return ID
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReflactJsonPayload

        return dataBytes.contentEquals(other.dataBytes)
    }

    override fun hashCode(): Int {
        return dataBytes.contentHashCode()
    }
}
