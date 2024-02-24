package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.VPUtil;
import com.pyding.vp.util.Vector3;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class Anemoculus extends Vestige{
    public Anemoculus(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(1, ChatFormatting.DARK_AQUA, 2, 30, 5, 60, 1, 10, hasDamage);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        if(!isUltimateActive) {
            for (LivingEntity entity : VPUtil.getEntities(player, 8)) {
                VPUtil.liftEntity(entity, VPUtil.commonPower);
            }
            VPUtil.spawnParticles(player, ParticleTypes.CLOUD,8,1,0,0.5,0,3,false);
        }
        else {
            if(Math.random() < 0.5)
                VPUtil.play(player,SoundRegistry.WIND1.get());
            else VPUtil.play(player,SoundRegistry.WIND2.get());
            for(LivingEntity entity: VPUtil.getEntities(player,16)){
                VPUtil.suckEntity(entity,player,2,true);
            }
            VPUtil.spawnParticles(player, ParticleTypes.CLOUD,8,1,0,0.5,0,3,false);
        }
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        player.getAbilities().mayfly = true;
        player.getAbilities().flying = true;
        player.onUpdateAbilities();
        VPUtil.play(player,SoundRegistry.WIND3.get());
        VPUtil.spawnParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE,3,1,0,0.1,0,1,false);
        super.doUltimate(seconds, player, level);
    }

    @Override
    public void ultimateEnds(Player player) {
        if(player.isCreative())
            return;
        player.getAbilities().mayfly = false;
        player.getAbilities().flying = false;
        player.onUpdateAbilities();
        if(player instanceof ServerPlayer serverPlayer)
            PacketHandler.sendToClient(new PlayerFlyPacket(2),serverPlayer);
        super.ultimateEnds(player);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(player.isCreative())
            return;
        player.getAbilities().mayfly = false;
        player.getAbilities().flying = false;
        player.onUpdateAbilities();
        if(player instanceof ServerPlayer serverPlayer)
            PacketHandler.sendToClient(new PlayerFlyPacket(2),serverPlayer);
        super.onUnequip(slotContext, newStack, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        super.curioTick(slotContext, stack);
    }
}
