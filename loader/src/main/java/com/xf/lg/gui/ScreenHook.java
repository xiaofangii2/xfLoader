package com.xf.lg.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ScreenHook {

    // 独立运行模式用这个(ASM 会调用)
    public static Button createButton(Screen screen) {
        return buildButton(screen, screen.width, screen.height);
    }

    // Fabric 模式用这个(Mixin 会调用)
    public static void addButton(Screen screen) {
        Button btn = buildButton(screen, screen.width, screen.height);
        try {
            java.lang.reflect.Method m = Screen.class.getDeclaredMethod("addRenderableWidget",
                net.minecraft.client.gui.components.events.GuiEventListener.class);
            m.setAccessible(true);
            m.invoke(screen, btn);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static Button buildButton(Screen screen, int w, int h) {
        int mpBottom = findMultiplayerBottom(screen);
        int newY = (mpBottom > 0) ? mpBottom + 4 : h / 4 + 120;

        if (mpBottom > 0) {
            int shift = 24;
            for (GuiEventListener el : screen.children()) {
                if (el instanceof AbstractWidget aw && aw.getY() >= mpBottom) {
                    aw.setY(aw.getY() + shift);
                }
            }
        }

        int x = (w - 200) / 2;

        return Button.builder(
            Component.literal("数据包"),
            b -> Minecraft.getInstance().gui.setScreen(new DatapackScreen(screen))
        ).pos(x, newY).size(200, 20).build();
    }

    private static int findMultiplayerBottom(Screen screen) {
        for (GuiEventListener el : screen.children()) {
            if (el instanceof AbstractWidget w) {
                String label = w.getMessage().getString();
                if (label.contains("多人游戏") || label.equalsIgnoreCase("Multiplayer")) {
                    return w.getY() + w.getHeight();
                }
            }
        }
        return -1;
    }
}
