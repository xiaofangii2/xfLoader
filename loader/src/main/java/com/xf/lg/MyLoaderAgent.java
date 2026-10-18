package com.xf.lg;

import java.lang.instrument.Instrumentation;

public class MyLoaderAgent {
    public static void premain(String args, Instrumentation inst) {
        Log.ok("agent 挂载 (独立模式)");
        inst.addTransformer(new MyTransformer(), true);
        inst.addTransformer(new PlayerJoinTransformer(), true);
        inst.addTransformer(new ScreenTransformer(), true);
        Log.ok("transformers 已装 (3 个)");

        LoaderCore.init();
        LoaderCore.onPreload();
    }
}
