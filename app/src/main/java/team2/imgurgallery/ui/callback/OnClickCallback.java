package team2.imgurgallery.ui.callback;

import android.widget.ImageView;

import team2.imgurgallery.model.GalleryAlbum;

/**
 * Created by d-kareski on 11/1/17.
 */

public abstract class OnClickCallback {

    public abstract void onImageSelected(GalleryAlbum galleryAlbum, ImageView imageView);
}
