package com.vincychannel.journeymapfix.mixin.client.ui.fullscreen;

import com.vincychannel.journeymapfix.config.ModConfig;

import journeymap.client.data.WaypointsData;
import journeymap.client.model.Waypoint;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.fullscreen.layer.WaypointLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.geom.Point2D;
import java.util.List;

@Mixin(WaypointLayer.class)
public abstract class MixinWaypointLayer {

    @Shadow(remap = false) @Final private List<DrawStep> drawStepList;

//    @Shadow @Final private WaypointLayer.BlockOutlineDrawStep clickDrawStep;

    @Shadow(remap = false) protected abstract void click(GridRenderer gridRenderer, BlockPos blockCoord);

    @Shadow(remap = false) Waypoint selected;

    @Shadow(remap = false) private Fullscreen fullscreen;

    /**
     * @author VincyChannel
     * @reason Editing the double Click event in the fullscreen map
     */
    @Overwrite(remap = false)
    public List<DrawStep> onMouseClick(Minecraft mc, GridRenderer gridRenderer, Point2D.Double mousePosition,
                                       BlockPos blockCoord, int button, boolean doubleClick, float fontScale) {
        if (!WaypointsData.isManagerEnabled()) {
            return this.drawStepList;
        } else {
//            if (!this.drawStepList.contains(this.clickDrawStep)) {
//                this.drawStepList.add(this.clickDrawStep);
//            }

            if (!doubleClick) {
                this.click(gridRenderer, blockCoord);
            } else {
                if (this.selected != null) {
                    UIManager.INSTANCE.openWaypointManager(this.selected, this.fullscreen);
                    return this.drawStepList;
                }

                if (ModConfig.client.disableWaypointMouseRC && button == 0) {
                    return this.drawStepList;
                }

                Waypoint waypoint = Waypoint.at(blockCoord, Waypoint.Type.Normal, mc.player.dimension);
                UIManager.INSTANCE.openWaypointEditor(waypoint, true, this.fullscreen);
            }

            return this.drawStepList;
        }
    }
}