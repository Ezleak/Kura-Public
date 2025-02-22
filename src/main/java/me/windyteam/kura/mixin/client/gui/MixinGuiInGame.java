package me.windyteam.kura.mixin.client.gui;

import me.windyteam.kura.module.ModuleManager;
import me.windyteam.kura.module.modules.render.Animations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiIngame.class})
public abstract class MixinGuiInGame
extends Gui {
    @Shadow
    @Final
    protected static ResourceLocation WIDGETS_TEX_PATH;
    @Shadow
    @Final
    protected Minecraft mc;

    @Shadow
    protected abstract void renderHotbarItem(int var1, int var2, float var3, EntityPlayer var4, ItemStack var5);

    @Inject(method={"renderHotbar"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderHotbar(ScaledResolution sr, float partialTicks, CallbackInfo ci) {
        if (ModuleManager.getModuleByClass(Animations.class).isEnabled() && ((Boolean)Animations.INSTANCE.hotbar.getValue()).booleanValue()) {
            ci.cancel();
            if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
                float f1;
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                this.mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
                EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
                ItemStack itemstack = entityplayer.getHeldItemOffhand();
                EnumHandSide enumhandside = entityplayer.getPrimaryHand().opposite();
                int i = sr.getScaledWidth() / 2;
                float f = this.zLevel;
                float x = Animations.INSTANCE.updateHotbar();
                this.zLevel = -90.0f;
                this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
                this.drawTexturedModalRect((float)(i - 91 - 1) + x, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
                if (!itemstack.isEmpty()) {
                    if (enumhandside == EnumHandSide.LEFT) {
                        this.drawTexturedModalRect(i - 91 - 29, sr.getScaledHeight() - 23, 24, 22, 29, 24);
                    } else {
                        this.drawTexturedModalRect(i + 91, sr.getScaledHeight() - 23, 53, 22, 29, 24);
                    }
                }
                this.zLevel = f;
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
                RenderHelper.enableGUIStandardItemLighting();
                for (int l = 0; l < 9; ++l) {
                    int i1 = i - 90 + l * 20 + 2;
                    int j1 = sr.getScaledHeight() - 16 - 3;
                    this.renderHotbarItem(i1, j1, partialTicks, entityplayer, (ItemStack)entityplayer.inventory.mainInventory.get(l));
                }
                if (!itemstack.isEmpty()) {
                    int l1 = sr.getScaledHeight() - 16 - 3;
                    if (enumhandside == EnumHandSide.LEFT) {
                        this.renderHotbarItem(i - 91 - 26, l1, partialTicks, entityplayer, itemstack);
                    } else {
                        this.renderHotbarItem(i + 91 + 10, l1, partialTicks, entityplayer, itemstack);
                    }
                }
                if (this.mc.gameSettings.attackIndicator == 2 && (f1 = this.mc.player.getCooledAttackStrength(0.0f)) < 1.0f) {
                    int i2 = sr.getScaledHeight() - 20;
                    int j2 = i + 91 + 6;
                    if (enumhandside == EnumHandSide.RIGHT) {
                        j2 = i - 91 - 22;
                    }
                    this.mc.getTextureManager().bindTexture(Gui.ICONS);
                    int k1 = (int)(f1 * 19.0f);
                    GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    this.drawTexturedModalRect(j2, i2, 0, 94, 18, 18);
                    this.drawTexturedModalRect(j2, i2 + 18 - k1, 18, 112 - k1, 18, k1);
                }
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }
        }
    }
}

