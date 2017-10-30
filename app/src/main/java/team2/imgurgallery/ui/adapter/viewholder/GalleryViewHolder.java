package team2.imgurgallery.ui.adapter.viewholder;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import team2.imgurgallery.R;
import team2.imgurgallery.model.GalleryAlbum;
import team2.imgurgallery.ui.activity.ImageActivity;
import team2.imgurgallery.ui.activity.MainActivity;

/**
 * Created by d-kareski on 10/23/17.
 */

public class GalleryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgur_image)
    ImageView imgurImage;

    @BindView(R.id.imgur_description)
    TextView description;

    private Context mContext;
    private GalleryAlbum galleryAlbum;

    public GalleryViewHolder(View itemView, Context mContext) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (galleryAlbum != null) {
                    // TODO some albums have no images ?!?!?!??!?!? dafuq with those (DOT title Primer)
                    Intent intent = ImageActivity.createIntent(mContext, galleryAlbum);

                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation((MainActivity) mContext, imgurImage, "transition_event_image");
                    mContext.startActivity(intent, options.toBundle());
                }
            }
        });
        this.mContext = mContext;
    }

    public void setItem(GalleryAlbum galleryAlbum) {
        this.galleryAlbum = galleryAlbum;

        String imageUrl = null;
        String imageDescription = null;
        if (galleryAlbum.isAlbum) {
            if (galleryAlbum.images.size() > 0 && galleryAlbum.images.get(0) != null) {
                imageUrl = galleryAlbum.images.get(0).link;
                imageDescription = galleryAlbum.images.get(0).description;
            }
        } else {
            imageUrl = galleryAlbum.link;
            imageDescription = galleryAlbum.description;
        }

        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(mContext)
                    .load(imageUrl)
                    .fitCenter()
                    .override(200, 600)
                    .into(imgurImage);
        }

        if (!TextUtils.isEmpty(imageDescription)) {
            description.setVisibility(View.VISIBLE);
            description.setText(imageDescription);
        } else {
            description.setVisibility(View.GONE);
        }
    }
}