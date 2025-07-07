package com.vincychannel.journeymapfix.access;

public interface IJourneyMapClientAccessor {
    void journeymapfix$setHideSneakingEntities(boolean value);
    void journeymapfix$setHideInvisiblePlayers(boolean value);

    boolean journeymapfix$getHideSneakingEntities();
    boolean journeymapfix$getHideInvisiblePlayers();
}
