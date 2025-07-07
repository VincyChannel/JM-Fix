package com.vincychannel.journeymapfix.access;

import journeymap.common.properties.config.BooleanField;

public interface IPermissionPropertiesAccessor {
    BooleanField getHideSneakingEntities();

    BooleanField getHideInvisiblePlayers();
}