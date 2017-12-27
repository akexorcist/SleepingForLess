package com.akexorcist.sleepingforless.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Akexorcist on 3/14/2016 AD.
 */
public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        int cacheSize = 1024 * 1024 * 20;
        int memorySize = 1024 * 1024 * 20;
        String cacheDirectory = context.getCacheDir().getAbsolutePath();
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheDirectory, cacheSize));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        builder.setMemoryCache(new LruResourceCache(memorySize));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

    }
}
