package team2.imgurgallery.retrofit;

/**
 * Created by d-kareski on 10/20/17.
 */

import android.support.annotation.WorkerThread;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import team2.imgurgallery.model.GalleryAlbum;
import team2.imgurgallery.model.GalleryImage;
import team2.imgurgallery.model.retrofit.GalleryResponse;
import team2.imgurgallery.ui.callback.UiCallback;

public class ApiHandler {

    private static final String CLIEND_ID = "Client-ID 5067c93cab3dd48";

    //    private RestAdapter restAdapter;
    private Retrofit retrofit;

    private static ApiHandler instance;

    public static ApiHandler getInstance() {
        if (instance == null) {
            instance = new ApiHandler();
        }

        return instance;
    }

    private ApiHandler() {

//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .onErrorReturn((Throwable ex) -> {
//                    Log.e("error", ex.getMessage()); //examine error here
//                    return null; //empty object of the datatype
//                })
//                .subscribe(galleryAlbum -> {
//                    if (galleryAlbum != null) {
////                        galleryAlbum.forEach(image -> Log.d("image title ", image.getTitle()));
//                        Log.d("album", "galleryAlbum.id " + galleryAlbum.galleryAlbums.id);
////                        galleryAlbum.galleryAlbums.images.forEach(image -> Log.d("image title ", image.title));
//
//                    }
//                });

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GalleryResponse.class, new JsonDeserializer<GalleryResponse>() {

                    @Override
                    public GalleryResponse deserialize(JsonElement json, Type typeOfT,
                                                       JsonDeserializationContext context) throws JsonParseException {

//                        Log.d("json", json.toString());
                        JsonArray galleryDataJson = json.getAsJsonObject().getAsJsonArray("data");
                        GalleryResponse galleryResponse = new GalleryResponse();

                        GalleryAlbum galleryAlbum;
                        GalleryImage galleryImage;

                        JsonObject jsonObjectGallery, jsonObjectImage;
                        for (JsonElement jsonElement : galleryDataJson) {

                            jsonObjectGallery = jsonElement.getAsJsonObject();
//                            Log.d("json", jsonObjectGallery.toString());
                            galleryAlbum = new GalleryAlbum();
                            galleryAlbum.id = jsonObjectGallery.get("id").getAsString();
                            galleryAlbum.title = jsonObjectGallery.get("title").getAsString();
                            galleryAlbum.link = jsonObjectGallery.get("link").getAsString();
                            if (!jsonObjectGallery.get("description").isJsonNull()) {
                                galleryAlbum.description = jsonObjectGallery.get("description").getAsString();
                            }
                            if (!jsonObjectGallery.get("datetime").isJsonNull()) {
                                galleryAlbum.datetime = jsonObjectGallery.get("datetime").getAsLong();
                            }
                            if (!jsonObjectGallery.get("ups").isJsonNull()) {
                                galleryAlbum.ups = jsonObjectGallery.get("ups").getAsInt();
                            }
                            if (!jsonObjectGallery.get("downs").isJsonNull()) {
                                galleryAlbum.downs = jsonObjectGallery.get("downs").getAsInt();
                            }
                            if (!jsonObjectGallery.get("score").isJsonNull()) {
                                galleryAlbum.score = jsonObjectGallery.get("score").getAsInt();
                            }
                            galleryAlbum.isAlbum = jsonObjectGallery.get("is_album").getAsBoolean();

                            if (jsonObjectGallery.getAsJsonArray("images") != null
                                    && !jsonObjectGallery.getAsJsonArray("images").isJsonNull()) {
                                for (JsonElement jsonImage : jsonObjectGallery.getAsJsonArray("images")) {

//                                    Log.d("json", jsonImage.toString());
                                    jsonObjectImage = jsonImage.getAsJsonObject();
                                    galleryImage = new GalleryImage();
                                    galleryImage.id = jsonObjectImage.get("id").getAsString();
                                    galleryImage.link = jsonObjectImage.get("link").getAsString();

                                    if (!jsonObjectImage.get("description").isJsonNull()) {
                                        galleryImage.description = jsonObjectImage.get("description").getAsString();
                                    }
                                    if (!jsonObjectImage.get("title").isJsonNull()) {
                                        galleryImage.title = jsonObjectImage.get("title").getAsString();
                                    }

                                    galleryAlbum.images.add(galleryImage);
                                }
                            }

                            galleryResponse.galleryAlbums.add(galleryAlbum);
                        }

                        return galleryResponse;
                    }
                })
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(ImgurAPI.server)
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(okHttpClient)
                .build();
    }

    @WorkerThread
    public void getGalleryCallback(String section, String sort, String window, boolean showViral, UiCallback uiCallback) {

        Call<GalleryResponse> call = retrofit.create(ImgurAPI.class).getGallery(
                CLIEND_ID,
                section,
                sort,
                window,
                0,
                showViral
        );
        call.enqueue(new Callback<GalleryResponse>() {
                    @Override
                    public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {
                        uiCallback.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(Call<GalleryResponse> call, Throwable t) {
                        uiCallback.onFailure(call, t);
                    }
                }
        );
    }

//    @WorkerThread
//    public GalleryResponse getGallery(String section, String sort, String window, boolean showViral) {
//        return restAdapter.create(ImgurAPI.class).getGallery(
//                CLIEND_ID,
//                section,
//                sort,
//                window,
//                0,
//                showViral
//        );
//    }
}