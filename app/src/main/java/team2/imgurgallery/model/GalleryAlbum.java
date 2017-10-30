package team2.imgurgallery.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d-kareski on 10/22/17.
 */
public class GalleryAlbum {

    public GalleryAlbum() {
        this.images = new ArrayList<>();
    }

    public String id;
    public String title;
    public String description;
    public long datetime;
    public String cover;
    @SerializedName("account_url")
    public String accountUrl;
    @SerializedName("account_id")
    public int accountId;
    public String privacy;
    public String layout;
    public int views;
    public String link;
    public int ups;
    public int downs;
    public int points;
    public int score;
    @SerializedName("is_album")
    public boolean isAlbum;
    public String vote;
    @SerializedName("comment_count")
    public int commentCount;
    @SerializedName("imagesCount")
    public int imagesCount;
    public List<GalleryImage> images;
}
