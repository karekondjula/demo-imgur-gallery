package team2.imgurgallery.ui.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import team2.imgurgallery.R;
import team2.imgurgallery.model.CustomImageSizeModel;
import team2.imgurgallery.model.CustomImageSizeModelFutureStudio;
import team2.imgurgallery.model.GalleryImage;
import team2.imgurgallery.utils.GlideApp;

/**
 * Created by d-kareski on 10/23/17.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgur_image)
    ImageView imgurImage;

    @BindView(R.id.imgur_description)
    TextView description;

    private Context mContext;

    private View view;

    public ImageViewHolder(View itemView, Context mContext) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        this.mContext = mContext;
        this.view = itemView;
    }

    public void setItem(GalleryImage galleryImage) {
         if (!TextUtils.isEmpty(galleryImage.link)) {

//             if (galleryImage.link.endsWith(".gif")) {
//                 Glide.with(mContext)
//                         .load(galleryImage.link)
////                         .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
////                         .dontTransform()
//                         .thumbnail(0.1f)
////                         .placeholder(R.drawable. ic_downoading)
////                         .error(R.drawable.ic_downloading_error)
//                         .into(imgurImage);
//             } else {
//                 Glide.with(mContext)
//                         .load(galleryImage.link)
////                         .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
////                         .dontTransform()
//                         .thumbnail(0.6f)
////                    .placeholder(R.drawable. ic_sync_black_24dp)
////                         .error(R.drawable.ic_downoading)
//                         .into(imgurImage);
//             }

             CustomImageSizeModel customImageRequest = new CustomImageSizeModelFutureStudio(galleryImage.link);

             GlideApp.with(mContext)
                     .load(customImageRequest)
                     .fitCenter()
                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                     .placeholder(R.drawable.ic_autorenew_black_18dp)
                     .error(R.drawable.ic_downloading_error)
                     .transition(DrawableTransitionOptions.withCrossFade())
                     .fallback(R.drawable.ic_downoading)
                     .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                     .into(imgurImage);

        } else {
            Log.e(">>", "again joke!!!!!");
        }

        if (!TextUtils.isEmpty(galleryImage.description)) {
            description.setVisibility(View.VISIBLE);
            description.setText(galleryImage.description);
        } else {
            description.setVisibility(View.GONE);
        }
    }

    public View getView() {
        return view;
    }
}