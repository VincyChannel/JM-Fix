package com.vincychannel.journeymapfix.mixin.server.config;

import com.vincychannel.journeymapfix.access.IPermissionPropertiesAccessor;
import com.vincychannel.journeymapfix.config.ModConfig;

import journeymap.common.properties.config.BooleanField;
import journeymap.server.properties.GlobalProperties;
import journeymap.server.properties.ServerCategory;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlobalProperties.class)
public class MixinServerCFG implements IPermissionPropertiesAccessor {

    @Unique public BooleanField hideSneakingEntities;

    @Unique public BooleanField hideInvisibleEntities;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void changeGlbProp(CallbackInfo ci) {
        if (ModConfig.server.enableHideSneakingEntities || FMLCommonHandler.instance().getSide() == Side.SERVER) {
            this.hideSneakingEntities = new BooleanField(ServerCategory.Radar, "Hide Sneaking Entities", true);
        }

        if (ModConfig.server.enableHideInvisibleEntities || FMLCommonHandler.instance().getSide() == Side.SERVER) {
            this.hideInvisibleEntities = new BooleanField(ServerCategory.Radar, "Hide Invisible Entities", true);
        }
    }

    @Override
    public BooleanField getHideSneakingEntities() {
        return this.hideSneakingEntities;
    }

    @Override
    public BooleanField getHideInvisibleEntities() {
        return this.hideInvisibleEntities;
    }
}
