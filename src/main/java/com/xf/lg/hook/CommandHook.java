package com.xf.lg.hook;

import com.xf.lg.Config;
import com.xf.lg.Log;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.lang.reflect.Method;

public class CommandHook {

    private static Class<?> COMP_CLS;

    public static void register(Object server) {
        try {
            COMP_CLS = Class.forName("net.minecraft.network.chat.Component");

            // ★ 每次都从 server 重新拿 commands, 不用之前的引用
            Object commands = server.getClass().getMethod("getCommands").invoke(server);
            Log.i("当前 Commands 实例: " + System.identityHashCode(commands));

            Object dispatcherObj = commands.getClass().getMethod("getDispatcher").invoke(commands);
            @SuppressWarnings("unchecked")
            CommandDispatcher<Object> dispatcher = (CommandDispatcher<Object>) dispatcherObj;

            RootCommandNode<Object> rootNode = dispatcher.getRoot();
            if (rootNode.getChild("xfloader") != null) {
                Log.i("/xfloader 已存在, 跳过");
                return;
            }

            LiteralArgumentBuilder<Object> root = LiteralArgumentBuilder.literal("xfloader");
            root.executes(new RootCommand());

            LiteralArgumentBuilder<Object> debugLit = LiteralArgumentBuilder.literal("debug");
            debugLit.executes(new DebugStatusCommand());
            RequiredArgumentBuilder<Object, Boolean> debugArg =
                RequiredArgumentBuilder.argument("value", BoolArgumentType.bool());
            debugArg.executes(new DebugSetCommand());
            debugLit.then(debugArg);
            root.then(debugLit);

            LiteralCommandNode<Object> node = root.build();
            rootNode.addChild(node);

            CommandNode<Object> found = rootNode.getChild("xfloader");
            Log.ok("注册 /xfloader, 根节点数: " + rootNode.getChildren().size()
                    + ", 验证 = " + (found != null ? found.getName() : "null"));

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
        } catch (Throwable t) {
            Log.fail("sendMsg 失败: " + t);
        }
    }

    static class RootCommand implements Command<Object> {
        @Override public int run(CommandContext<Object> ctx) {
            sendMsg(ctx.getSource(), "xfLoader 还在开发");
            return 1;
        }
    }
    static class DebugStatusCommand implements Command<Object> {
        @Override public int run(CommandContext<Object> ctx) {
            sendMsg(ctx.getSource(), "当前 debug = " + Config.DEBUG);
            return 1;
        }
    }
    static class DebugSetCommand implements Command<Object> {
        @Override public int run(CommandContext<Object> ctx) {
            boolean val = ctx.getArgument("value", Boolean.class);
            Config.DEBUG = val;
            Log.DEBUG = val;
            Config.save();
            sendMsg(ctx.getSource(), "debug = " + val);
            return 1;
        }
    }
}
