package com.vincychannel.journeymapfix.mixin.client.render.draw;

import com.vincychannel.journeymapfix.access.IJourneyMapClientAccessor;

import journeymap.client.render.draw.DrawEntityStep;
import journeymap.common.Journeymap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawEntityStep.class)
public class MixinDrawEntityStep {

    @Shadow(remap = false) boolean hideSneaks;

    @Inject(method = "<init>(Lnet/minecraft/entity/EntityLivingBase;)V", at = @At("RETURN"))
    private void changeHideSnE(CallbackInfo ci) {
        // First check the settings value got from the server and stored into client
        boolean value = ((IJourneyMapClientAccessor) Journeymap.getClient()).journeymapfix$getHideSneakingEntities();

        // If the server doesn't force the hiding sneaking entities then read the client config value
        if (!value) {
            value = Journeymap.getClient().getCoreProperties().hideSneakingEntities.get();
        }

        this.hideSneaks = value;
    }
}
