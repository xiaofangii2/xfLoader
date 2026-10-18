package com.xf.lg.forge;

import com.xf.lg.LoaderCore;
import com.xf.lg.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@net.minecraftforge.fml.common.Mod("xfloader")
public class XfLoaderForge {

    public XfLoaderForge() {
        Log.ok("Forge 模组模式启动 (暴力反射版)");
        LoaderCore.init();

        // 纯反射注册事件
        try {
            // 1. 拿 EVENT_BUS (用反射避开 IEventBus 类加载)
            Class<?> mfClass = Class.forName("net.minecraftforge.common.MinecraftForge");
            Field eventBusField = mfClass.getField("EVENT_BUS");
            Object eventBus = eventBusField.get(null);

            // 2. 我们的监听器 (纯Object)
            Object listener = new EventListener();

            // 3. eventBus.register(listener)
            Method registerMethod = eventBus.getClass().getMethod("register", Object.class);
            registerMethod.invoke(eventBus, listener);
            
            Log.ok("暴力反射注册成功！");

        } catch (Throwable t) {
            Log.fail("暴力反射注册失败: " + t);
            t.printStackTrace();
        }
    }

    // 这个类没有任何 Forge 注解，只有一个 Object 参数的方法
    public static class EventListener {
        
        // 这个方法名随便起，但我们需要自己触发它
        public void onServerStarting(Object event) {
            Log.ok("触发事件: 服务端启动");
            try {
                Method getServer = event.getClass().getMethod("getServer");
                Object server = getServer.invoke(event);
                com.xf.lg.hook.WorldHook.onWorldLoad(server);
            } catch (Throwable t) {
                Log.fail("onServerStarting 处理失败: " + t);
            }
        }

        public void onPlayerJoin(Object event) {
            try {
                Method getEntity = event.getClass().getMethod("getEntity");
                Object player = getEntity.invoke(event);
                Method level = player.getClass().getMethod("level");
                Object levelObj = level.invoke(player);
                Method getServer = levelObj.getClass().getMethod("getServer");
                Object server = getServer.invoke(levelObj);

                if (server != null) {
                    Method getPlayerList = server.getClass().getMethod("getPlayerList");
                    Object playerList = getPlayerList.invoke(server);
                    com.xf.lg.hook.PostJoinHook.onPlayerJoin(playerList, player);
                }
            } catch (Throwable t) {
                Log.fail("onPlayerJoin 处理失败: " + t);
            }
        }

        public void onScreenInit(Object event) {
            try {
                Method getScreen = event.getClass().getMethod("getScreen");
                Object screen = getScreen.invoke(event);
                String name = screen.getClass().getName();
                if (name.endsWith("TitleScreen") || name.endsWith("PauseScreen")) {
                    com.xf.lg.gui.ScreenHook.addButton((net.minecraft.client.gui.screens.Screen) screen);
                    Log.ok("暴力反射: 界面按钮已注入");
                }
            } catch (Throwable t) {
                Log.fail("onScreenInit 处理失败: " + t);
            }
        }
    }
}
