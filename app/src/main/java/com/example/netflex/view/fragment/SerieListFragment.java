package com.example.netflex.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflex.R;
import com.example.netflex.adapter.CountryFilterAdapter;
import com.example.netflex.adapter.GenreFilterAdapter;
import com.example.netflex.adapter.SerieListAdapter;
import com.example.netflex.adapter.YearFilterAdapter;
import com.example.netflex.model.Country;
import com.example.netflex.model.Genre;
import com.example.netflex.model.Serie;
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

public class SerieListFragment extends AppCompatActivity {
    private RecyclerView recyclerGenreFilter, recyclerSeries, recyclerCountryFilter, recyclerYearFilter;
    private SerieListAdapter serieAdapter;
    private Button btnNewest;
    private List<Integer> selectedGenreIds = new ArrayList<>();
    private List<String> selectedCountryCodes = new ArrayList<>();
    private Integer selectedYear = null;
    private boolean sortNewest = false;
    private List<Serie> serieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_serie_list);

        recyclerGenreFilter = findViewById(R.id.recyclerGenreFilter);
        recyclerGenreFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerCountryFilter = findViewById(R.id.recyclerCountryFilter);
        recyclerCountryFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerYearFilter = findViewById(R.id.recyclerYearFilter);
        recyclerYearFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        btnNewest = findViewById(R.id.btnNewest);
        btnNewest.setOnClickListener(v -> {
            sortNewest = !sortNewest;
            btnNewest.setText(sortNewest ? "Newest ↓" : "Default");
            fetchSeries();
        });
        setupYearFilter();
        fetchGenres();
        fetchCountries();

        recyclerSeries = findViewById(R.id.recyclerSeries);
        recyclerSeries.setLayoutManager(new GridLayoutManager(this, 2));

        serieAdapter = new SerieListAdapter(serieList, serie -> {
            Toast.makeText(this, "Clicked: " + serie.getName(), Toast.LENGTH_SHORT).show();
        });
        recyclerSeries.setAdapter(serieAdapter);

        fetchSeries();
    }

    private void fetchGenres() {
        RetrofitClient.getApiService(this)
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
                        Log.e("SerieListActivity", "Error fetching genres: " + t.getMessage());
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
                        selectedCountryCodes = selectedCountries.stream()
                                .map(Country::getCca3)
                                .collect(Collectors.toList());

                        String selectedNames = selectedCountries.stream()
                                .map(c -> c.getName().getCommon())
                                .collect(Collectors.joining(", "));

                        Toast.makeText(SerieListFragment.this, "Selected: " + selectedNames, Toast.LENGTH_SHORT).show();
                        fetchSeries();
                    });

                    recyclerCountryFilter.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("SerieListActivity", "Error fetching countries: " + t.getMessage());
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
            fetchSeries();
        });
        recyclerYearFilter.setAdapter(yearAdapter);
    }

    private void fetchSeries() {
        String genreParam = selectedGenreIds != null && !selectedGenreIds.isEmpty()
                ? selectedGenreIds.stream().map(String::valueOf).collect(Collectors.joining(","))
                : "";
        String countryParam = selectedCountryCodes != null && !selectedCountryCodes.isEmpty()
                ? String.join(",", selectedCountryCodes)
                : "";

        String sortBy = sortNewest ? "lastAirDate_desc" : "name";

        RetrofitClient.getApiService(this)
                .getFilteredSeries(
                        "",
                        genreParam,
                        countryParam,
                        "",
                        sortBy,
                        selectedYear,
                        1,
                        20
                )
                .enqueue(new Callback<PaginatedResponse<Serie>>() {
                    @Override
                    public void onResponse(Call<PaginatedResponse<Serie>> call, Response<PaginatedResponse<Serie>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            serieList = response.body().getData();
                            serieAdapter.setSerieList(serieList);
                        } else {
                            Log.e("SerieListActivity", "Empty or error response: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<PaginatedResponse<Serie>> call, Throwable t) {
                        Log.e("SerieListActivity", "Error fetching series: " + t.getMessage());
                    }
                });
    }

    private void onFilterChanged() {
        fetchSeries();
    }

}
