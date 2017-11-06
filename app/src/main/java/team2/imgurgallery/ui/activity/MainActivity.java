package team2.imgurgallery.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;
import team2.imgurgallery.R;
import team2.imgurgallery.model.GalleryAlbum;
import team2.imgurgallery.model.retrofit.GalleryResponse;
import team2.imgurgallery.retrofit.ApiHandler;
import team2.imgurgallery.retrofit.ImgurAPI;
import team2.imgurgallery.ui.adapter.AlbumsAdapter;
import team2.imgurgallery.ui.callback.OnClickCallback;
import team2.imgurgallery.ui.callback.UiCallback;
import team2.imgurgallery.ui.fragment.DialogFragmentAbout;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    @BindView(R.id.grid_view)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private AlbumsAdapter adapter;

    private String section = ImgurAPI.SECTION_HOT;
    private boolean isViralChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context appContext = getApplication().getApplicationContext();

        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);

            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                    toolbar, R.string.drawer_open, R.string.drawer_close) {

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };
            drawerLayout.addDrawerListener(actionBarDrawerToggle);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.settings:
                            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivity(settingsIntent);
                            break;
                        case R.id.about:
                            DialogFragmentAbout editNameDialog = new DialogFragmentAbout();
                            editNameDialog.show(getFragmentManager(), DialogFragmentAbout.TAG);
                            break;
                    }

                    return false;
                }
            });
        }

        BottomNavigationView bottomNavigationView = ButterKnife.findById(this, R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                new Thread() {
                    @Override
                    public void run() {
                        if (item.getItemId() == R.id.hot) {
                            section = ImgurAPI.SECTION_HOT;
                        } else if (item.getItemId() == R.id.top) {
                            section = ImgurAPI.SECTION_TOP;
                        } else if (item.getItemId() == R.id.user) {
                            section = ImgurAPI.SECTION_USER;
                        }
                        fetchGallery(section, isViralChecked);
                        recyclerView.smoothScrollToPosition(0);

                    }
                }.start();

                return true;
            }
        });

        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        sglm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        sglm.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(sglm);
        adapter = new AlbumsAdapter(appContext, onClickCallback);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                int lastVisible = 0;
                if (lm instanceof LinearLayoutManager) {
                    lastVisible = ((LinearLayoutManager) lm).findLastVisibleItemPosition();
                } else if (lm instanceof GridLayoutManager) {
                    lastVisible = ((GridLayoutManager) lm).findLastVisibleItemPosition();
                } else if (lm instanceof StaggeredGridLayoutManager) {
                    int positions[] = new int[2];
                    ((StaggeredGridLayoutManager) lm).findLastCompletelyVisibleItemPositions(positions);
                    lastVisible = positions[positions.length - 1];
                }

                if (Math.abs(lastVisible - adapter.getItemCount()) < 3) {
                    Log.d(">>", "load next page");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // using loader?
        fetchGallery(section, isViralChecked);
    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        GalleryResponse galleryResponse = (GalleryResponse) savedInstanceState.getParcelable("gallery");
////        uiCallback.setCachedGalleryResponse(false);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable("gallery", uiCallback.getGalleryResponse());
//        super.onSaveInstanceState(outState);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        CheckBox viralCheckBox = ButterKnife.findById(MenuItemCompat.getActionView(menu.findItem(R.id.action_viral)), R.id.viral_check_box);
        viralCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isViralChecked = isChecked;

                fetchGallery(section, isViralChecked);
                recyclerView.smoothScrollToPosition(0);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_switch_rv_view) {
            View menuItemView = findViewById(R.id.action_switch_rv_view);
            PopupMenu popup = new PopupMenu(MainActivity.this, menuItemView);
            popup.getMenuInflater().inflate(R.menu.pop_up_rv_view_change, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.list:
                            LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
                            llm.setInitialPrefetchItemCount(3);
                            recyclerView.setLayoutManager(llm);
                            break;
                        case R.id.grid:
                            GridLayoutManager glm = new GridLayoutManager(MainActivity.this, 2);
                            glm.setInitialPrefetchItemCount(4);
                            recyclerView.setLayoutManager(glm);
                            break;
                        case R.id.stag_grid:
                            StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            sglm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                            recyclerView.setLayoutManager(sglm);
                            break;
                    }
                    recyclerView.smoothScrollToPosition(0);
                    return true;
                }
            });
            popup.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchGallery(String section, boolean showViral) {

        final String sort = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(SettingsActivity.PREF_SORT, "viral");
        final String window = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(SettingsActivity.PREF_SORT, "day");

        new Thread() {
            @Override
            public void run() {
                ApiHandler.getInstance().getGalleryCallback(section, sort, window, showViral, uiCallback);
            }
        }.start();
    }

    private UiCallback uiCallback = new UiCallback() {

        private GalleryResponse cachedGalleryResponse;

        @Override
        public GalleryResponse getGalleryResponse() {
            return cachedGalleryResponse;
        }

        @Override
        public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (response.isSuccessful()) {
                        GalleryResponse galleryResponse = response.body();
                        if (galleryResponse != null) {
                            cachedGalleryResponse = galleryResponse;
                            adapter.setGalleryResponse(response.body());
                        } else {
                            if (cachedGalleryResponse != null) {
                                adapter.setGalleryResponse(cachedGalleryResponse);
                            }
                            uiCallback.onFailure(call, null);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public void onFailure(Call<GalleryResponse> call, Throwable t) {
            if (t != null) {
                Toast.makeText(MainActivity.this, String.format(getString(R.string.problem_), t.getMessage()), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_LONG).show();
            }

            if (cachedGalleryResponse != null) {
                adapter.setGalleryResponse(cachedGalleryResponse);
            }
        }
    };

    private OnClickCallback onClickCallback = new OnClickCallback() {

        @Override
        public void onImageSelected(GalleryAlbum galleryAlbum, ImageView imageView) {
            if (galleryAlbum != null) {
                Intent intent = ImageActivity.createIntent(MainActivity.this, galleryAlbum);

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(MainActivity.this, imageView, "transition_event_image");
                startActivity(intent, options.toBundle());
            }
        }
    };
}