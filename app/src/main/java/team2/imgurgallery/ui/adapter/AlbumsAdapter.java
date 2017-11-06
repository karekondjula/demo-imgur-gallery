package team2.imgurgallery.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import team2.imgurgallery.R;
import team2.imgurgallery.model.GalleryAlbum;
import team2.imgurgallery.model.retrofit.GalleryResponse;
import team2.imgurgallery.ui.adapter.viewholder.GalleryViewHolder;
import team2.imgurgallery.ui.callback.OnClickCallback;

/**
 * Created by d-kareski on 10/23/17.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    private Context appContext;

    private GalleryResponse galleryResponse;
    private OnClickCallback onClickCallback;

    public AlbumsAdapter(Context appContext, OnClickCallback onClickCallback) {
        this.appContext = appContext;
        galleryResponse = new GalleryResponse();
        this.onClickCallback = onClickCallback;
    }

    public void setGalleryResponse(GalleryResponse galleryResponse) {
        this.galleryResponse = galleryResponse;
    }

    @Override
    public int getItemCount() {
        return galleryResponse.galleryAlbums.size();
    }

    @Override
    public long getItemId(int position) {
        return galleryResponse.galleryAlbums.get(position).datetime;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new GalleryViewHolder(view, appContext, onClickCallback);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        final GalleryAlbum galleryAlbum = galleryResponse.galleryAlbums.get(position);
        holder.setItem(galleryAlbum);
    }

    @Override
    public void onViewRecycled(GalleryViewHolder holder) {
        super.onViewRecycled(holder);
        holder.getImgurImage().setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }
}