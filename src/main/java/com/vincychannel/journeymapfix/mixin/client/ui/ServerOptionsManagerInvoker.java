package com.vincychannel.journeymapfix.mixin.client.ui;

import journeymap.client.ui.serveroption.ServerOptionsManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerOptionsManager.class)
public interface ServerOptionsManagerInvoker {

    @Invoker(value = "formattedToolTipHeader", remap = false)
    static String callFormattedToolTipHeader(String input) {
        throw new AssertionError();
    }
}

