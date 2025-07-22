package com.example.netflex.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflex.R;
import com.example.netflex.adapter.EpisodesAdapter;
import com.example.netflex.adapter.GenresAdapter;
import com.example.netflex.adapter.RelatedSeriesAdapter;
import com.example.netflex.model.Episode;
import com.example.netflex.model.PaginatedResponse;
import com.example.netflex.model.Serie;
import com.example.netflex.model.SerieDetail;
import com.example.netflex.network.ApiService;
import com.example.netflex.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerieDetailFragment extends Fragment {

    private ImageView imagePoster;
    private TextView textTitle, textReleaseInfo, textRatingValue, textOverview, btnFollow, btnReport;
    private RatingBar ratingBar;
    private RecyclerView rvGenres, rvEpisodes, rvRelated;
    private EpisodesAdapter episodesAdapter;
    private RelatedSeriesAdapter relatedAdapter;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serie_detail, container, false);

        imagePoster = view.findViewById(R.id.imagePoster);
        textTitle = view.findViewById(R.id.textTitle);
        textReleaseInfo = view.findViewById(R.id.textReleaseInfo);
        textRatingValue = view.findViewById(R.id.textRatingValue);
        ratingBar = view.findViewById(R.id.ratingBar);
        textOverview = view.findViewById(R.id.textOverview);
        btnFollow = view.findViewById(R.id.btnFollow);
        btnReport = view.findViewById(R.id.btnReport);
        rvGenres = view.findViewById(R.id.rvGenres);
        rvEpisodes = view.findViewById(R.id.rvEpisodes);
        rvRelated = view.findViewById(R.id.rvRelated);

        apiService = RetrofitClient.getApiService(getContext());

        long seriesId = getArguments() != null ? getArguments().getLong("series_id", 0L) : 0L;
        if (seriesId > 0) {
            loadSerieDetail(seriesId);
        }

        return view;
    }

    private void loadSerieDetail(long seriesId) {
        apiService.getSerieDetails(seriesId).enqueue(new Callback<SerieDetail>() {
            @Override
            public void onResponse(@NonNull Call<SerieDetail> call, @NonNull Response<SerieDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SerieDetail detail = response.body();

                    textTitle.setText(detail.getName());
                    textOverview.setText(detail.getOverview());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(detail.getFirstAirDate());
                    textReleaseInfo.setText(formattedDate + " | " + detail.getCountryIso());

                    Glide.with(requireContext()).load(detail.getPosterPath()).into(imagePoster);

                    float rating = (float) detail.getAverageRating();
                    ratingBar.setRating(rating);
                    textRatingValue.setText(String.format(Locale.getDefault(), "%.1f", rating));

                    rvGenres.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    rvGenres.setAdapter(new GenresAdapter(detail.getGenres()));
                    loadEpisodes(seriesId);
                    setupRelated(detail);
                } else {
                    Toast.makeText(getContext(), "Series not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SerieDetail> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEpisodes(long serieId) {
        episodesAdapter = new EpisodesAdapter(new ArrayList<>());
        rvEpisodes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEpisodes.setAdapter(episodesAdapter);
        episodesAdapter.setOnEpisodeClickListener(episode -> {
            Fragment fragment = new EpisodeDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putLong("episode_id", episode.getId());
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        apiService.getEpisodesForSeries(serieId, 1, 50, "")
                .enqueue(new Callback<PaginatedResponse<Episode>>() {
                    @Override
                    public void onResponse(@NonNull Call<PaginatedResponse<Episode>> call, @NonNull Response<PaginatedResponse<Episode>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            episodesAdapter.setEpisodeList(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PaginatedResponse<Episode>> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupRelated(SerieDetail detail) {
        rvRelated.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        relatedAdapter = new RelatedSeriesAdapter(getContext());
        relatedAdapter.setOnSerieClickListener(serie -> {
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
        rvRelated.setAdapter(relatedAdapter);
        List<String> genreIds = detail.getGenres().stream().map(g -> String.valueOf(g.getId())).collect(Collectors.toList());
        apiService.getFilteredSeries(null, String.join(",", genreIds), null, null, null, null, 1, 10)
                .enqueue(new Callback<PaginatedResponse<Serie>>() {
                    @Override
                    public void onResponse(Call<PaginatedResponse<Serie>> call, Response<PaginatedResponse<Serie>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            relatedAdapter.setSeries(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<PaginatedResponse<Serie>> call, Throwable t) {
                        // Silent fail
                    }
                });
    }
}
