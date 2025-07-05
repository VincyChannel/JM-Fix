package com.vincychannel.journeymapfix.mixin.client.render;

import journeymap.client.render.map.TilePos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TilePos.class)
public interface TilePosInvoker {
    @Invoker("<init>")
    static TilePos journeymapfix$create(int x, int z) {
        throw new AssertionError(); // verrà sostituito da Mixin
    }
}
