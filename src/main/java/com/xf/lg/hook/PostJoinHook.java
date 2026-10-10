package com.xf.lg.hook;

import com.xf.lg.Log;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public class PostJoinHook {

    private static boolean reloaded = false;

    public static void onPlayerJoin(Object playerList, Object player) {
        if (reloaded) return;
        Log.step("玩家加入");

        try {
            Object server = null;
            try {
                server = playerList.getClass().getMethod("getServer").invoke(playerList);
            } catch (NoSuchMethodException e) {
                for (java.lang.reflect.Field f : playerList.getClass().getDeclaredFields()) {
                    if (f.getName().equals("server")) {
                        f.setAccessible(true);
                        server = f.get(playerList);
                        break;
                    }
                }
            }
            if (server == null) { Log.fail("拿不到 server"); return; }

            // 1. 执行 /reload
            Log.step("执行 /reload");
            Object commands = server.getClass().getMethod("getCommands").invoke(server);
            Object sourceStack = null;
            for (Method m : server.getClass().getMethods()) {
                if (m.getName().equals("createCommandSourceStack") && m.getParameterCount() == 0) {
                    sourceStack = m.invoke(server);
                    break;
                }
            }
            if (sourceStack == null) { Log.fail("拿不到 CommandSourceStack"); return; }

            Method perf = null;
            for (Method m : commands.getClass().getMethods()) {
                if (m.getName().equals("performPrefixedCommand") && m.getParameterCount() == 2) {
                    perf = m;
                    break;
                }
            }
            if (perf == null) { Log.fail("找不到 performPrefixedCommand"); return; }

            Object result = perf.invoke(commands, sourceStack, "reload");
            Log.ok("/reload 触发, 返回: " + result);

            // 2. 等 reload 完成后注册命令
            final Object fServer = server;
            if (result instanceof CompletableFuture<?> future) {
                Log.i("reload 是异步, 等它完成");
                future.whenComplete((r, err) -> {
                    if (err != null) {
                        Log.fail("reload 出错: " + err);
                        return;
                    }
                    Log.ok("reload 完成, 注册命令");
                    CommandHook.register(fServer);
                });
            } else {
                Log.i("reload 是同步, 立即注册");
                CommandHook.register(fServer);
            }

            reloaded = true;
        } catch (Throwable t) {
            Log.fail("PostJoin 失败: " + t);
            t.printStackTrace();
        }
    }
}
