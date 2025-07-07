package com.vincychannel.journeymapfix.mixin.client.render.draw;

import com.vincychannel.journeymapfix.access.IJourneyMapClientAccessor;

import journeymap.client.render.draw.DrawEntityStep;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.map.GridRenderer;
import journeymap.common.Journeymap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.geom.Point2D;
import java.lang.ref.WeakReference;

@Mixin(DrawEntityStep.class)
public abstract class MixinDrawEntityStep {

    @Unique boolean hideInvisible;

    @Shadow(remap = false) boolean hideSneaks;

    @Shadow(remap = false) WeakReference<EntityLivingBase> entityLivingRef;

    @Shadow(remap = false) Point2D screenPosition;

    @Shadow(remap = false) Minecraft minecraft;

    @Shadow(remap = false)
    protected abstract void drawPlayer(DrawStep.Pass pass, double drawX, double drawY, GridRenderer gridRenderer, float alpha, double heading, double fontScale, double rotation);

    @Shadow(remap = false)
    protected abstract void drawCreature(DrawStep.Pass pass, double drawX, double drawY, GridRenderer gridRenderer, float alpha, double heading, double fontScale, double rotation);


    @Inject(method = "<init>(Lnet/minecraft/entity/EntityLivingBase;)V", at = @At("RETURN"))
    private void changeHideSnE(CallbackInfo ci) {
        // First check the settings isHideSneaksEnabled got from the server and stored into client
        boolean isHideSneaksEnabled = ((IJourneyMapClientAccessor) Journeymap.getClient()).journeymapfix$getHideSneakingEntities();
        boolean isHideInvEnabled = ((IJourneyMapClientAccessor) Journeymap.getClient()).journeymapfix$getHideInvisiblePlayers();

        // If the server doesn't force the hiding sneaking entities then read the client config isHideSneaksEnabled
        if (!isHideSneaksEnabled) {
            isHideSneaksEnabled = Journeymap.getClient().getCoreProperties().hideSneakingEntities.get();
        }

        this.hideSneaks = isHideSneaksEnabled;
        this.hideInvisible = isHideInvEnabled;
    }

    /**
     * @author VincyChannel
     * @reason Added the check for Invisible Players
     */
    @Overwrite(remap = false)
    public void draw(DrawStep.Pass pass, double xOffset, double yOffset, GridRenderer gridRenderer, double fontScale, double rotation) {
        if (pass != DrawStep.Pass.Tooltip) {
            EntityLivingBase entityLiving = (EntityLivingBase)this.entityLivingRef.get();
            if (pass == DrawStep.Pass.Object) {
                if (entityLiving == null || entityLiving.isDead || entityLiving.isInvisibleToPlayer(this.minecraft.player) || !entityLiving.addedToChunk || this.hideSneaks && entityLiving.isSneaking()
                        || this.hideInvisible && (entityLiving.isInvisible() || entityLiving instanceof EntityPlayer && this.minecraft.world.getPlayerEntityByName(entityLiving.getName()).isInvisible())) {
                    this.screenPosition = null;
                    return;
                }

                this.screenPosition = gridRenderer.getPixel(entityLiving.posX, entityLiving.posZ);
            }

            if (this.screenPosition != null) {
                double heading = (double)entityLiving.rotationYawHead;
                double drawX = this.screenPosition.getX() + xOffset;
                double drawY = this.screenPosition.getY() + yOffset;
                float alpha = 1.0F;
                if (entityLiving.posY > this.minecraft.player.posY) {
                    alpha = 1.0F - Math.max(0.1F, (float)((entityLiving.posY - this.minecraft.player.posY) / (double)32.0F));
                }

                if (entityLiving instanceof EntityPlayer) {
                    this.drawPlayer(pass, drawX, drawY, gridRenderer, alpha, heading, fontScale, rotation);
                } else {
                    this.drawCreature(pass, drawX, drawY, gridRenderer, alpha, heading, fontScale, rotation);
                }
            }
        }
    }
}
