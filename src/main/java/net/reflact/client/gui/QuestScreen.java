package net.reflact.client.gui;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class QuestScreen extends BaseOwoScreen<FlowLayout> {

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, (horizontal, vertical) -> new FlowLayout(horizontal, vertical, FlowLayout.Algorithm.VERTICAL) {});
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        rootComponent.child(
                new LabelComponent(Text.of("Quest Log")) {}
                        .shadow(true)
                        .margins(Insets.top(10))
        );
        
        // This would be populated dynamically based on packet data
        // For now, hardcoded example
        rootComponent.child(createQuestEntry("Welcome to Reflact", "Talk to the Guide", true));
        rootComponent.child(createQuestEntry("Wolf Slayer", "Kill 10 Wolves", false));
    }
    
    private FlowLayout createQuestEntry(String title, String objective, boolean active) {
        FlowLayout container = new FlowLayout(Sizing.fixed(200), Sizing.content(), FlowLayout.Algorithm.VERTICAL) {};
        container.padding(Insets.of(5));
        container.surface(Surface.DARK_PANEL);
        container.margins(Insets.bottom(5));
        
        container.child(new LabelComponent(Text.of(title)) {}.color(active ? Color.ofRgb(0xFFFF55) : Color.ofRgb(0xAAAAAA)));
        container.child(new LabelComponent(Text.of(objective)) {}.color(Color.ofRgb(0xFFFFFF)));
        
        return container;
    }
}
