package com.xf.lg;

import net.fabricmc.api.ModInitializer;

public class XfLoaderFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Log.ok("Fabric 模组模式启动");
        LoaderCore.init();
    }
}
