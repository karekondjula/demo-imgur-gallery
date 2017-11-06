package team2.imgurgallery.model.retrofit;

import java.util.ArrayList;
import java.util.List;

import team2.imgurgallery.model.GalleryAlbum;

/**
 * Created by d-kareski on 10/20/17.
 */

public class GalleryResponse {

    public List<GalleryAlbum> galleryAlbums;

    public GalleryResponse() {
        galleryAlbums = new ArrayList<>();
    }

    public void appendGalleryResponse(GalleryResponse galleryResponse) {
        if (galleryResponse != null) {
            galleryAlbums.addAll(galleryResponse.galleryAlbums);
        }
    }
}