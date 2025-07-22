package com.example.netflex.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.netflex.R;
import com.example.netflex.adapter.RelatedMoviesAdapter;
import com.example.netflex.adapter.RelatedSeriesAdapter;
import com.example.netflex.model.Movie;
import com.example.netflex.model.PaginatedResponse;
import com.example.netflex.model.Serie;
import com.example.netflex.network.ApiService;
import com.example.netflex.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ImageSlider bannerImageSlider;
    private RecyclerView rvMovies, rvSeries;
    private RelatedMoviesAdapter movieAdapter;
    private RelatedSeriesAdapter seriesAdapter;
    private ApiService apiService;
    private List<SlideModel> slideModels;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slideModels = new ArrayList<>();
        bannerImageSlider = view.findViewById(R.id.bannerImageSlider);
        rvMovies = view.findViewById(R.id.rvMovies);
        rvSeries = view.findViewById(R.id.rvSeries);
        apiService = RetrofitClient.getApiService(getActivity());
        movieAdapter = new RelatedMoviesAdapter(getActivity());
        seriesAdapter = new RelatedSeriesAdapter(getActivity());

        movieAdapter.setOnMovieClickListener(movie -> {
            Fragment fragment = new com.example.netflex.view.fragment.MovieDetailFragment();
            Bundle args = new Bundle();
            args.putLong("movie_id", movie.getId());
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        seriesAdapter.setOnSerieClickListener(serie -> {
            SerieDetailFragment fragment = new SerieDetailFragment();
            Bundle args = new Bundle();
            args.putLong("series_id", serie.getId());
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        var btnSeeMoreMovies = view.findViewById(R.id.btnSeeMoreMovies);
        var btnSeeMoreSeries = view.findViewById(R.id.btnSeeMoreSeries);

        btnSeeMoreMovies.setOnClickListener(v -> {});
        btnSeeMoreSeries.setOnClickListener(v -> {});

        setupBanner();
        setupMovieList();
        setupSeriesList();
    }

    private void setupBanner() {
        bannerImageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
    }

    private void setupMovieList() {
        apiService.getFilteredMovies(null, null, null, null, "release_date.desc", null, 1, 9)
                .enqueue(new Callback<PaginatedResponse<Movie>>(){
                    @Override
                    public void onResponse(Call<PaginatedResponse<Movie>> call, Response<PaginatedResponse<Movie>> response) {
                        if (response.isSuccessful()  && response.body() != null) {
                            var list = response.body().getData();
                            movieAdapter.setMovies(list);
                            rvMovies.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.HORIZONTAL, false));
                            rvMovies.setAdapter(movieAdapter);

                            for (int i = 0; i < list.size(); i++) {
                                var movie = list.get(i);
                                slideModels.add(new SlideModel(movie.getPosterPath(), movie.getTitle(), ScaleTypes.CENTER_CROP));
                            }

                            setupBanner();
                        }
                    }
                    @Override
                    public void onFailure(Call<PaginatedResponse<Movie>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupSeriesList() {
        apiService.getFilteredSeries(null, null, null, null, "first_air_date.desc", null, 1, 9)
                .enqueue(new Callback<PaginatedResponse<Serie>>() {
                    @Override
                    public void onResponse(Call<PaginatedResponse<Serie>> call, Response<PaginatedResponse<Serie>> response) {
                        if (response.isSuccessful()  && response.body() != null) {
                            var list = response.body().getData();
                            seriesAdapter.setSeries(list);
                            rvSeries.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            rvSeries.setAdapter(seriesAdapter);
                            for (int i = 0; i < list.size(); i++) {
                                var serie = list.get(i);
                                slideModels.add(new SlideModel(serie.getPosterPath(), serie.getName(), ScaleTypes.CENTER_CROP));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PaginatedResponse<Serie>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
