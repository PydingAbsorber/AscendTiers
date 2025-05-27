package com.pyding.at.network.packets;

import com.pyding.at.capability.PlayerCapabilityProviderAT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SendPlayerCapaToClient {
    private final CompoundTag tag;

    public SendPlayerCapaToClient(CompoundTag tag) {
        this.tag = tag;
    }

    public static void encode(SendPlayerCapaToClient msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.tag);
    }

    public static SendPlayerCapaToClient decode(FriendlyByteBuf buf) {
        return new SendPlayerCapaToClient(buf.readNbt());
    }

    public static void handle(SendPlayerCapaToClient msg,Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.tag);
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(CompoundTag tag) {
        LocalPlayer player = Minecraft.getInstance().player;
        player.getCapability(PlayerCapabilityProviderAT.playerCap).ifPresent(cap -> {
            cap.loadNBT(tag);
        });
    }
}
