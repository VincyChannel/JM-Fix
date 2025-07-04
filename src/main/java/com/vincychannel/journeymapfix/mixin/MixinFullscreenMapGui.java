package com.vincychannel.journeymapfix.mixin;

import journeymap.client.render.map.Tile;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.render.map.GridRenderer;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Fullscreen.class, remap = false)
public class MixinFullscreenMapGui {
    @Final
    @Shadow()
    static GridRenderer gridRenderer;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        System.out.println("TESTING FULLSCREEN MAP GUI");
        journeymapfix$fixGridSize();
    }

    @Inject(method = "drawMap", at = @At("HEAD"), remap = false)
    private void beforeDrawMap(CallbackInfo ci) {
        System.out.println("TESTING FULLSCREEN MAP GUI");
        journeymapfix$fixGridSize();
    }

    @Unique
    private void journeymapfix$fixGridSize() {
        int screenWidth = Minecraft.getMinecraft().displayWidth;
        int screenHeight = Minecraft.getMinecraft().displayHeight;
        int gridSize = Math.max(screenWidth, screenHeight) / Tile.TILESIZE;

        while ((gridSize * Tile.TILESIZE) < screenWidth)
        {
            gridSize++;
        }
        //add one for good measure
        gridSize++;
        //assure gridSize is odd
        if (gridSize % 2 == 0)
        {
            gridSize++;
        }
        gridRenderer.setGridSize(gridSize);
    }
}
