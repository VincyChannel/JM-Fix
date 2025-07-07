package com.vincychannel.journeymapfix.mixin.client.ui;

import com.google.gson.JsonObject;

import journeymap.client.Constants;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.component.*;
import journeymap.client.ui.component.Label;
import journeymap.client.ui.serveroption.*;
import journeymap.common.properties.Category;
import journeymap.common.properties.config.EnumField;

import net.minecraft.client.gui.FontRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;

@Mixin(RadarOptions.class)
public abstract class MixinRadarOpt implements Draw {

    @Shadow(remap = false) private JsonObject properties;

    @Shadow(remap = false) private ButtonList buttons;

    @Shadow(remap = false) private FontRenderer fontRenderer;

    @Shadow(remap = false) private Label label;

    @Shadow(remap = false) private ButtonList checkBoxList;

    @Shadow(remap = false) private ListPropertyButton<ServerOption.Option> radarPropertyButton;

    @Shadow(remap = false) protected abstract void updateCheckBoxes(ServerOption.Option options);

    /**
     * @author VincyChannel
     * @reason Added the hide sneaking entities button into server admin panel
     */
    @Overwrite(remap = false)
    private ButtonList createRadarButtons() {
        ButtonList list = new ButtonList();;
        this.label = new Label(this.fontRenderer.getStringWidth(Constants.getString("jm.server.edit.radar.label")) + 10, "jm.server.edit.radar.label");
        this.label.setHAlign(DrawUtil.HAlign.Center);
        this.label.setWidth(this.label.getFitWidth(this.fontRenderer));

        CheckBox playerChkBx = this.checkBox("jm.server.edit.radar.chkbox.player", "playerRadar", this.properties);
        CheckBox villagerChkBx = this.checkBox("jm.server.edit.radar.chkbox.villager", "villagerRadar", this.properties);
        CheckBox animalChkBx = this.checkBox("jm.server.edit.radar.chkbox.animal", "animalRadar", this.properties);
        CheckBox mobChkBx = this.checkBox("jm.server.edit.radar.chkbox.mob", "mobRadar", this.properties);

        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(playerChkBx);
        checkBoxes.add(villagerChkBx);
        checkBoxes.add(animalChkBx);
        checkBoxes.add(mobChkBx);

        if (this.properties.get("hide_sneaking_entities") != null) {
            CheckBox hideEnt = this.checkBox("jm.server.edit.hide_sneaking_entities", "hide_sneaking_entities", this.properties);
            checkBoxes.add(hideEnt);
        }

        if (this.properties.get("hide_invisible_players") != null) {
            CheckBox hideEnt = this.checkBox("jm.server.edit.hide_invisible_entities", "hide_invisible_players", this.properties);
            checkBoxes.add(hideEnt);
        }

        this.checkBoxList = new ButtonList(checkBoxes.toArray(new CheckBox[0]));

        ServerOption option = new ServerOption("radar", this.properties);
        this.radarPropertyButton = new ListPropertyButton(EnumSet.allOf(ServerOption.Option.class), Constants.getString("jm.server.edit.radar.toggle.label"), new EnumField(Category.Hidden, "", option.getOption()));
        this.radarPropertyButton.addClickListener((button) -> {
            option.setOption((ServerOption.Option)this.radarPropertyButton.getField().get());
            this.updateToggleProperty(option, this.properties, "radar", "op_radar");
            this.updateCheckBoxes((ServerOption.Option)this.radarPropertyButton.getField().get());
            return true;
        });
        this.radarPropertyButton.setWidth(this.fontRenderer.getStringWidth(this.label.displayString) + 40);
        this.radarPropertyButton.setTooltip(300, new String[]{ServerOptionsManagerInvoker.callFormattedToolTipHeader("jm.server.edit.radar.toggle.label") + this.getToggleTooltipBase(), Constants.getString("jm.server.edit.radar.toggle.tooltip1"), Constants.getString("jm.server.edit.radar.toggle.tooltip2")});

        this.updateCheckBoxes((ServerOption.Option)this.radarPropertyButton.getField().get());

        list.add(this.label);
        list.add(this.radarPropertyButton);
        list.addAll(this.checkBoxList);
        return list;
    }

    @Override
    public void draw(int startX, int startY, int gap) {
        // Make some order because of the new button "Hide Sneaking Entities"
        this.label.setX(startX - this.label.getWidth() / 2);
        this.label.setY(startY + 5);
        DrawUtil.drawRectangle((double)this.label.getX(), (double)(this.label.getBottomY() - 4), (double)this.label.getWidth(), (double)1.0F, (new Color(255, 255, 255)).getRGB(), 1.0F);

        this.radarPropertyButton.setX(startX - this.radarPropertyButton.getWidth() / 2);
        this.radarPropertyButton.setY(this.label.getBottomY());

        this.checkBoxList.layoutCenteredHorizontal(startX, this.radarPropertyButton.getBottomY() + 3, true, gap - 2, true);
    }

    @Override
    public ButtonList getButtons() {
        return this.buttons;
    }
}
