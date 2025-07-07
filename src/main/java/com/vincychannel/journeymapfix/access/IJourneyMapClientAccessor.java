package com.vincychannel.journeymapfix.access;

public interface IJourneyMapClientAccessor {
    void journeymapfix$setHideSneakingEntities(boolean value);
    void  journeymapfix$setHideInvisibleEntities(boolean value);

    boolean journeymapfix$getHideSneakingEntities();
    boolean journeymapfix$getHideInvisibleEntities();
}
