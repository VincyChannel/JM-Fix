package com.vincychannel.journeymapfix.mixin.client.render;

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
     * @author Vincenzo Roberti
     * @reason Fixing Fullscreen Map Bug
     */
    @Overwrite
    private void populateGrid(Tile centerTile) {
        int endRow = (this.gridSize - 1) / 2;
        int endCol = (10 - 1) / 2;
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