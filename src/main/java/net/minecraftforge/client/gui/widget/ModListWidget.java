/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.locale.Language;
import net.minecraftforge.client.gui.ModListScreen;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.forgespi.language.IModInfo;

public class ModListWidget extends ObjectSelectionList<ModListWidget.ModEntry> {
    private static String stripControlCodes(String value) { return net.minecraft.util.StringUtil.stripColor(value); }
    private static final ResourceLocation VERSION_CHECK_ICONS = ResourceLocation.fromNamespaceAndPath(ForgeVersion.MOD_ID, "textures/gui/version_check_icons.png");
    private final int listWidth;

    private final ModListScreen parent;

    public ModListWidget(ModListScreen parent, int listWidth, int top, int bottom) {
        super(parent.getMinecraftInstance(), listWidth, bottom, top, parent.getFontRenderer().lineHeight * 2 + 8);
        this.parent = parent;
        this.listWidth = listWidth;
        this.refreshList();
    }

    @Override
    public int getRowWidth() {
        return this.listWidth;
    }

    @Override
    protected int scrollBarX() {
        return this.getRowRight() - 8;
    }

    @Override
    protected void renderSelection(GuiGraphics gui, ModListWidget.ModEntry entry, int color) {
        int widthOffset = this.scrollbarVisible() ? 11 : 4;
        int top = entry.getContentY();
        int left  = entry.getContentX();
        int right = left + entry.getWidth() - widthOffset;
        int bottom = top + entry.getHeight();
        gui.fill(left,     top - 2, right,     bottom + 2, color);
        gui.fill(left + 1, top - 1, right - 1, bottom + 1, /*backgroundColor*/ 0xFF000000);
    }

    public void refreshList() {
        this.clearEntries();
        parent.buildModList(this::addEntry, mod->new ModEntry(mod, this.parent));
    }

    public class ModEntry extends ObjectSelectionList.Entry<ModEntry> {
        private final IModInfo modInfo;
        private final ModListScreen parent;

        ModEntry(IModInfo info, ModListScreen parent) {
            this.modInfo = info;
            this.parent = parent;
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select", modInfo.getDisplayName());
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            int top = this.getContentY();
            int left = this.getContentX();
            int entryHeight = this.getContentBottom();

            Component name = Component.literal(stripControlCodes(modInfo.getDisplayName()));
            Component version = Component.literal(stripControlCodes(MavenVersionStringHelper.artifactVersionToString(modInfo.getVersion())));
            VersionChecker.CheckResult vercheck = VersionChecker.getResult(modInfo);
            Font font = this.parent.getFontRenderer();
            var barOffset = ModListWidget.this.scrollbarVisible() ? 6 : 0;
            guiGraphics.drawString(font, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name,    listWidth - 6 - barOffset))), left + 3, top + 2, 0xFFFFFFFF, false);
            guiGraphics.drawString(font, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(version, listWidth - 6 - barOffset))), left + 3, top + 2 + font.lineHeight, 0xFFCCCCCC, false);
            if (vercheck.status().shouldDraw()) {
                //TODO: [Forge][ModList] Consider adding more icons for visualization
                guiGraphics.pose().pushMatrix();
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, VERSION_CHECK_ICONS, getX() + width - 12 - barOffset, top + entryHeight / 4, vercheck.status().getSheetOffset() * 8, (vercheck.status().isAnimated() && ((System.currentTimeMillis() / 800 & 1)) == 1) ? 8 : 0, 8, 8, 64, 16);
                guiGraphics.pose().popMatrix();

            }
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent info, boolean recent) {
            parent.setSelected(this);
            ModListWidget.this.setSelected(this);
            return false;
        }

        public IModInfo getInfo() {
            return modInfo;
        }
    }
}
