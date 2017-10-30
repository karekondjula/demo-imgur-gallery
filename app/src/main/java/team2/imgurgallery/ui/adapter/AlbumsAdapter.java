package team2.imgurgallery.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team2.imgurgallery.R;
import team2.imgurgallery.model.GalleryAlbum;
import team2.imgurgallery.model.retrofit.GalleryResponse;
import team2.imgurgallery.ui.adapter.viewholder.GalleryViewHolder;

/**
 * Created by d-kareski on 10/23/17.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    private Context mContext;

    private GalleryResponse galleryResponse;

    public AlbumsAdapter(Context c) {
        mContext = c;
        galleryResponse = new GalleryResponse();
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
        return new GalleryViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        final GalleryAlbum galleryAlbum = galleryResponse.galleryAlbums.get(position);
        holder.setItem(galleryAlbum);
    }
}