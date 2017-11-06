package team2.imgurgallery.model.retrofit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import team2.imgurgallery.model.GalleryAlbum;

/**
 * Created by d-kareski on 10/20/17.
 */

public class GalleryResponse implements Parcelable {

    public boolean success;
    public int status;
    public List<GalleryAlbum> galleryAlbums;

    public GalleryResponse() {
        galleryAlbums = new ArrayList<>();
    }

    private GalleryResponse(Parcel in) {
        success = in.readByte() != 0;
        status = in.readInt();
    }

    public static final Creator<GalleryResponse> CREATOR = new Creator<GalleryResponse>() {
        @Override
        public GalleryResponse createFromParcel(Parcel in) {
            return new GalleryResponse(in);
        }

        @Override
        public GalleryResponse[] newArray(int size) {
            return new GalleryResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success ? 1 : 0));
        parcel.writeInt(status);
    }
}