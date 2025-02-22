package me.windyteam.kura.utils.math;

import me.windyteam.kura.utils.math.deneb.LagCompensator;
import me.windyteam.kura.utils.math.deneb.LagCompensator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.text.DecimalFormat;

/**
 * @author S-B99
 * Created by S-B99 on 18/01/20
 * Updated by S-B99 on 06/02/20
 * <p>
 * Speed:
 * @author S-B99
 * Created by S-B99 on 18/01/20
 * Credit to Seppuku for the following calculation I made more efficient and got inspiration from
 * final String bps = "BPS: " + df.format((MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) / tickRate));
 * <p>
 * Durability:
 * @author TBM
 * Created by TBM on 8/12/19
 * <p>
 * TPS:
 * @author 086
 */
public class InfoCalculator {

    // Speed {
    private static final DecimalFormat formatter = new DecimalFormat("#.#");
    // }
    // }
    private static final float[] tickRates = new float[20];

    // Ping {
    public static int ping(Minecraft mc) {
        if (mc.getConnection() == null) { // tested, this is not null in mp
            return 1;
        } else if (mc.player == null) { // this actually takes about 30 seconds to load in Minecraft
            return -1;
        } else {
            try {
                return mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
            } catch (NullPointerException ignored) {
            }
            return -1;
        }
    }

    public static String speed(boolean useUnitKmH, Minecraft mc) {
        float currentTps = mc.timer.tickLength / 1000.0f;
        double multiply = 1.0;
        if (useUnitKmH) multiply = 3.6; // convert mps to kmh
        return formatter.format(((MathHelper.sqrt(Math.pow(coordsDiff('x', mc), 2) + Math.pow(coordsDiff('z', mc), 2)) / currentTps)) * multiply);
    }

    // }

    private static double coordsDiff(final char c, final Minecraft minecraft) {
        switch (c) {
            case 'x': {
                return minecraft.player.posX - minecraft.player.prevPosX;
            }
            case 'z': {
                return minecraft.player.posZ - minecraft.player.prevPosZ;
            }
            default: {
                return 0.0;
            }
        }
    }
    // }

    // Durability {
    public static int dura(Minecraft mc) {
        ItemStack itemStack = mc.player.getHeldItemMainhand();
        return itemStack.getMaxDamage() - itemStack.getItemDamage();
    }
    // }

    // Memory {
    public static String memory() {
        return "" + (Runtime.getRuntime().freeMemory() / 1000000);
    }

    // Ticks Per Second {
    public static String tps() {
        return "" + Math.round(LagCompensator.INSTANCE.getTickRate());
    }

    public static float getTickRate() {
        float numTicks = 0.0F;
        float sumTickRates = 0.0F;

        for (float tickRate : tickRates) {
            if (tickRate > 0.0F) {
                sumTickRates += tickRate;
                numTicks += 1.0F;
            }
        }
        return MathHelper.clamp(sumTickRates / numTicks, 0.0F, 20.0F);
    }

    // Round {
    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
    // }

    // Is Even {
    public static boolean isNumberEven(int i) {
        return (i & 1) == 0;
    }
    // }

    // Reverse Number {
    public static int reverseNumber(int num, int min, int max) {
        return (max + min) - num;
    }
    // }

    // Cardinal to Axis {
    public static String cardinalToAxis(char cardinal) {
        switch (cardinal) {
            case 'N':
                return "-Z";
            case 'S':
                return "+Z";
            case 'E':
                return "+X";
            case 'W':
                return "-X";
            default:
                return "invalid";
        }
    }
    // }

    // Dimension {
    public static String playerDimension(Minecraft mc) {
        switch (mc.player.dimension) {
            case -1:
                return "Nether";
            case 0:
                return "Overworld";
            case 1:
                return "End";
            default:
                return "No Dimension";
        }
    }
}
