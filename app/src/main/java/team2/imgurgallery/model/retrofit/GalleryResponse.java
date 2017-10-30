package team2.imgurgallery.model.retrofit;

import java.util.ArrayList;
import java.util.List;

import team2.imgurgallery.model.GalleryAlbum;

/**
 * Created by d-kareski on 10/20/17.
 */

public class GalleryResponse {

    public boolean success;
    public int status;
    public List<GalleryAlbum> galleryAlbums;
//    public List<GalleryImage> images;

    public GalleryResponse() {
        galleryAlbums = new ArrayList<>();
//        images = new ArrayList<>();
    }

//    public void add(GalleryAlbum galleryAlbum) {
//        galleryAlbums.add(galleryAlbum);
//    }
}