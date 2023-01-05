package me.dyzjct.kura.utils.player

import me.dyzjct.kura.utils.entity.EntityUtil
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer

val mc = Minecraft.getMinecraft()
fun getTarget(range: Int): EntityPlayer? {
    return mc.world.playerEntities.stream().filter { e -> !EntityUtil.isntValid(e, range.toDouble()) }
    .filter { e -> mc.player.getDistance(e) <= range }.min(Comparator.comparing { e -> mc.player.getDistance(e) })
    .orElse(null)
}