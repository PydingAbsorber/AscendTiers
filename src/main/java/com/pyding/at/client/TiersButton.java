package com.pyding.at.client;

import com.pyding.at.capability.PlayerCapabilityProviderAT;
import com.pyding.at.util.ATUtil;
import net.minecraft.ChatFormatting;
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
        if (player == null)
            return;
        int barIndex = 0;
        int power = ATUtil.getPowerLevel(player);
        int maxPower = ATUtil.getMaximumPower(player);
        if (power > 0 && maxPower > 0) {
            barIndex = (int)((float) power / maxPower * 10);
            barIndex = Math.max(0, Math.min(10, barIndex));
        }
        ResourceLocation dynamicTexture = new ResourceLocation("at", "textures/gui/bar" + barIndex + ".png");
        guiGraphics.blit(dynamicTexture, getX(), getY(), 0, 0, width, height, width, height);
        ChatFormatting color;
        if(power >= maxPower*0.9)
            color = ChatFormatting.RED;
        else if(power >= maxPower*0.5)
            color = ChatFormatting.YELLOW;
        else color = ChatFormatting.GREEN;
        int tier = ATUtil.getTier(player);
        Component component;
        if(power > maxPower && (maxPower/100) > 0)
            component = Component.translatable("at.exceed",(power-maxPower)/(maxPower/100) + "%");
        else component = Component.empty();
        player.getCapability(PlayerCapabilityProviderAT.playerCap).ifPresent(cap -> {
            setTooltip(Tooltip.create(Component.translatable("at.power_level", power + " / " + maxPower + "\n").withStyle(color)
                    .append(Component.translatable("at.tier", ATUtil.convertToRoman(tier) + "\n").withStyle(ATUtil.getColor(tier)))
                    .append(Component.translatable("at.tier.2", cap.getExp(), ATUtil.getExpNext(cap.getTier(player))).withStyle(ATUtil.getColor(tier)))
                    .append(component)));
        });
    }
}
