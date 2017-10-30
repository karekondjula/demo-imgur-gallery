package team2.imgurgallery.ui.activity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team2.imgurgallery.R;
import team2.imgurgallery.model.retrofit.GalleryResponse;
import team2.imgurgallery.retrofit.ApiHandler;
import team2.imgurgallery.retrofit.ImgurAPI;
import team2.imgurgallery.ui.adapter.AlbumsAdapter;
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
    private ApiHandler apiHandler;
    private AlbumsAdapter adapter;

    private String section = ImgurAPI.SECTION_HOT;
    private boolean isViralChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        recyclerView.setLayoutManager(sglm);
        adapter = new AlbumsAdapter(MainActivity.this);
        recyclerView.setAdapter(adapter);

        apiHandler = new ApiHandler(uiCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchGallery(section, isViralChecked);
    }

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
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            break;
                        case R.id.grid:
                            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                            break;
                        case R.id.stag_grid:
                            StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            sglm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                            recyclerView.setLayoutManager(sglm);
                            break;
                    }

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
                apiHandler.getGallery(section, sort, window, showViral);
            }
        }.start();
    }

    private UiCallback uiCallback = new UiCallback() {

        @Override
        public void success(final GalleryResponse galleryResponse, Response response) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setGalleryResponse(galleryResponse);
                    adapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public void failure(final RetrofitError error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error != null) {
                        Toast.makeText(MainActivity.this, String.format(getString(R.string.problem_), error), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.unknown_error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };
}