package net.reflact.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class WorldMapScreen extends Screen {

    public WorldMapScreen() {
        super(Text.of("World Map"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, "World Map (Placeholder)", width / 2, height / 2, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
