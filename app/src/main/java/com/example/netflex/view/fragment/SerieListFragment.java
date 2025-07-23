package com.example.netflex.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflex.R;
import com.example.netflex.adapter.CountryFilterAdapter;
import com.example.netflex.adapter.GenreFilterAdapter;
import com.example.netflex.adapter.MovieListAdapter;
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

public class SerieListFragment extends Fragment {

    private RecyclerView recyclerGenreFilter, recyclerSeries, recyclerCountryFilter, recyclerYearFilter;
    private SerieListAdapter serieAdapter;
    private Button btnNewest;

    private List<Integer> selectedGenreIds = new ArrayList<>();
    private List<String> selectedCountryCodes = new ArrayList<>();
    private Integer selectedYear = null;
    private boolean sortNewest = false;
    private List<Serie> serieList = new ArrayList<>();

    private static final List<String> HIGHLIGHTED_COUNTRIES = Arrays.asList(
            "USA", "KOR", "JPN", "CHN", "FRA", "GBR", "IND", "VNM", "THA", "HKG"
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serie_list, container, false);

        recyclerGenreFilter = view.findViewById(R.id.recyclerGenreFilter);
        recyclerGenreFilter.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerCountryFilter = view.findViewById(R.id.recyclerCountryFilter);
        recyclerCountryFilter.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerYearFilter = view.findViewById(R.id.recyclerYearFilter);
        recyclerYearFilter.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        btnNewest = view.findViewById(R.id.btnNewest);
        btnNewest.setOnClickListener(v -> {
            sortNewest = !sortNewest;
            btnNewest.setText(sortNewest ? "Newest ↓" : "Default");
            fetchSeries();
        });

        recyclerSeries = view.findViewById(R.id.recyclerSeries);
        recyclerSeries.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        serieAdapter = new SerieListAdapter(serieList, serie -> {
            Fragment fragment = new SerieDetailFragment();
            Bundle args = new Bundle();
            args.putLong("series_id", serie.getId());
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerSeries.setAdapter(serieAdapter);

        setupYearFilter();
        fetchGenres();
        fetchCountries();
        fetchSeries();

        return view;
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

    private void fetchGenres() {
        RetrofitClient.getApiService(requireContext())
                .getGenres("", "name", 1, 20)
                .enqueue(new Callback<PaginatedResponse<Genre>>() {
                    @Override
                    public void onResponse(Call<PaginatedResponse<Genre>> call, Response<PaginatedResponse<Genre>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Genre> genreList = response.body().getData();
                            Log.d("SerieListFragment", "Genres: " + genreList.size());

                            GenreFilterAdapter adapter = new GenreFilterAdapter(genreList, selectedGenres -> {
                                selectedGenreIds = selectedGenres.stream()
                                        .map(Genre::getId)
                                        .collect(Collectors.toList());
                                onFilterChanged();
                            });
                            recyclerGenreFilter.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<PaginatedResponse<Genre>> call, Throwable t) {
                        Log.e("SerieListFragment", "Error fetching genres: " + t.getMessage());
                    }
                });
    }

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

                        Toast.makeText(requireContext(), "Selected: " + selectedNames, Toast.LENGTH_SHORT).show();
                        fetchSeries();
                    });

                    recyclerCountryFilter.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("SerieListFragment", "Error fetching countries: " + t.getMessage());
            }
        });
    }

    private void fetchSeries() {
        String genreParam = selectedGenreIds != null && !selectedGenreIds.isEmpty()
                ? selectedGenreIds.stream().map(String::valueOf).collect(Collectors.joining(","))
                : "";
        String countryParam = selectedCountryCodes != null && !selectedCountryCodes.isEmpty()
                ? String.join(",", selectedCountryCodes)
                : "";

        String sortBy = sortNewest ? "lastAirDate_desc" : "name";

        RetrofitClient.getApiService(requireContext())
                .getFilteredSeries(
                        "", // search query
                        genreParam,
                        countryParam,
                        "", // status
                        sortBy,
                        selectedYear,
                        1, // page
                        20 // size
                )
                .enqueue(new Callback<PaginatedResponse<Serie>>() {
                    @Override
                    public void onResponse(Call<PaginatedResponse<Serie>> call, Response<PaginatedResponse<Serie>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            serieList = response.body().getData();
                            serieAdapter.setSerieList(serieList);
                        } else {
                            Log.e("SerieListFragment", "Failed or empty series: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<PaginatedResponse<Serie>> call, Throwable t) {
                        Log.e("SerieListFragment", "Error fetching series: " + t.getMessage());
                    }
                });
    }

    private void onFilterChanged() {
        fetchSeries();
    }
}
