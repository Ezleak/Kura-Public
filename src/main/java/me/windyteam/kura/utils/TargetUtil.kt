package me.windyteam.kura.utils

import me.windyteam.kura.utils.entity.EntityUtil
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer

private val mc = Minecraft.getMinecraft()
fun getTarget(range: Int): EntityPlayer? {
    return mc.world.playerEntities.stream().filter { e -> !EntityUtil.isntValid(e, range.toDouble()) }
        .filter { e -> mc.player.getDistance(e) <= range }.min(Comparator.comparing { e -> mc.player.getDistance(e) })
        .orElse(null)
}
fun getTarget(range: Double): EntityPlayer? {
    return mc.world.playerEntities.stream().filter { e -> !EntityUtil.isntValid(e, range) }
        .filter { e -> mc.player.getDistance(e) <= range }.min(Comparator.comparing { e -> mc.player.getDistance(e) })
        .orElse(null)
}

fun getTarget(range: Float): EntityPlayer? {
    return mc.world.playerEntities.stream().filter { e -> !EntityUtil.isntValid(e, range.toDouble()) }
        .filter { e -> mc.player.getDistance(e) <= range }.min(Comparator.comparing { e -> mc.player.getDistance(e) })
        .orElse(null)
}