package net.reflact.client.map;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

public class MapTextureManager {

    private static final int MAP_SIZE = 128; // Standard map size
    private final NativeImage nativeImage;
    private final NativeImageBackedTexture dynamicTexture;
    private final Identifier textureId;
    private boolean dirty = false;
    
    // Cache last player position to avoid pointless updates
    private int lastPlayerX = Integer.MAX_VALUE;
    private int lastPlayerZ = Integer.MAX_VALUE;

    public MapTextureManager() {
        this.nativeImage = new NativeImage(NativeImage.Format.RGBA, MAP_SIZE, MAP_SIZE, false);
        // Fill with transparent or background color initially
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                this.nativeImage.setColor(x, y, 0xFF000000); // Black opaque
            }
        }
        
        // NativeImageBackedTexture(NativeImage) might be gone or requires more args
        // Error says: constructor NativeImageBackedTexture(Supplier<String>,NativeImage)
        this.dynamicTexture = new NativeImageBackedTexture(() -> "Reflact Map", this.nativeImage);
        this.textureId = Identifier.of("reflact", "world_map_dynamic");
        MinecraftClient.getInstance().getTextureManager().registerTexture(this.textureId, this.dynamicTexture);
    }

    public void update(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        int playerX = client.player.getBlockX();
        int playerZ = client.player.getBlockZ();
        
        // Simple optimization: only update if moved (or periodically)
        // For now, update always to see it working
        
        int radius = MAP_SIZE / 2;
        
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int z = 0; z < MAP_SIZE; z++) {
                int worldX = playerX - radius + x;
                int worldZ = playerZ - radius + z;
                
                int worldY = client.world.getTopY(net.minecraft.world.Heightmap.Type.MOTION_BLOCKING, worldX, worldZ);
                net.minecraft.util.math.BlockPos pos = new net.minecraft.util.math.BlockPos(worldX, worldY - 1, worldZ);
                net.minecraft.block.BlockState state = client.world.getBlockState(pos);
                
                int color = state.getMapColor(client.world, pos).color;
                
                if (color != 0) {
                     int pixelColor = 0xFF000000 | color;
                     this.nativeImage.setColor(x, z, pixelColor);
                } else {
                     this.nativeImage.setColor(x, z, 0xFF000000);
                }
            }
        }
        
        this.dynamicTexture.upload();
        dirty = false;
    }

    public Identifier getTextureId() {
        return textureId;
    }
    
    public void close() {
        this.dynamicTexture.close();
        // NativeImage is closed by the texture usually, but let's be safe or check doc
        // NativeImageBackedTexture closes the image.
    }
}
