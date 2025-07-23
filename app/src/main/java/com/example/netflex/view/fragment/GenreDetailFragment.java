package com.example.netflex.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflex.R;
import com.example.netflex.adapter.RelatedMoviesForGenreAndActorAdapter;
import com.example.netflex.adapter.RelatedSeriesForGenreAndActorAdapter;
import com.example.netflex.model.Movie;
import com.example.netflex.model.PaginatedResponse;
import com.example.netflex.model.Serie;
import com.example.netflex.network.ApiService;
import com.example.netflex.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreDetailFragment extends Fragment {

    private TextView txtGenreTitle;
    private RecyclerView recyclerGenre;
    private RelatedMoviesForGenreAndActorAdapter movieAdapter;
    private RelatedSeriesForGenreAndActorAdapter seriesAdapter;
    private ApiService apiService;
    private EditText edtSearch;
    private Spinner spinnerSort;
    private Button btnMovie, btnSeries;

    private boolean showingMovies = true;

    private int genreId;
    private String genreName;

    public static GenreDetailFragment newInstance(int genreId, String genreName) {
        GenreDetailFragment fragment = new GenreDetailFragment();
        Bundle args = new Bundle();
        args.putInt("genre_id", genreId);
        args.putString("genre_name", genreName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre_detail, container, false);
        txtGenreTitle = view.findViewById(R.id.txtGenreName);
        recyclerGenre = view.findViewById(R.id.recyclerMovies);
        edtSearch = view.findViewById(R.id.edtSearch);
        spinnerSort = view.findViewById(R.id.spinnerSort);
        btnMovie = view.findViewById(R.id.btnMovie);
        btnSeries = view.findViewById(R.id.btnSeries);

        genreId = getArguments() != null ? getArguments().getInt("genre_id") : 0;
        genreName = getArguments() != null ? getArguments().getString("genre_name") : "";

        txtGenreTitle.setText("Genre: " + genreName);

        recyclerGenre.setLayoutManager(new LinearLayoutManager(getContext()));
        movieAdapter = new RelatedMoviesForGenreAndActorAdapter(getContext());
        seriesAdapter = new RelatedSeriesForGenreAndActorAdapter(getContext());

        apiService = RetrofitClient.getApiService(getActivity());

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Name", "Year", "Rating"}
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            reloadContent();
            return true;
        });

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reloadContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnMovie.setOnClickListener(v -> {
            showingMovies = true;
            updateToggleButtonUI();
            reloadContent();
        });

        btnSeries.setOnClickListener(v -> {
            showingMovies = false;
            updateToggleButtonUI();
            reloadContent();
        });

        updateToggleButtonUI();
        reloadContent();

        return view;
    }

    private void updateToggleButtonUI() {
        if (showingMovies) {
            btnMovie.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
            btnMovie.setTextColor(getResources().getColor(R.color.black));
            btnSeries.setBackgroundTintList(getResources().getColorStateList(R.color.dark_gray));
            btnSeries.setTextColor(getResources().getColor(R.color.white));
        } else {
            btnSeries.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
            btnSeries.setTextColor(getResources().getColor(R.color.black));
            btnMovie.setBackgroundTintList(getResources().getColorStateList(R.color.dark_gray));
            btnMovie.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void reloadContent() {
        if (showingMovies) {
            fetchMovies();
        } else {
            fetchSeries();
        }
    }

    private void fetchMovies() {
        movieAdapter.setOnMovieClickListener(movie -> {
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

        recyclerGenre.setAdapter(movieAdapter);

        apiService.getFilteredMovies(
                edtSearch.getText().toString(), String.valueOf(genreId),
                null, null, null,
                getSelectedSort(), null, 1, 20
        ).enqueue(new Callback<PaginatedResponse<Movie>>() {
            @Override
            public void onResponse(Call<PaginatedResponse<Movie>> call, Response<PaginatedResponse<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieAdapter.setMovies(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<PaginatedResponse<Movie>> call, Throwable t) {
                Log.e("GenreDetail", "Failed to load movies: " + t.getMessage());
            }
        });
    }

    private void fetchSeries() {
        seriesAdapter.setOnMovieClickListener(serie -> {
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

        recyclerGenre.setAdapter(seriesAdapter);

        apiService.getFilteredSeries(
                edtSearch.getText().toString(), String.valueOf(genreId),
                null, null, getSelectedSort(), null, 1, 20
        ).enqueue(new Callback<PaginatedResponse<Serie>>() {
            @Override
            public void onResponse(Call<PaginatedResponse<Serie>> call, Response<PaginatedResponse<Serie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    seriesAdapter.setMovies(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<PaginatedResponse<Serie>> call, Throwable t) {
                Log.e("GenreDetail", "Failed to load series: " + t.getMessage());
            }
        });
    }

    private String getSelectedSort() {
        String selected = spinnerSort.getSelectedItem().toString().toLowerCase();
        switch (selected) {
            case "name": return "name";
            case "year": return "release_year";
            case "rating": return "rating";
            default: return null;
        }
    }
}
