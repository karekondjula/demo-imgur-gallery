package team2.imgurgallery.ui.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import team2.imgurgallery.R;
import team2.imgurgallery.model.CustomImageSizeModel;
import team2.imgurgallery.model.CustomImageSizeModelFutureStudio;
import team2.imgurgallery.model.GalleryAlbum;
import team2.imgurgallery.ui.callback.OnClickCallback;
import team2.imgurgallery.utils.GlideApp;

/**
 * Created by d-kareski on 10/23/17.
 */

public class GalleryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgur_image)
    ImageView imgurImage;

    @BindView(R.id.imgur_description)
    TextView description;

    private Context appContext;
    private GalleryAlbum galleryAlbum;

    public GalleryViewHolder(View itemView, Context appContext, final OnClickCallback onClickCallback) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCallback.onImageSelected(galleryAlbum, imgurImage);
            }
        });
        this.appContext = appContext;
    }

    public void setItem(GalleryAlbum galleryAlbum) {
        this.galleryAlbum = galleryAlbum;

        String imageUrl;
        String imageDescription;
        if (galleryAlbum.isAlbum) {
            if (galleryAlbum.images.size() > 0 && galleryAlbum.images.get(0) != null) {
                imageUrl = galleryAlbum.images.get(0).link;
                imageDescription = galleryAlbum.images.get(0).description;
            } else {
                imageUrl = galleryAlbum.link;
                imageDescription = galleryAlbum.description;
            }
        } else {
            imageUrl = galleryAlbum.link;
            imageDescription = galleryAlbum.description;
        }

        if (!TextUtils.isEmpty(imageUrl)) {

            RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model,
                                               Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                    imgurImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return false;
                }
            };

            CustomImageSizeModel customImageRequest = new CustomImageSizeModelFutureStudio(imageUrl);

            GlideApp.with(appContext)
                    .load(customImageRequest)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_autorenew_black_18dp)
                    .error(R.drawable.ic_downloading_error)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .fallback(R.drawable.ic_downoading)
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .listener(requestListener)
                    .into(imgurImage);
        }

        if (!TextUtils.isEmpty(imageDescription)) {
            description.setVisibility(View.VISIBLE);
            description.setText(imageDescription);
        } else {
            description.setVisibility(View.GONE);
        }
    }

    public ImageView getImgurImage() {
        return imgurImage;
    }
}