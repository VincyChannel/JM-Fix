package com.vincychannel.journeymapfix.mixin.client.render;

import com.vincychannel.journeymapfix.config.ModConfig;

import journeymap.client.render.map.GridRenderer;
import journeymap.client.render.map.Tile;
import journeymap.client.render.map.TilePos;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.TreeMap;

@Mixin(value = GridRenderer.class, remap = false)
public abstract class MixinRenderGrid {

    @Shadow @Final private TreeMap<TilePos, Tile> grid;

    @Shadow protected abstract Tile findNeighbor(Tile tile, TilePos pos);

    @Shadow private int gridSize;

    /**
     * @author VincyChannel
     * @reason Fixing Fullscreen Map Grid Bug
     */
    @Overwrite
    private void populateGrid(Tile centerTile) {
        int value = this.gridSize;

        if (ModConfig.client.fixFullscreenBug) {
            value = 10;
        }

        int endRow = (this.gridSize - 1) / 2;
        int endCol = (value - 1) / 2; // Setted a static value TODO: Change this to dynamic value
        int startRow = -endRow;
        int startCol = -endCol;

        for(int z = startRow; z <= endRow; ++z) {
            for(int x = startCol; x <= endCol; ++x) {
                TilePos pos = TilePosInvoker.journeymapfix$create(x, z);
                Tile tile = this.findNeighbor(centerTile, pos);
                this.grid.put(pos, tile);
            }
        }
    }
}