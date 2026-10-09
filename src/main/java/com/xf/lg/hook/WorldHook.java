package com.xf.lg.hook;

import com.xf.lg.DatapackCache;
import com.xf.lg.Log;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

public class WorldHook {

    private static boolean injected = false;

    public static void onWorldLoad(Object server) {
        if (injected) { Log.i("已注入"); return; }
        Log.step("世界加载, 注入数据包");

        try {
            Object repo = server.getClass().getMethod("getPackRepository").invoke(server);
            Log.ok("repo = " + repo.getClass().getName());

            Field sourcesField = repo.getClass().getDeclaredField("sources");
            sourcesField.setAccessible(true);
            Object oldObj = sourcesField.get(repo);

            List<Object> newSources = new ArrayList<>();
            if (oldObj instanceof Iterable<?> it) {
                for (Object o : it) newSources.add(o);
            }
            Log.i("原 sources: " + newSources.size());

            Class<?> frsClass = Class.forName("net.minecraft.server.packs.repository.FolderRepositorySource");
            Class<?> ptClass  = Class.forName("net.minecraft.server.packs.PackType");
            Class<?> psClass  = Class.forName("net.minecraft.server.packs.repository.PackSource");
            Class<?> dvClass  = Class.forName("net.minecraft.world.level.validation.DirectoryValidator");

            Object serverData  = ptClass.getField("SERVER_DATA").get(null);
            Object worldSource = psClass.getField("WORLD").get(null);
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**");
            Object validator = dvClass.getConstructor(PathMatcher.class).newInstance(matcher);

            Object src = frsClass
                .getConstructor(Path.class, ptClass, psClass, dvClass)
                .newInstance(DatapackCache.DATAPACK_DIR, serverData, worldSource, validator);
            newSources.add(src);
            Log.ok("source: " + DatapackCache.DATAPACK_DIR);

            // 构造新 repo
            Class<?> repoCls = repo.getClass();
            Constructor<?> ctor = null;
            for (Constructor<?> c : repoCls.getDeclaredConstructors()) {
                if (c.getParameterCount() == 1 && c.getParameterTypes()[0].isArray()) {
                    ctor = c; break;
                }
            }
            Class<?> srcCls = Class.forName("net.minecraft.server.packs.repository.RepositorySource");
            Object arr = Array.newInstance(srcCls, newSources.size());
            for (int i = 0; i < newSources.size(); i++) Array.set(arr, i, newSources.get(i));
            ctor.setAccessible(true);
            Object newRepo = ctor.newInstance(arr);
            Log.ok("新 repo = " + newRepo);

            newRepo.getClass().getMethod("reload").invoke(newRepo);
            Log.ok("reload 完成");

            // 替换 server 字段
            Field f = null;
            Class<?> cls = server.getClass();
            while (cls != null && f == null) {
                for (Field ff : cls.getDeclaredFields()) {
                    if (ff.getType().equals(repoCls)) { f = ff; break; }
                }
                cls = cls.getSuperclass();
            }
            f.setAccessible(true);
            f.set(server, newRepo);
            Log.ok("已替换");

            injected = true;
        } catch (Throwable t) {
            Log.fail("注入失败: " + t);
            t.printStackTrace();
        }
    }
}
