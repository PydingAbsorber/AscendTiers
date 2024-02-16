package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;

public class Flower extends Vestige{
    public Flower(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(16, ChatFormatting.DARK_GREEN, 2, 10, 1, 30, 5, 30, hasDamage);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        float healRes = 0;
        for(LivingEntity entity: getCreaturesAround(player,30,30,30)){
            healRes += VPUtil.missingHealth(entity)/10;
            if(isStellar)
                entity.getPersistentData().putLong("VPFlowerStellar",System.currentTimeMillis()+1000);
        }
        player.getPersistentData().putFloat("VPHealResFlower",healRes);
        player.getPersistentData().putFloat("VPShieldBonusFlower",healRes*10);
        if(isStellar)
            player.getPersistentData().putLong("VPFlowerStellar",System.currentTimeMillis()+1000);
        super.curioTick(slotContext, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.HEAL2.get());
        player.getPersistentData().putLong("VPDonutSpecial",System.currentTimeMillis()+seconds);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.MAGIC2.get());
        float damage = 0;
        for(ItemStack stack: VPUtil.getAllEquipment(player)){
            if(stack.isDamaged())
                damage += stack.getDamageValue();
        }
        VPUtil.addShield(player,damage,false);
        for(LivingEntity entity: getCreaturesAround(player,30,30,30)){
            VPUtil.addShield(entity,damage,false);
        }
        super.doUltimate(seconds, player, level);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        player.getPersistentData().putFloat("VPHealResFlower",0);
        player.getPersistentData().putFloat("VPShieldBonusFlower",0);
        super.onUnequip(slotContext, newStack, stack);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        player.getPersistentData().putFloat("VPHealResFlower",0);
        player.getPersistentData().putFloat("VPShieldBonusFlower",0);
        super.onEquip(slotContext, prevStack, stack);
    }

    public static List<LivingEntity> getCreaturesAround(Player player, double x, double y, double z){
        List<LivingEntity> list = new ArrayList<>();
        for(LivingEntity entity: player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
            if(entity instanceof Animal || entity instanceof Player)
                list.add(entity);
        }
        return list;
    }
}
