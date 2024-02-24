package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Trigon extends Vestige{
    public Trigon(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(19, ChatFormatting.GOLD, 3, 13, 1, 30, 1, 1, true);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player, SoundRegistry.MAGIC5.get());
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.TRIGON2.get());
        super.doUltimate(seconds, player, level);
    }
}
