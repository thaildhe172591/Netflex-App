package com.example.netflex.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflex.R;
import com.example.netflex.adapter.CountryFilterAdapter;
import com.example.netflex.adapter.GenreFilterAdapter;
import com.example.netflex.adapter.MovieListAdapter;
import com.example.netflex.adapter.YearFilterAdapter;
import com.example.netflex.model.Country;
import com.example.netflex.model.Genre;
import com.example.netflex.model.Movie;
import com.example.netflex.model.PaginatedResponse;
import com.example.netflex.network.ApiService;
import com.example.netflex.network.CountryRetrofitClient;
import com.example.netflex.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListFragment extends Fragment  {
    private RecyclerView recyclerGenreFilter, recyclerMovies, recyclerCountryFilter, recyclerYearFilter;
    private MovieListAdapter movieAdapter;
    private Button btnNewest;
    private List<Integer> selectedGenreIds = new ArrayList<>();
    private String selectedCountryCode = "";
    private Integer selectedYear = null;
    private boolean sortNewest = false;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        recyclerGenreFilter = view.findViewById(R.id.recyclerGenreFilter);
        recyclerGenreFilter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerCountryFilter = view.findViewById(R.id.recyclerCountryFilter);
        recyclerCountryFilter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerYearFilter = view.findViewById(R.id.recyclerYearFilter);
        recyclerYearFilter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        btnNewest = view.findViewById(R.id.btnNewest);
        btnNewest.setOnClickListener(v -> {
            sortNewest = !sortNewest;
            btnNewest.setText(sortNewest ? "Newest ↓" : "Default");
            fetchMovies();
        });

        setupYearFilter();
        fetchGenres();
        fetchCountries();

        recyclerMovies = view.findViewById(R.id.recyclerMovies);
        recyclerMovies.setLayoutManager(new GridLayoutManager(getContext(), 2));

        movieAdapter = new MovieListAdapter(movieList, movie -> {
            Fragment fragment = new MovieDetailFragment();
            Bundle args = new Bundle();
            args.putLong("movie_id", movie.getId());
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerMovies.setAdapter(movieAdapter);

        fetchMovies();

        return view;
    }

    private void fetchGenres() {
        RetrofitClient.getApiService(requireContext())
                .getGenres("", "name", 1, 20)
                .enqueue(new Callback<PaginatedResponse<Genre>>() {
                    @Override
                    public void onResponse(Call<PaginatedResponse<Genre>> call, Response<PaginatedResponse<Genre>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Genre> genreList = response.body().getData();
                            Log.d("Genres", "Total: " + genreList.size());
                            GenreFilterAdapter adapter = new GenreFilterAdapter(genreList, selectedGenres -> {
                                selectedGenreIds = selectedGenres.stream().map(Genre::getId).collect(Collectors.toList());
                                onFilterChanged();
                            });
                            recyclerGenreFilter.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<PaginatedResponse<Genre>> call, Throwable t) {
                        Log.e("MovieListActivity", "Error fetching genres: " + t.getMessage());
                    }
                });
    }

    private static final List<String> HIGHLIGHTED_COUNTRIES = Arrays.asList("USA", "KOR", "JPN", "CHN", "FRA", "GBR", "IND", "VNM", "THA", "HKG");

    private void fetchCountries() {
        ApiService.CountryApiService countryApi = CountryRetrofitClient.getClient().create(ApiService.CountryApiService.class);

        countryApi.getAllCountries().enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Country> allCountries = response.body();

                    List<Country> filtered = new ArrayList<>();
                    for (Country c : allCountries) {
                        if (HIGHLIGHTED_COUNTRIES.contains(c.getCca3())) {
                            filtered.add(c);
                        }
                    }

                    CountryFilterAdapter adapter = new CountryFilterAdapter(filtered, selectedCountries -> {
                        Country selected = selectedCountries.get(0);
                        selectedCountryCode = selected.getCca3() == null ? "" : selected.getCca3();

                        fetchMovies();
                    });
                    recyclerCountryFilter.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("MovieListActivity", "Error fetching countries: " + t.getMessage());
            }
        });
    }


    private void setupYearFilter() {
        List<Integer> years = new ArrayList<>();
        for (int y = 2025; y >= 2000; y--) {
            years.add(y);
        }
        YearFilterAdapter yearAdapter = new YearFilterAdapter(years, year -> {
            selectedYear = year;
            fetchMovies();
        });
        recyclerYearFilter.setAdapter(yearAdapter);
    }

    private void fetchMovies() {
        String genreParam = selectedGenreIds != null && !selectedGenreIds.isEmpty()
                ? selectedGenreIds.stream().map(String::valueOf).collect(Collectors.joining(","))
                : "";

        String sortBy = sortNewest ? "createdat_desc" : "title";


        RetrofitClient.getApiService(requireContext())
                .getFilteredMovies(
                        "",
                        genreParam,
                        selectedCountryCode,
                        "",
                        "",
                        sortBy,
                        selectedYear,
                        1,
                        20
                )
                .enqueue(new Callback<PaginatedResponse<Movie>>() {
                    @Override
                    public void onResponse(Call<PaginatedResponse<Movie>> call, Response<PaginatedResponse<Movie>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            movieList = response.body().getData();
                            movieAdapter.setMovieList(movieList);
                        } else {
                            Log.e("MovieListActivity", "Empty or error response: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<PaginatedResponse<Movie>> call, Throwable t) {
                        Log.e("MovieListActivity", "Error fetching movies: " + t.getMessage());
                    }
                });
    }

    private void onFilterChanged() {
        fetchMovies();
    }

}
