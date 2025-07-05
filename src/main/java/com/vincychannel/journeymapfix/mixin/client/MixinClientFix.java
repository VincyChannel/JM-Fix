package com.vincychannel.journeymapfix.mixin.client;

import com.vincychannel.journeymapfix.access.IJourneyMapClientAccessor;
import journeymap.client.render.draw.DrawEntityStep;

import journeymap.common.Journeymap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawEntityStep.class)
public class MixinClientFix {

    @Shadow
    boolean hideSneaks;

    @Inject(method = "<init>(Lnet/minecraft/entity/EntityLivingBase;)V", at = @At("RETURN"))
    private void changeHideSnE(CallbackInfo ci) {
        boolean value = ((IJourneyMapClientAccessor) Journeymap.getClient()).journeymapfix$getHideSneakingEntities();
        System.out.printf("Hide SN Entities: %b\n", value);
        this.hideSneaks = value;
    }
}
