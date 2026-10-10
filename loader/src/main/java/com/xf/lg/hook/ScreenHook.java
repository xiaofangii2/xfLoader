package com.xf.lg.hook;

import java.lang.reflect.Constructor;

public class ScreenHook {
    public static void addButton(Object screen) {
        try {
            Class<?> btn = Class.forName(
                "net.minecraft.client.gui.widget.ButtonWidget");
            Class<?> builder = Class.forName(
                "net.minecraft.client.gui.widget.ButtonWidget$Builder");

            Class<?> text = Class.forName("net.minecraft.network.chat.Component");
            Object label = text.getMethod("literal", String.class)
                               .invoke(null, "数据包");

            Class<?> pressAction = Class.forName(
                "net.minecraft.client.gui.widget.ButtonWidget$OnPress");

            // lambda 用反射不好写,直接用一个实现类
            Object action = java.lang.reflect.Proxy.newProxyInstance(
                ScreenHook.class.getClassLoader(),
                new Class[]{pressAction},
                (p, m, a) -> {
                    System.out.println("[xiaofang] 点击了数据包按钮");
                    openFolder();
                    return null;
                });

            Object b = builder.getMethod("builder", text, pressAction)
                              .invoke(null, label, action);
            b = builder.getMethod("dimensions", int.class, int.class, int.class, int.class)
                       .invoke(b, 6, 6, 100, 20);
            Object widget = builder.getMethod("build").invoke(b);

            // screen.addDrawableChild(widget)
            Class<?> drawable = Class.forName(
                "net.minecraft.client.gui.components.events.GuiEventListener");
            screen.getClass()
                  .getMethod("addDrawableChild", drawable)
                  .invoke(screen, widget);

            System.out.println("[xiaofang] 按钮已加");
        } catch (Exception e) {
            System.out.println("[xiaofang] 加按钮失败: " + e);
        }
    }

    private static void openFolder() {
        System.out.println("[xiaofang] 打开 datapack 文件夹");
        // 安卓上开文件夹用 Intent,后面再补
    }
}
