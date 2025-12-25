package com.pyding.at.client;

import com.pyding.at.util.ATUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class TiersButton extends ImageButton {
    private final Player player;

    public TiersButton(int x, int y, int width, int height, Player player, OnPress onPress) {
        super(x, y, width, height, 0, 0, 0, new ResourceLocation("at", "textures/gui/bar0.png"), width, height, onPress);
        this.player = player;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int barIndex = 0;
        if (player != null) {
            int power = ATUtil.getPowerLevel(player);
            int maxPower = ATUtil.getMaximumPower(player);
            if (power > 0 && maxPower > 0) {
                barIndex = (int)((float) power / maxPower * 10);
                barIndex = Math.max(0, Math.min(10, barIndex));
            }
        }
        ResourceLocation dynamicTexture = new ResourceLocation("at", "textures/gui/bar" + barIndex + ".png");
        guiGraphics.blit(dynamicTexture, getX(), getY(), 0, 0, width, height, width, height);
        setTooltip(Tooltip.create(Component.translatable("at.power_level", ATUtil.getPowerLevel(player) + " / " + ATUtil.getMaximumPower(player))));
    }
}
