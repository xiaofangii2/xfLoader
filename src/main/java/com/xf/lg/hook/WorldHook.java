package com.xf.lg.hook;

import com.xf.lg.DatapackCache;
import com.xf.lg.Log;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

public class WorldHook {

    private static boolean injected = false;

    public static void onWorldLoad(Object server) {
        if (injected) return;
        Log.step("世界加载, 注入数据包 source");

        try {
            Object repo = server.getClass().getMethod("getPackRepository").invoke(server);
            Field sourcesField = repo.getClass().getDeclaredField("sources");
            sourcesField.setAccessible(true);
            Object oldObj = sourcesField.get(repo);

            List<Object> newSources = new ArrayList<>();
            if (oldObj instanceof Iterable<?> it) for (Object o : it) newSources.add(o);

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

            Class<?> repoCls = repo.getClass();
            Constructor<?> ctor = null;
            for (Constructor<?> c : repoCls.getDeclaredConstructors()) {
                if (c.getParameterCount() == 1 && c.getParameterTypes()[0].isArray()) ctor = c;
            }
            Class<?> srcCls = Class.forName("net.minecraft.server.packs.repository.RepositorySource");
            Object arr = Array.newInstance(srcCls, newSources.size());
            for (int i = 0; i < newSources.size(); i++) Array.set(arr, i, newSources.get(i));
            ctor.setAccessible(true);
            Object newRepo = ctor.newInstance(arr);

            newRepo.getClass().getMethod("reload").invoke(newRepo);

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
            Log.ok("source 注入完成");
            injected = true;
        } catch (Throwable t) {
            Log.fail("注入失败: " + t);
            t.printStackTrace();
        }
    }
}
