package net.reflact.client.map;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

public class MapTextureManager {

    private static final int MAP_SIZE = 128; // Standard map size
    private final NativeImage nativeImage;
    private NativeImageBackedTexture dynamicTexture; // Remove final
    private final Identifier textureId;
    private boolean dirty = false;
    
    // Singleton instance
    public static final MapTextureManager INSTANCE = new MapTextureManager();

    // Cache last player position to avoid pointless updates
    private int lastPlayerX = Integer.MAX_VALUE;
    private int lastPlayerZ = Integer.MAX_VALUE;
    
    private boolean initialized = false;

    private MapTextureManager() {
        this.nativeImage = new NativeImage(NativeImage.Format.RGBA, MAP_SIZE, MAP_SIZE, false);
        // Fill with transparent or background color initially
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                this.nativeImage.setColor(x, y, 0xFF000000); // Black opaque
            }
        }
        
        // NativeImageBackedTexture(NativeImage) creation moved to init() to avoid early RenderSystem access
        this.textureId = Identifier.of("reflact", "world_map_dynamic");
    }
    
    public void init() {
        if (initialized) return;
        this.dynamicTexture = new NativeImageBackedTexture(() -> "Reflact Map", this.nativeImage);
        MinecraftClient.getInstance().getTextureManager().registerTexture(this.textureId, this.dynamicTexture);
        initialized = true;
    }

    public void update(MinecraftClient client) {
        if (!initialized) init();

        if (client.player == null || client.world == null) return;
        
        int px = client.player.getBlockPos().getX();
        int pz = client.player.getBlockPos().getZ();
        
        int radius = MAP_SIZE / 2;
        
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int z = 0; z < MAP_SIZE; z++) {
                int worldX = px - radius + x;
                int worldZ = pz - radius + z;
                
                int y = client.world.getTopY(net.minecraft.world.Heightmap.Type.WORLD_SURFACE, worldX, worldZ);
                net.minecraft.util.math.BlockPos pos = new net.minecraft.util.math.BlockPos(worldX, y - 1, worldZ);
                net.minecraft.block.BlockState state = client.world.getBlockState(pos);
                
                int color = state.getMapColor(client.world, pos).color;
                
                if (color == 0) {
                    color = 0xFF000000;
                } else {
                    // Convert MapColor (ARGB) to ABGR for NativeImage
                    int r = (color >> 16) & 0xFF;
                    int g = (color >> 8) & 0xFF;
                    int b = color & 0xFF;
                    int a = 0xFF;
                    color = (a << 24) | (b << 16) | (g << 8) | r;
                }
                
                nativeImage.setColor(x, z, color);
            }
        }

        this.dynamicTexture.upload();
    }
    
    public void setMapData(byte[] colors, int width, int height) {
        if (!initialized) init();
        // Assuming colors is RGBA
        // NativeImage expects RGBA? Or ABGR?
        // NativeImage methods usually take int color.
        // We can loop.
        
        if (width != MAP_SIZE || height != MAP_SIZE) {
             // Handle resize or mismatch? 
             // For now assume 128x128 match
        }
        
        for (int i = 0; i < colors.length / 4; i++) {
            int index = i * 4;
            int x = i % width;
            int y = i / width;
            
            // Reconstruct int color
            // If server sent B, G, R, A
            int b = colors[index] & 0xFF;
            int g = colors[index+1] & 0xFF;
            int r = colors[index+2] & 0xFF;
            int a = colors[index+3] & 0xFF;
            
            // NativeImage setPixelColor usually expects ABGR packed int on little endian?
            // Or RGBA? Minecraft uses ABGR internally usually.
            // Let's try 0xAABBGGRR
            int color = (a << 24) | (b << 16) | (g << 8) | r;
            
            if (x < MAP_SIZE && y < MAP_SIZE) {
                this.nativeImage.setColor(x, y, color);
            }
        }
        dirty = true;
    }

    public Identifier getTextureId() {
        return textureId;
    }
    
    public void close() {
        // Do nothing for singleton
    }
}
