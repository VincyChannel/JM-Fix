package com.vincychannel.journeymapfix.mixin.old;

import journeymap.server.properties.GlobalProperties;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(GlobalProperties.class)
public class MixinServerConfig {
//    @Unique
//    public BooleanField journeymapfix$hideSneakingEntities;
//
//    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
//    private void changeGlbProp(CallbackInfo ci) {
//        this.journeymapfix$hideSneakingEntities = new BooleanField(ServerCategory.General, "Hide Sneaking Entities", true);
//    }
}
