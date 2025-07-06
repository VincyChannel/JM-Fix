package com.vincychannel.journeymapfix.mixin.client;

import com.vincychannel.journeymapfix.access.IJourneyMapClientAccessor;

import journeymap.client.JourneymapClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(JourneymapClient.class)
public class MixinJMClient implements IJourneyMapClientAccessor {

    @Unique private boolean journeymapfix$hideSneakingEntities;

    @Unique
    public void journeymapfix$setHideSneakingEntities(boolean value) {
        this.journeymapfix$hideSneakingEntities = value;
    }

    @Unique
    public boolean journeymapfix$getHideSneakingEntities() {
        return this.journeymapfix$hideSneakingEntities;
    }
}
