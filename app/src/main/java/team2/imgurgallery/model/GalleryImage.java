package team2.imgurgallery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d-kareski on 10/20/17.
 */
public class GalleryImage implements Parcelable {

    public String id;
    public String title;
    public String description;
    @SerializedName("datetime")
    public long dateTime;
    public String type;
    public boolean animated;
    public int width;
    public int height;
    public int size;
    public int views;
    public long bandwidth;
    public String link;
    @SerializedName("ups")
    public int upVotes;
    @SerializedName("downs")
    public int downVotes;
    public int score;

    public GalleryImage() {

    }

    protected GalleryImage(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        dateTime = in.readLong();
        type = in.readString();
        animated = in.readByte() != 0;
        width = in.readInt();
        height = in.readInt();
        size = in.readInt();
        views = in.readInt();
        bandwidth = in.readLong();
        link = in.readString();
        upVotes = in.readInt();
        downVotes = in.readInt();
        score = in.readInt();
    }

    public static final Creator<GalleryImage> CREATOR = new Creator<GalleryImage>() {
        @Override
        public GalleryImage createFromParcel(Parcel in) {
            return new GalleryImage(in);
        }

        @Override
        public GalleryImage[] newArray(int size) {
            return new GalleryImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeLong(dateTime);
        parcel.writeString(type);
        parcel.writeByte((byte) (animated ? 1 : 0));
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeInt(size);
        parcel.writeInt(views);
        parcel.writeLong(bandwidth);
        parcel.writeString(link);
        parcel.writeInt(upVotes);
        parcel.writeInt(downVotes);
        parcel.writeInt(score);
    }
}
