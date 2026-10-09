package com.xf.lg.hook;

import com.xf.lg.Log;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.lang.reflect.Method;

public class CommandHook {

    private static boolean registered = false;
    private static Class<?> COMP_CLS;

    public static void register(Object server) {
        if (registered) return;
        try {
            Log.step("注册 /xfloader");
            COMP_CLS = Class.forName("net.minecraft.network.chat.Component");

            Object commands = server.getClass().getMethod("getCommands").invoke(server);
            Object dispatcherObj = commands.getClass().getMethod("getDispatcher").invoke(commands);
            @SuppressWarnings("unchecked")
            CommandDispatcher<Object> dispatcher = (CommandDispatcher<Object>) dispatcherObj;

            LiteralArgumentBuilder<Object> root =
                LiteralArgumentBuilder.<Object>literal("xfloader")
                    .executes(ctx -> {
                        Log.ok("/xfloader 被调用");
                        sendMsg(ctx.getSource(), "xfLoader 还在开发");
                        return 1;
                    })
                    .then(LiteralArgumentBuilder.<Object>literal("debug")
                        .then(RequiredArgumentBuilder
                            .<Object, Boolean>argument("value", BoolArgumentType.bool())
                            .executes(ctx -> {
                                boolean val = ctx.getArgument("value", Boolean.class);
                                Log.DEBUG = val;
                                Log.ok("debug 模式: " + val);
                                sendMsg(ctx.getSource(), "debug = " + val);
                                return 1;
                            })));

            dispatcher.register(root);
            Log.ok("命令注册完成");
            registered = true;
        } catch (Throwable t) {
            Log.fail("命令注册失败: " + t);
            t.printStackTrace();
        }
    }

    private static void sendMsg(Object source, String text) {
        try {
            Object component = COMP_CLS.getMethod("literal", String.class).invoke(null, text);
            java.util.function.Supplier<Object> sup = () -> component;
            for (Method m : source.getClass().getMethods()) {
                if (m.getName().equals("sendSuccess") && m.getParameterCount() == 2) {
                    m.invoke(source, sup, false);
                    return;
                }
            }
            Log.fail("没找到 sendSuccess");
        } catch (Throwable t) {
            Log.fail("sendMsg 失败: " + t);
        }
    }
}
