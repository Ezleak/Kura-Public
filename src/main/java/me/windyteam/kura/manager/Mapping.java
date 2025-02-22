package me.windyteam.kura.manager;

import net.minecraft.client.Minecraft;

public class Mapping {
    public static final String tickLength;
    public static final String timer;
    public static final String placedBlockDirection;
    public static final String playerPosLookYaw;
    public static final String playerPosLookPitch;
    public static final String isInWeb;
    public static final String cPacketPlayerYaw;
    public static final String cPacketPlayerPitch;
    public static final String renderManagerRenderPosX;
    public static final String renderManagerRenderPosY;
    public static final String renderManagerRenderPosZ;
    public static final String rightClickDelayTimer;
    public static final String sPacketEntityVelocityMotionX;
    public static final String sPacketEntityVelocityMotionY;
    public static final String sPacketEntityVelocityMotionZ;

    static {
        tickLength = (isObfuscated() ? "tickLength" : "tickLength");
        timer = (isObfuscated() ? "timer" : "timer");
        placedBlockDirection = (isObfuscated() ? "placedBlockDirection" : "placedBlockDirection");
        playerPosLookYaw = (isObfuscated() ? "yaw" : "yaw");
        playerPosLookPitch = (isObfuscated() ? "pitch" : "pitch");
        isInWeb = (isObfuscated() ? "isInWeb" : "isInWeb");
        cPacketPlayerYaw = (isObfuscated() ? "yaw" : "yaw");
        cPacketPlayerPitch = (isObfuscated() ? "pitch" : "pitch");
        renderManagerRenderPosX = (isObfuscated() ? "renderPosX" : "renderPosX");
        renderManagerRenderPosY = (isObfuscated() ? "renderPosY" : "renderPosY");
        renderManagerRenderPosZ = (isObfuscated() ? "renderPosZ" : "renderPosZ");
        rightClickDelayTimer = (isObfuscated() ? "rightClickDelayTimer" : "rightClickDelayTimer");
        sPacketEntityVelocityMotionX = (isObfuscated() ? "motionX" : "motionX");
        sPacketEntityVelocityMotionY = (isObfuscated() ? "motionY" : "motionY");
        sPacketEntityVelocityMotionZ = (isObfuscated() ? "motionZ" : "motionZ");
    }

    public static boolean isObfuscated() {
        try {
            return Minecraft.class.getDeclaredField("instance") == null;
        } catch (Exception e) {
            return true;
        }
    }
}
