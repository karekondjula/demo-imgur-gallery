package team2.imgurgallery.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import team2.imgurgallery.R;
import team2.imgurgallery.model.GalleryAlbum;
import team2.imgurgallery.model.GalleryImage;
import team2.imgurgallery.ui.adapter.ImagesAdapter;

/**
 * Created by d-kareski on 10/26/17.
 */

public class ImageActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_DESCRIPTION = "extra_description";
    private static final String EXTRA_UP_VOTES = "extra_up_votes";
    private static final String EXTRA_DOWN_VOTES = "extra_down_votes";
    private static final String EXTRA_SCORE = "extra_score";
    private static final String EXTRA_IMAGES = "extra_images";
    private static final String EXTRA_LINK = "extra_link";

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.up_votes)
    TextView upVotes;

    @BindView(R.id.down_votes)
    TextView downVotes;

    @BindView(R.id.score)
    TextView score;

    @BindView(R.id.images_recycler_view)
    RecyclerView imagesRecyclerView;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);
        unbinder = ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        if (bundle != null) {
            if (bundle.containsKey(EXTRA_TITLE)) {
                title.setText(bundle.getString(EXTRA_TITLE));
            }

            if (bundle.containsKey(EXTRA_UP_VOTES)) {
                upVotes.setText(bundle.getString(EXTRA_UP_VOTES));
            }

            if (bundle.containsKey(EXTRA_DOWN_VOTES)) {
                downVotes.setText(bundle.getString(EXTRA_DOWN_VOTES));
            }

            if (bundle.containsKey(EXTRA_SCORE)) {
                score.setText(bundle.getString(EXTRA_SCORE));
            }

            ArrayList<GalleryImage> images;
            if (bundle.containsKey(EXTRA_IMAGES)) {
                images = bundle.getParcelableArrayList(EXTRA_IMAGES);
            } else {
                GalleryImage galleryImage = new GalleryImage();
                galleryImage.link = bundle.getString(EXTRA_LINK);
                galleryImage.description = bundle.getString(EXTRA_DESCRIPTION);
                images = new ArrayList<>();
                images.add(galleryImage);
            }

            imagesRecyclerView.setHasFixedSize(true);
            imagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            ImagesAdapter imagesAdapter = new ImagesAdapter(this);
            imagesAdapter.setImages(images);
            imagesRecyclerView.setAdapter(imagesAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public static Intent createIntent(Context context, GalleryAlbum galleryAlbum) {
        Intent imageActivityIntent = new Intent(context, ImageActivity.class);

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(galleryAlbum.title)) {
            imageActivityIntent.putExtra(ImageActivity.EXTRA_TITLE, galleryAlbum.title);
        }
        if (!TextUtils.isEmpty(galleryAlbum.description)) {
            bundle.putString(ImageActivity.EXTRA_DESCRIPTION, galleryAlbum.description);
        }
        bundle.putString(ImageActivity.EXTRA_UP_VOTES, String.valueOf(galleryAlbum.ups));
        bundle.putString(ImageActivity.EXTRA_DOWN_VOTES, String.valueOf(galleryAlbum.downs));
        bundle.putString(ImageActivity.EXTRA_SCORE, String.valueOf(galleryAlbum.score));

        if (galleryAlbum.isAlbum && galleryAlbum.images != null && galleryAlbum.images.size() > 0) {
            bundle.putParcelableArrayList(ImageActivity.EXTRA_IMAGES, (ArrayList<GalleryImage>) galleryAlbum.images);
        } else {
            bundle.putString(ImageActivity.EXTRA_LINK, galleryAlbum.link);
        }

        imageActivityIntent.putExtras(bundle);
        return imageActivityIntent;
    }
}
