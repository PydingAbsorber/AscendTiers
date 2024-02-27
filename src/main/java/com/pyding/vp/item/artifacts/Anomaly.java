package com.pyding.vp.item.artifacts;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Random;

public class Anomaly extends Vestige{
    public Anomaly(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(10, ChatFormatting.LIGHT_PURPLE, 2, 60, 1, 360, 30, 1, true);
    }

    public boolean fuckNbt1 = false;
    public boolean fuckNbt2 = false;
    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        if(player.getMainHandItem().getItem() instanceof EnderEyeItem){
            fuckNbt1 = true;
            fuckNbt2 = true;
            ItemStack stackInSlot = VPUtil.getVestigeStack(this,player);
            stackInSlot.getOrCreateTag().putDouble("VPReturnX", player.getX());
            stackInSlot.getOrCreateTag().putDouble("VPReturnY", player.getY());
            stackInSlot.getOrCreateTag().putDouble("VPReturnZ", player.getZ());
            stackInSlot.getOrCreateTag().putString("VPReturnKey", player.getCommandSenderWorld().dimension().location().getPath());
        } else {
            for(LivingEntity entity: VPUtil.ray(player,3,60,true)){
                if(player instanceof ServerPlayer serverPlayer){
                    serverPlayer.teleportTo(entity.getX()-1,entity.getY(),entity.getZ()-1);
                    VPUtil.dealDamage(entity,player,player.damageSources().dragonBreath(),400,2);
                    entity.getPersistentData().putLong("VPAntiTP",seconds+System.currentTimeMillis());
                }
            }
        }
        VPUtil.spawnParticles(player, ParticleTypes.PORTAL,3,1,0,-0.1,0,1,false);
        VPUtil.play(player,SoundRegistry.TELEPORT1.get());
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        if(player instanceof ServerPlayer serverPlayer){
            serverPlayer.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                if(isStellar && (Math.random() < 0.05 || (player.getScoreboardName().equals("Pyding") && player.isCreative()))){  //don't blame me it's for test
                    for(ServerPlayer victim: serverPlayer.getCommandSenderWorld().getServer().getPlayerList().getPlayers()){
                        if(victim != serverPlayer){
                            serverPlayer.teleportTo((ServerLevel) victim.getCommandSenderWorld(),victim.getX(),victim.getY(),victim.getZ(),0,0);
                            break;
                        }
                    }
                }
                else {
                    String key = cap.getRandomDimension();
                    ServerLevel serverLevel = serverPlayer.getCommandSenderWorld().getServer().getLevel(VPUtil.getWorldKey(key));
                    if (serverLevel == null) {
                        serverLevel = serverPlayer.getCommandSenderWorld().getServer().getLevel(Level.OVERWORLD);
                        cap.removeDimension(key);
                    }
                    Random random = new Random();
                    double x;
                    double z;
                    if (Math.random() < 0.5) {
                        x = random.nextInt((int) level.getWorldBorder().getMaxX());
                    } else {
                        x = random.nextInt((int) level.getWorldBorder().getMinX() * -1);
                        x *= -1;
                    }
                    if (Math.random() < 0.5) {
                        z = random.nextInt((int) level.getWorldBorder().getMaxZ());
                    } else {
                        z = random.nextInt((int) level.getWorldBorder().getMinX() * -1);
                        z *= -1;
                    }
                    double y = random.nextInt(260);
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,5*20));
                    serverPlayer.teleportTo(serverLevel, x, y, z, 0, 0);
                    VPUtil.teleportRandomly(serverPlayer,50);
                    VPUtil.spawnParticles(player, ParticleTypes.PORTAL,8,1,0,-0.1,0,1,false);
                    VPUtil.play(player, SoundRegistry.TELEPORT2.get());
                }
            });
        }
        super.doUltimate(seconds, player, level);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if(!fuckNbt1) {
            super.onUnequip(slotContext, newStack, stack);
        }
        else fuckNbt1 = false;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if(!fuckNbt2) {
            super.onEquip(slotContext, prevStack, stack);
        }
        else fuckNbt2 = false;
    }
}
