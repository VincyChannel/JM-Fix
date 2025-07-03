package com.vincychannel.journeymapfix.mixin;

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

@Mixin(Fullscreen.class)
public class MixinFullscreenMapGui {
    @Final
    @Shadow(remap = false)
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
        int tileSize = 512;

        int gridSize = Math.max(screenWidth, screenHeight) / tileSize;
        if (gridSize < 1) gridSize = 1;

        gridRenderer.setGridSize(gridSize);
    }
}
