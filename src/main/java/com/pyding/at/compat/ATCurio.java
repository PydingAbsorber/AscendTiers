package com.pyding.at.compat;

import com.pyding.at.util.ATUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

public class ATCurio {

    public static List<ItemStack> getCurioList(Player player){
        List<ItemStack> stacks = new ArrayList<>();
        CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent(handler -> {
            for (int index = 0; index < handler.getSlots(); index++) {
                if (handler.getStackInSlot(index) != null && !handler.getStackInSlot(index).isEmpty()) {
                    stacks.add(handler.getStackInSlot(index));
                }
            }
        });
        return stacks;
    }

    public static void dropCurios(Player player, int tier){
        CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent(handler -> {
            for (int index = 0; index < handler.getSlots(); index++) {
                if (handler.getStackInSlot(index) != null && !handler.getStackInSlot(index).isEmpty()) {
                    ItemStack stack = handler.getStackInSlot(index);
                    if (ATUtil.getTier(stack) > tier && ATUtil.notIgnored(stack)) {
                        player.drop(stack, true);
                        stack.setCount(0);
                    }
                }
            }
        });
    }

}
