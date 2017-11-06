package team2.imgurgallery.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import team2.imgurgallery.model.CustomImageSizeModel;

/**
 * Created by d-kareski on 11/1/17.
 */
@GlideModule
public class CustomImageSizeGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        // set disk cache size & external vs. internal
        int cacheSize100MegaBytes = 104857600;
//
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));

//        String downloadDirectoryPath = Environment.getDownloadCacheDirectory().getPath();
//        builder.setDiskCache(
//                new DiskLruCacheFactory( downloadDirectoryPath, cacheSize100MegaBytes )
//        );
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        // nothing to do here
        registry.append(CustomImageSizeModel.class, InputStream.class, new CustomImageSizeUrlLoaderFactory());
    }

    private class CustomImageSizeUrlLoaderFactory implements ModelLoaderFactory<CustomImageSizeModel, InputStream> {
        private final ModelCache<CustomImageSizeModel, GlideUrl> modelCache = new ModelCache<>(500);

        @Override
        public ModelLoader<CustomImageSizeModel, InputStream> build(MultiModelLoaderFactory multiFactory) {
            ModelLoader<GlideUrl, InputStream> modelLoader = multiFactory.build(GlideUrl.class, InputStream.class);
            return new CustomImageSizeUrlLoader(modelLoader, modelCache);
        }

        @Override
        public void teardown() {

        }
    }

    public static class CustomImageSizeUrlLoader extends BaseGlideUrlLoader<CustomImageSizeModel> {

        public CustomImageSizeUrlLoader(ModelLoader<GlideUrl, InputStream> concreteLoader, @Nullable ModelCache<CustomImageSizeModel, GlideUrl> modelCache) {
            super(concreteLoader, modelCache);
        }

        @Override
        protected String getUrl(CustomImageSizeModel model, int width, int height, Options options) {
            return model.requestCustomSizeUrl(width, height);
        }

        @Override
        public boolean handles(CustomImageSizeModel customImageSizeModel) {
            return true;
        }
    }
}