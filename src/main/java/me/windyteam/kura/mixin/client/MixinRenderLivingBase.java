package me.windyteam.kura.mixin.client;

import me.windyteam.kura.event.events.render.RenderPlayerTagsEvent;
import me.windyteam.kura.event.events.render.entity.RenderEntityModelEvents;
import me.windyteam.kura.friend.FriendManager;
import me.windyteam.kura.module.modules.render.Wireframe;
import me.windyteam.kura.utils.color.ColorUtil;
import me.windyteam.kura.module.modules.render.Wireframe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderLivingBase.class})
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T> {
    @Final
    @Shadow
    private static final Logger LOGGER = LogManager.getLogger();

    @Shadow
    protected ModelBase mainModel;
    @Shadow
    protected boolean renderMarker;
    float red;
    float green;
    float blue;

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(renderManager);
        this.red = 0.0f;
        this.green = 0.0f;
        this.blue = 0.0f;
    }

    @Shadow
    protected abstract boolean isVisible(EntityLivingBase p0);

    @Shadow
    protected abstract float getSwingProgress(T p0, float p1);

    @Shadow
    protected abstract float interpolateRotation(float p0, float p1, float p2);

    @Shadow
    protected abstract float handleRotationFloat(T p0, float p1);

    @Shadow
    protected abstract void applyRotations(T p0, float p1, float p2, float p3);

    @Shadow
    public abstract float prepareScale(T p0, float p1);

    @Shadow
    protected abstract void unsetScoreTeamColor();

    @Shadow
    protected abstract boolean setScoreTeamColor(T p0);

    @Shadow
    protected abstract void renderLivingAt(T p0, double p1, double p2, double p3);

    @Shadow
    protected abstract void unsetBrightness();

    @Shadow
    protected abstract void renderModel(T p0, float p1, float p2, float p3, float p4, float p5, float p6);

    @Shadow
    protected abstract void renderLayers(T p0, float p1, float p2, float p3, float p4, float p5, float p6, float p7);

    @Shadow
    protected abstract boolean setDoRenderBrightness(T p0, float p1);

    /**
     * @author ???
     * @reason ???
     */
    @Overwrite
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(entity, RenderLivingBase.class.cast(this), partialTicks, x, y, z))) {
            GlStateManager.pushMatrix();
            GL11.glShadeModel(GL11.GL_FLAT);
            GlStateManager.disableCull();
            this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
            boolean shouldSit = entity.isRiding() && entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit();
            this.mainModel.isRiding = shouldSit;
            this.mainModel.isChild = entity.isChild();
            try {
                float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
                float f2 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
                float f3 = f2 - f;
                if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
                    f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    f3 = f2 - f;
                    float f4 = MathHelper.wrapDegrees(f3);
                    if (f4 < -85.0f) {
                        f4 = -85.0f;
                    }
                    if (f4 >= 85.0f) {
                        f4 = 85.0f;
                    }
                    f = f2 - f4;
                    if (f4 * f4 > 2500.0f) {
                        f += f4 * 0.2f;
                    }
                    f3 = f2 - f;
                }
                float f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                this.renderLivingAt(entity, x, y, z);
                float f4 = this.handleRotationFloat(entity, partialTicks);
                this.applyRotations(entity, f4, f, partialTicks);
                float f6 = this.prepareScale(entity, partialTicks);
                float f7 = 0.0f;
                float f8 = 0.0f;
                if (!entity.isRiding()) {
                    f7 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                    f8 = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);
                    if (entity.isChild()) {
                        f8 *= 3.0f;
                    }
                    if (f7 > 1.0f) {
                        f7 = 1.0f;
                    }
                    f3 = f2 - f;
                }
                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(entity, f8, f7, partialTicks);
                this.mainModel.setRotationAngles(f8, f7, f4, f3, f5, f6, entity);
                if (this.renderOutlines) {
                    boolean flag1 = this.setScoreTeamColor(entity);
                    GlStateManager.enableColorMaterial();
                    GlStateManager.enableOutlineMode(this.getTeamColor(entity));
                    if (!this.renderMarker) {
                        this.renderModel(entity, f8, f7, f4, f3, f5, f6);
                    }
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
                        this.renderLayers(entity, f8, f7, partialTicks, f4, f3, f5, f6);
                    }
                    GlStateManager.disableOutlineMode();
                    GlStateManager.disableColorMaterial();
                    if (flag1) {
                        this.unsetScoreTeamColor();
                    }
                } else {
                    if (Wireframe.getINSTANCE().isEnabled() && Wireframe.getINSTANCE().players.getValue() && entity instanceof EntityPlayer && Wireframe.getINSTANCE().mode.getValue().equals(Wireframe.RenderMode.SOLID)) {
                        this.red = Wireframe.getInstance().red.getValue() / 255.0f;
                        this.green = Wireframe.getInstance().green.getValue() / 255.0f;
                        this.blue = Wireframe.getInstance().blue.getValue() / 255.0f;
                        GlStateManager.pushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        GL11.glDisable(2929);
                        GL11.glDepthMask(false);
                        if (FriendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Wireframe.getINSTANCE().alpha.getValue() / 255.0f);
                        } else {
                            GL11.glColor4f((Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getRed() / 255.0f) : this.red, (Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getGreen() / 255.0f) : this.green, (Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getBlue() / 255.0f) : this.blue, Wireframe.getINSTANCE().alpha.getValue() / 255.0f);
                        }
                        this.renderModel(entity, f8, f7, f4, f3, f5, f6);
                        GL11.glDisable(2896);
                        GL11.glEnable(2929);
                        GL11.glDepthMask(true);
                        if (FriendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Wireframe.getINSTANCE().alpha.getValue() / 255.0f);
                        } else {
                            GL11.glColor4f((Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getRed() / 255.0f) : this.red, (Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getGreen() / 255.0f) : this.green, (Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getBlue() / 255.0f) : this.blue, Wireframe.getINSTANCE().alpha.getValue() / 255.0f);
                        }
                        this.renderModel(entity, f8, f7, f4, f3, f5, f6);
                        GL11.glEnable(2896);
                        GlStateManager.popAttrib();
                        GlStateManager.popMatrix();
                    }
                    boolean flag1 = this.setDoRenderBrightness(entity, partialTicks);
                    if (!(entity instanceof EntityPlayer) || (Wireframe.getINSTANCE().isEnabled() && Wireframe.getINSTANCE().mode.getValue().equals(Wireframe.RenderMode.WIREFRAME) && Wireframe.getINSTANCE().playerModel.getValue()) || Wireframe.getINSTANCE().isDisabled()) {
                        this.renderModel(entity, f8, f7, f4, f3, f5, f6);
                    }
                    if (flag1) {
                        this.unsetBrightness();
                    }
                    GlStateManager.depthMask(true);
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
                        this.renderLayers(entity, f8, f7, partialTicks, f4, f3, f5, f6);
                    }
                    if (Wireframe.getINSTANCE().isEnabled() && Wireframe.getINSTANCE().players.getValue() && entity instanceof EntityPlayer && Wireframe.getINSTANCE().mode.getValue().equals(Wireframe.RenderMode.WIREFRAME)) {
                        this.red = Wireframe.getInstance().red.getValue() / 255.0f;
                        this.green = Wireframe.getInstance().green.getValue() / 255.0f;
                        this.blue = Wireframe.getInstance().blue.getValue() / 255.0f;
                        GlStateManager.pushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        if (FriendManager.isFriend(entity.getName()) || entity == Minecraft.getMinecraft().player) {
                            GL11.glColor4f(0.0f, 191.0f, 255.0f, Wireframe.getINSTANCE().alpha.getValue() / 255.0f);
                        } else {
                            GL11.glColor4f((Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getRed() / 255.0f) : this.red, (Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getGreen() / 255.0f) : this.green, (Wireframe.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(Wireframe.getInstance().rainbowHue.getValue()).getBlue() / 255.0f) : this.blue, Wireframe.getINSTANCE().alpha.getValue() / 255.0f);
                        }
                        GL11.glLineWidth(Wireframe.getINSTANCE().lineWidth.getValue());
                        this.renderModel(entity, f8, f7, f4, f3, f5, f6);
                        GL11.glEnable(2896);
                        GlStateManager.popAttrib();
                        GlStateManager.popMatrix();
                    }
                }
                GlStateManager.disableRescaleNormal();
            } catch (Exception var20) {
                MixinRenderLivingBase.LOGGER.error("Couldn't render entity", var20);
            }
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(entity, RenderLivingBase.class.cast(this), partialTicks, x, y, z));
        }
    }

    @Inject(method = "renderName", at = @At(value = "HEAD"), cancellable = true)
    public void obRenderNamePre(T entity, double x, double y, double z, CallbackInfo ci) {
        if (entity instanceof EntityPlayer) {
            RenderPlayerTagsEvent event = new RenderPlayerTagsEvent();
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled()) ci.cancel();
        }
    }

    @Redirect(method = {"renderModel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderModelWrapper(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        RenderEntityModelEvents preEvent = new RenderEntityModelEvents(0, modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        MinecraftForge.EVENT_BUS.post(preEvent);
        if (preEvent.isCanceled()) {
            return;
        }
        modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        RenderEntityModelEvents postEvent = new RenderEntityModelEvents(1, modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        MinecraftForge.EVENT_BUS.post(postEvent);
    }
}

