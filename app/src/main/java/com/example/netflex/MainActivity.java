package com.example.netflex;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.netflex.view.fragment.FavoriteFragment;
import com.example.netflex.view.fragment.GenreDetailFragment;
import com.example.netflex.view.fragment.HomeFragment;
import com.example.netflex.view.fragment.ProfileFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar topAppBar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        topAppBar = findViewById(R.id.topAppBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        topAppBar.setNavigationOnClickListener(view -> {
            drawerLayout.openDrawer(navigationView);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            handleNavigationItem(item);
            drawerLayout.closeDrawer(navigationView);
            return true;
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            handleNavigationItem(item);
            return true;
        });

        if (savedInstanceState == null) {
//            switchFragment(new HomeFragment());

//            Fragment movieDetailFragment = new com.example.netflex.view.fragment.MovieDetailFragment();
//            Bundle args = new Bundle();
//            args.putLong("movie_id", 1);
//            movieDetailFragment.setArguments(args);
//            switchFragment(movieDetailFragment);

//            Fragment serieDetailFragment = new com.example.netflex.view.fragment.SerieDetailFragment();
//            Bundle args = new Bundle();
//            args.putLong("series_id", 15);
//            serieDetailFragment.setArguments(args);
//            switchFragment(serieDetailFragment);

//            Fragment actorDetailFragment = new com.example.netflex.view.fragment.ActorDetailFragment();
//            Bundle args = new Bundle();
//            args.putLong("actor_id", 3);
//            actorDetailFragment.setArguments(args);
//            switchFragment(actorDetailFragment);

//            Fragment genreDetailFragment = GenreDetailFragment.newInstance(1, "Action");
//            switchFragment(genreDetailFragment);

            Fragment movieListFragment = new com.example.netflex.view.fragment.GenreDetailFragment();
            switchFragment(movieListFragment);
//            topAppBar.setTitle("Movie Detail");
        }
    }

    private void handleNavigationItem(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            switchFragment(new HomeFragment());
            topAppBar.setTitle("Home");
        } else if (id == R.id.nav_favorite) {
            switchFragment(new FavoriteFragment());
            topAppBar.setTitle("Favorites");
        } else if (id == R.id.nav_profile) {
            switchFragment(new ProfileFragment());
            topAppBar.setTitle("Profile");
        }
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}