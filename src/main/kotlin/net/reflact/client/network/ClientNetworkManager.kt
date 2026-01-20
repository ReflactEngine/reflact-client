package net.reflact.client.network

import com.google.common.io.ByteStreams
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.reflact.client.ClientData
import net.reflact.client.render.DamageIndicatorRenderer
import net.reflact.common.network.PacketRegistry
import net.reflact.common.network.packet.DamageIndicatorPacket
import net.reflact.common.network.packet.ManaUpdatePacket
import net.reflact.common.network.packet.MapDataPacket
import net.reflact.common.network.packet.ReflactPacket
import net.reflact.common.network.packet.S2CSyncItemPacket
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.DataInputStream

object ClientNetworkManager {
    private val LOGGER = LoggerFactory.getLogger("ReflactNet")

    fun init() {
        ClientPlayNetworking.registerGlobalReceiver(ReflactJsonPayload.ID) { payload, context ->
            // ReflactJsonPayload now contains raw bytes despite the name
            val data = payload.dataBytes

            context.client().execute {
                try {
                    val input = DataInputStream(ByteArrayInputStream(data))
                    val id = input.readUTF()

                    val clazz = PacketRegistry.get(id)
                    if (clazz == null) {
                        LOGGER.warn("Unknown packet ID: {}", id)
                        return@execute
                    }

                    // Instantiate via no-arg constructor
                    val packet = clazz.getDeclaredConstructor().newInstance()
                    packet.decode(input)

                    processPacket(packet)

                } catch (e: Exception) {
                    LOGGER.error("Failed to decode packet", e)
                }
            }
        }
    }

    private fun processPacket(packet: ReflactPacket) {
        if (packet is ManaUpdatePacket) {
            ClientData.currentMana = packet.currentMana
            ClientData.maxMana = packet.maxMana
        } else if (packet is S2CSyncItemPacket) {
            if (packet.item != null) {
                net.reflact.client.managers.ClientItemManager.cacheItem(packet.item!!)
            }
        } else if (packet is MapDataPacket) {
            net.reflact.client.map.MapTextureManager.INSTANCE.setMapData(
                packet.colors,
                packet.width,
                packet.height
            )
        } else if (packet is DamageIndicatorPacket) {
            DamageIndicatorRenderer.add(packet.entityId, packet.amount, packet.isCritical)
        }
    }

    fun sendPacket(packet: ReflactPacket) {
        val id = PacketRegistry.getId(packet.javaClass)
        if (id == null) {
            LOGGER.warn("Tried to send unregistered packet: {}", packet.javaClass.name)
            return
        }

        try {
            val out = ByteStreams.newDataOutput()
            out.writeUTF(id)
            packet.encode(out)

            if (ClientPlayNetworking.canSend(ReflactJsonPayload.ID)) {
                ClientPlayNetworking.send(ReflactJsonPayload(out.toByteArray()))
            }
        } catch (e: Exception) {
            LOGGER.error("Failed to send packet", e)
        }
    }
}
