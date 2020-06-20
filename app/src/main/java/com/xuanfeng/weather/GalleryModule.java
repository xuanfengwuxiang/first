package com.xuanfeng.weather;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/** Ensures that Glide's generated API is created for the Gallery sample. */
@GlideModule
public final class GalleryModule extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
