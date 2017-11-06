package team2.imgurgallery.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import java.io.File;

import retrofit2.Call;
import retrofit2.Response;
import team2.imgurgallery.model.GalleryAlbum;
import team2.imgurgallery.model.GalleryImage;
import team2.imgurgallery.model.retrofit.GalleryResponse;
import team2.imgurgallery.retrofit.ApiHandler;
import team2.imgurgallery.retrofit.ImgurAPI;
import team2.imgurgallery.ui.callback.UiCallback;

/**
 * Created by d-kareski on 11/1/17.
 */
public class SplashActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportLoaderManager().initLoader(1, null, SplashActivity.this).forceLoad();
    }

    private UiCallback uiCallback = new UiCallback() {

        @Override
        public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {

            if (response.isSuccessful()) {
                GalleryResponse galleryResponse = response.body();
                if (galleryResponse != null) {
                    RequestBuilder<File> requestBuilder;

                    for(GalleryAlbum galleryAlbum: galleryResponse.galleryAlbums) {
                        for (GalleryImage galleryImage : galleryAlbum.images) {
                            requestBuilder = Glide.with(getApplicationContext())
                                    .downloadOnly()
                                    .load(galleryImage.link);

                            requestBuilder.preload();
                        }
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<GalleryResponse> call, Throwable t) {

        }
    };

    @Override
    public ImagesLoader onCreateLoader(int i, Bundle bundle) {
        return new ImagesLoader(SplashActivity.this, uiCallback);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader loader, Object data) {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {

    }

    private static class ImagesLoader extends AsyncTaskLoader {

        private final UiCallback uiCallback;
        String sort;
        String window;

        ImagesLoader(Context context, UiCallback uiCallback) {
            super(context);

            sort = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(SettingsActivity.PREF_SORT, "viral");

            window = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(SettingsActivity.PREF_SORT, "day");

            this.uiCallback = uiCallback;

            onContentChanged();
        }

        @Override
        public Object loadInBackground() {
            ApiHandler.getInstance().getGalleryCallback(ImgurAPI.SECTION_HOT, sort, window, true, 0, uiCallback);
            return null;
        }
    }
}
