package com.xf.lg.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;

public class DatapackScreen extends Screen {

    private final Screen parent;
    private List<DatapackEntry> entries;
    private int selected = -1;

    private static final int PADDING = 8;
    private static final int LIST_WIDTH = 200;
    private static final int LINE_H = 18;

    public DatapackScreen(Screen parent) {
        super(Component.literal("数据包列表"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        entries = DatapackList.load();

        int btnW = 100, btnH = 20;
        int bx = PADDING + 8;
        int by = this.height - 44;   // 往上调

        this.addRenderableWidget(
            Button.builder(Component.literal("完成"), b -> this.onClose())
                .pos(bx, by)
                .size(btnW, btnH)
                .build()
        );
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, this.width, this.height, 0xC0000000);
        super.extractRenderState(g, mouseX, mouseY, partialTick);

        Font font = Minecraft.getInstance().font;

        g.text(font, "数据包列表", PADDING, PADDING, 0xFFFFFFFF);

        int top = PADDING + 20;
        int bottom = this.height - 52;   // 面板下边界也往上, 和按钮拉开
        int listH = bottom - top;
        int listX = PADDING;
        int previewX = listX + LIST_WIDTH + PADDING;
        int previewW = this.width - previewX - PADDING;

        int panelBg = 0xE0000000;

        g.fill(listX, top, listX + LIST_WIDTH, top + listH, panelBg);
        g.outline(listX, top, listX + LIST_WIDTH, top + listH, 0x60FFFFFF);

        g.fill(previewX, top, previewX + previewW, top + listH, panelBg);
        g.outline(previewX, top, previewX + previewW, top + listH, 0x60FFFFFF);

        if (entries != null) {
            for (int i = 0; i < entries.size(); i++) {
                int y = top + i * LINE_H;
                if (y + LINE_H > top + listH) break;

                DatapackEntry e = entries.get(i);
                boolean hovered = mouseX >= listX && mouseX <= listX + LIST_WIDTH
                               && mouseY >= y && mouseY <= y + LINE_H;

                if (i == selected) {
                    g.fill(listX + 1, y, listX + LIST_WIDTH - 1, y + LINE_H, 0x80FFFFFF);
                } else if (hovered) {
                    g.fill(listX + 1, y, listX + LIST_WIDTH - 1, y + LINE_H, 0x40FFFFFF);
                }

                g.text(font, e.name, listX + 4, y + 5, 0xFFFFFFFF);
            }
        }

        if (entries != null && selected >= 0 && selected < entries.size()) {
            DatapackEntry e = entries.get(selected);
            g.text(font, "名称: " + e.name, previewX + 6, top + 6, 0xFFFF80);
            g.text(font, "描述: " + e.description, previewX + 6, top + 26, 0xFFFFFFFF);
        } else {
            g.text(font, "选择一个数据包", previewX + 6, top + 6, 0xAAAAAA);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
        int top = PADDING + 20;
        int bottom = this.height - 52;
        int listX = PADDING;

        double mx = event.x();
        double my = event.y();

        if (mx >= listX && mx <= listX + LIST_WIDTH
                && my >= top && my <= bottom) {
            int idx = (int)((my - top) / LINE_H);
            if (entries != null && idx >= 0 && idx < entries.size()) {
                selected = idx;
                return true;
            }
        }

        return super.mouseClicked(event, doubled);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().gui.setScreen(parent);
    }
}
