package team2.imgurgallery.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import team2.imgurgallery.R;
import team2.imgurgallery.model.GalleryImage;
import team2.imgurgallery.ui.adapter.viewholder.ImageViewHolder;

/**
 * Created by d-kareski on 10/23/17.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private Context mContext;

    private ArrayList<GalleryImage> images;

    public ImagesAdapter(Context c) {
        mContext = c;
        images = new ArrayList<>();
    }

    public void setImages(ArrayList<GalleryImage> images) {
        this.images = images;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public long getItemId(int position) {
        return images.get(position).dateTime;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final GalleryImage galleryImage = images.get(position);

        if (position == 0) {
            holder.getView().setTransitionName("transition_event_image");
        } else {
            holder.getView().setTransitionName("");
        }
        holder.setItem(galleryImage);
    }
}