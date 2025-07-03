//package com.vincychannel.journeymapfix.mixin;
//
//import zone.rong.mixinbooter.Context;
//import zone.rong.mixinbooter.ILateMixinLoader;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class MixinLoad implements ILateMixinLoader {
//    @Override
//    public List<String> getMixinConfigs() {
//        List<String> mixinConfigs = Collections.synchronizedList(new ArrayList<String>());
//        mixinConfigs.add("mixins.journeymapfix.mixin");
//        return mixinConfigs;
//    }
//
//    @Override
//    public boolean shouldMixinConfigQueue(Context context) {
//        return ILateMixinLoader.super.shouldMixinConfigQueue(context);
//    }
//
//    @Override
//    public boolean shouldMixinConfigQueue(String mixinConfig) {
//        return ILateMixinLoader.super.shouldMixinConfigQueue(mixinConfig);
//    }
//
//    @Override
//    public void onMixinConfigQueued(Context context) {
//        ILateMixinLoader.super.onMixinConfigQueued(context);
//    }
//
//    @Override
//    public void onMixinConfigQueued(String mixinConfig) {
//        ILateMixinLoader.super.onMixinConfigQueued(mixinConfig);
//    }
//}
