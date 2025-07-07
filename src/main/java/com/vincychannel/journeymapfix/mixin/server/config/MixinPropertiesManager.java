package com.vincychannel.journeymapfix.mixin.server.config;

import journeymap.server.properties.GlobalProperties;
import journeymap.server.properties.PropertiesManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PropertiesManager.class)
public class MixinPropertiesManager {

    @Shadow(remap = false) private GlobalProperties globalProperties;

    @Inject(method = "loadConfigs", at = @At("RETURN"), remap = false)
    public void loadConfigs(CallbackInfo ci) {
        this.globalProperties.save(); // Saving new config entries to alredy generated config file
    }
}
