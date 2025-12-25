package com.pyding.at.client;

import com.pyding.at.util.ATUtil;
import net.minecraft.client.Minecraft;
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
    static Button tiers;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen || event.getScreen() instanceof CreativeModeInventoryScreen) {
            Player player = Minecraft.getInstance().player;
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) event.getScreen();
            int buttonWidth = 65*2;
            int buttonHeight = 12*2;
            int x = gui.getGuiLeft() + (gui.getXSize() / 2) - (buttonWidth / 2);
            int y = gui.getGuiTop() + gui.getYSize();
            if(event.getScreen() instanceof CreativeModeInventoryScreen)
                y += gui.getYSize()/5;
            tiers = new TiersButton(
                    x, y,
                    buttonWidth, buttonHeight,
                    player,
                    button -> player.displayClientMessage(Component.literal("Уровень силы: " + ATUtil.getPowerLevel(player)), false)
            );
            tiers.setTooltip(Tooltip.create(Component.translatable("at.power_level", ATUtil.getPowerLevel(player) + " / " + ATUtil.getMaximumPower(player))));
            event.addListener(tiers);
        }
    }
}
