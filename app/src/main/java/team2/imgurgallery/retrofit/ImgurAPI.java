package team2.imgurgallery.retrofit;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;
import team2.imgurgallery.model.retrofit.GalleryResponse;

/**
 * Created by d-kareski on 10/20/17.
 */

public interface ImgurAPI {

    String SECTION_HOT = "hot";
    String SECTION_TOP = "top";
    String SECTION_USER = "user";

    String server = "https://api.imgur.com";

//    @GET("/3/gallery/{section}/{sort}/{window}/{page}.json")
//    Observable<GalleryResponse> getGallery(
//            @Header("Authorization") String auth,
//            @Path("section") String section,
//            @Path("sort") String sort,
//            @Path("window") String window,
//            @Path("page") int page
//    );

    @GET("/3/gallery/{section}/{sort}/{window}/{page}")
    void getGallery(
            @Header("Authorization") String auth,
            @Path("section") String section, // hot, top, user
            @Path("sort") String sort, // viral | top | time | rising
            @Path("window") String window, // day | week | month | year | all
            @Path("page") int page,
            @Query("showViral") boolean showViral,
            Callback<GalleryResponse> cb
    );

//    @GET("/3/gallery/image/{id}")
//    Observable<GalleryResponse> getGallery(@Path("page") String page);
}