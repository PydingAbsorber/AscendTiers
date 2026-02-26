package com.pyding.at.client;

import com.pyding.at.util.ATUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "at", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    private static final ResourceLocation ICON_OPEN = new ResourceLocation("at", "textures/gui/open.png");
    private static final ResourceLocation ICON_CLOSE = new ResourceLocation("at", "textures/gui/close.png");
    private static final ResourceLocation ICON_QUEST = new ResourceLocation("at", "textures/gui/vaprosi.png");

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen || event.getScreen() instanceof CreativeModeInventoryScreen) {
            Player player = Minecraft.getInstance().player;
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) event.getScreen();
            int buttonWidth = 65 * 2;
            int buttonHeight = 12 * 2;
            int x = gui.getGuiLeft() + (gui.getXSize() / 2) - (buttonWidth / 2);
            int y = gui.getGuiTop() + gui.getYSize();
            if (event.getScreen() instanceof CreativeModeInventoryScreen)
                y += gui.getYSize() / 5;
            int buttonSize = 16;
            int toggleX = x + buttonWidth / 2 - buttonSize / 2;
            TiersButton tiers = new TiersButton(x, y + buttonSize, buttonWidth, buttonHeight, player, button -> {});
            tiers.setTooltip(Tooltip.create(Component.translatable("at.power_level", ATUtil.getPowerLevel(player) + " / " + ATUtil.getMaximumPower(player))));
            tiers.visible = !ATUtil.hide;
            ImageButton question = new ImageButton(
                    (int) (x + buttonWidth + buttonSize * 1.2), (y + buttonSize) - buttonSize / 2 + buttonHeight / 2,
                    buttonSize, buttonSize, 0, 0, 0,
                    ICON_QUEST, buttonSize, buttonSize,
                    button -> {}
            );
            question.setTooltip(Tooltip.create(Component.translatable("at.buff.1").withStyle(ChatFormatting.GREEN)
                    .append(Component.translatable("at.buff.2").withStyle(ChatFormatting.GREEN))
                    .append(Component.translatable("at.buff.3").withStyle(ChatFormatting.GREEN))
                    .append(Component.translatable("at.debuff").withStyle(ChatFormatting.RED))
                            .append(Component.translatable("at.debuff.1").withStyle(ChatFormatting.RED))
                            .append(Component.translatable("at.debuff.2").withStyle(ChatFormatting.RED))
                            .append(Component.translatable("at.debuff.3").withStyle(ChatFormatting.RED))
                            .append(Component.translatable("at.debuff.4").withStyle(ChatFormatting.RED))
                            .append(Component.translatable("at.debuff.5").withStyle(ChatFormatting.RED))
                    ));
            question.visible = !ATUtil.hide;
            ImageButton toggleBtn = new ImageButton(
                    toggleX, y, buttonSize, buttonSize, 0, 0, 0,
                    ICON_OPEN, buttonSize, buttonSize,
                    button -> {
                        ATUtil.hide = !ATUtil.hide;
                        tiers.visible = !ATUtil.hide;
                        question.visible = !ATUtil.hide;
                        button.setTooltip(Tooltip.create(Component.translatable(ATUtil.hide ? "at.show" : "at.hide")));
                    }
            ) {
                @Override
                public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    ResourceLocation currentIcon = ATUtil.hide ? ICON_OPEN : ICON_CLOSE;
                    guiGraphics.blit(currentIcon, getX(), getY(), 0, 0, width, height, width, height);
                }
            };
            toggleBtn.setTooltip(Tooltip.create(Component.translatable(ATUtil.hide ? "at.show" : "at.hide")));
            event.addListener(tiers);
            event.addListener(question);
            event.addListener(toggleBtn);
        }
    }
}
