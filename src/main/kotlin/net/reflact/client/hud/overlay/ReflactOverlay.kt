package net.reflact.client.hud.overlay

import net.minecraft.client.gui.DrawContext

interface ReflactOverlay {
    fun render(context: DrawContext, tickDelta: Float)
    
    var x: Int
    var y: Int
    
    // Properties for resize/bounds
    var width: Int
        get() = 0 // Default impl
        set(value) {} // Default impl
        
    var height: Int
        get() = 0 // Default impl
        set(value) {} // Default impl

    val isEnabled: Boolean
    val name: String
}
