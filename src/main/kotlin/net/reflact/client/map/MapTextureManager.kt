package net.reflact.client.map

import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import net.minecraft.world.Heightmap

class MapTextureManager private constructor() {

    private val nativeImage: NativeImage = NativeImage(NativeImage.Format.RGBA, MAP_SIZE, MAP_SIZE, false)
    private lateinit var dynamicTexture: NativeImageBackedTexture
    val textureId: Identifier = Identifier.of("reflact", "world_map_dynamic")
    private var dirty = false
    private var initialized = false

    private var lastServerUpdate: Long = 0

    init {
        // Fill with transparent or background color initially
        for (x in 0 until MAP_SIZE) {
            for (y in 0 until MAP_SIZE) {
                this.nativeImage.setColor(x, y, 0xFF000000.toInt()) // Black opaque
            }
        }
    }

    fun init() {
        if (initialized) return
        this.dynamicTexture = NativeImageBackedTexture({ "Reflact Map" }, this.nativeImage)
        MinecraftClient.getInstance().textureManager.registerTexture(this.textureId, this.dynamicTexture)
        initialized = true
    }

    fun update(client: MinecraftClient) {
        if (!initialized) init()

        // If we received server map data recently (within 6 seconds), don't overwrite with client scan
        if (System.currentTimeMillis() - lastServerUpdate < 6000) {
            this.dynamicTexture.upload()
            return
        }

        if (client.player == null || client.world == null) return

        val px = client.player!!.blockPos.x
        val pz = client.player!!.blockPos.z

        val radius = MAP_SIZE / 2

        for (x in 0 until MAP_SIZE) {
            for (z in 0 until MAP_SIZE) {
                val worldX = px - radius + x
                val worldZ = pz - radius + z

                val y = client.world!!.getTopY(Heightmap.Type.WORLD_SURFACE, worldX, worldZ)
                val pos = net.minecraft.util.math.BlockPos(worldX, y - 1, worldZ)
                val state = client.world!!.getBlockState(pos)

                val color = state.getMapColor(client.world, pos).color

                if (color == 0) {
                    // Void / Black - Use transparent or dark gray
                    nativeImage.setColor(x, z, 0xFF000000.toInt())
                } else {
                    // MapColor.color is usually integer ARGB.
                    // NativeImage expects ABGR (Little Endian).
                    // We need to swap Red and Blue.

                    val alpha = 255 // Force opaque
                    val red = (color shr 16) and 0xFF
                    val green = (color shr 8) and 0xFF
                    val blue = color and 0xFF

                    // ABGR
                    val abgr = (alpha shl 24) or (blue shl 16) or (green shl 8) or red
                    nativeImage.setColor(x, z, abgr)
                }
            }
        }

        this.dynamicTexture.upload()
    }

    fun setMapData(colors: ByteArray, width: Int, height: Int) {
        if (!initialized) init()
        this.lastServerUpdate = System.currentTimeMillis()

        if (width != MAP_SIZE || height != MAP_SIZE) {
            // Handle resize or mismatch?
            // For now assume 128x128 match
        }

        for (i in 0 until colors.size / 4) {
            val index = i * 4
            val x = i % width
            val y = i / width

            // Reconstruct int color
            // If server sent B, G, R, A
            val b = colors[index].toInt() and 0xFF
            val g = colors[index + 1].toInt() and 0xFF
            val r = colors[index + 2].toInt() and 0xFF
            val a = colors[index + 3].toInt() and 0xFF

            // NativeImage setPixelColor usually expects ABGR packed int on little endian?
            // Let's try 0xAABBGGRR
            val color = (a shl 24) or (b shl 16) or (g shl 8) or r

            if (x < MAP_SIZE && y < MAP_SIZE) {
                this.nativeImage.setColor(x, y, color)
            }
        }
        dirty = true
    }

    fun close() {
        // Do nothing for singleton
    }

    companion object {
        const val MAP_SIZE = 128 // Standard map size
        val INSTANCE = MapTextureManager()
    }
}
