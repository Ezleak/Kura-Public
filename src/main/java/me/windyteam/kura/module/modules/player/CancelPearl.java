package me.windyteam.kura.module.modules.player;

import me.windyteam.kura.event.events.client.PacketEvents;
import me.windyteam.kura.event.events.entity.MotionUpdateEvent;
import me.windyteam.kura.module.Category;
import me.windyteam.kura.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


@Module.Info(name = "CancelPearl",category = Category.PLAYER)
public class CancelPearl extends Module {
    private final Queue<CPacketPlayer> packets = new ConcurrentLinkedQueue<>();
    int thrownPearlId = -1;
    @SubscribeEvent
    public void onTick(MotionUpdateEvent.Tick event) {
        if (nullCheck()) return;

        if (thrownPearlId != -1) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity.getEntityId() == thrownPearlId && entity instanceof EntityEnderPearl) {
                    EntityEnderPearl pearl = (EntityEnderPearl) entity;
                    if (pearl.isDead) {
                        thrownPearlId = -1;
                    }
                }
            }

        } else {
            if (!packets.isEmpty()) {
                do {
                    mc.player.connection.sendPacket(packets.poll());
                } while (!packets.isEmpty());
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvents.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            SPacketSpawnObject packet = event.getPacket();

            if (packet.getType() == 65) {
                mc.world.playerEntities.stream().min(Comparator.comparingDouble((p) -> p.getDistance(packet.getX(), packet.getY(), packet.getZ()))).ifPresent((player) -> {

                    if (player.equals(mc.player)) {
                        if (!mc.player.onGround) return;

                        mc.player.motionX = 0.0;
                        mc.player.motionY = 0.0;
                        mc.player.motionZ = 0.0;
                        mc.player.movementInput.moveForward = 0.0f;
                        mc.player.movementInput.moveStrafe = 0.0f;
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ, false));
                        thrownPearlId = packet.getEntityID();
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvents.Send event) {
        if (thrownPearlId != -1 && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = event.getPacket();
            packets.add(packet);
            event.setCanceled(true);
        }
    }
}
