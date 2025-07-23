package com.example.netflex.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflex.R;
import com.example.netflex.adapter.EpisodesAdapter;
import com.example.netflex.adapter.GenresAdapter;
import com.example.netflex.adapter.RelatedSeriesAdapter;
import com.example.netflex.model.Episode;
import com.example.netflex.model.Follow;
import com.example.netflex.model.FollowRequest;
import com.example.netflex.model.PaginatedResponse;
import com.example.netflex.model.ReportRequest;
import com.example.netflex.model.Review;
import com.example.netflex.model.ReviewRequest;
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
    private int totalReview = 0;
    private float currentRating = 0f;
    private boolean isFollowing = false;

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
            checkFollowStatus(seriesId);
            btnFollow.setOnClickListener(v -> toggleFollow(seriesId));
        }

        btnReport.setOnClickListener(v -> showReportDialog(seriesId));
        setupRatingBar(seriesId);
        return view;
    }

    private void checkFollowStatus(long serieId) {
        apiService.getFollow(String.valueOf(serieId), "serie").enqueue(new Callback<Follow>() {
            @Override
            public void onResponse(@NonNull Call<Follow> call, @NonNull Response<Follow> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isFollowing = true;
                    updateFollowButton();
                } else {
                    isFollowing = false;
                    updateFollowButton();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Follow> call, @NonNull Throwable t) {
                isFollowing = false;
                updateFollowButton();
            }
        });
    }
    private void updateFollowButton() {
        if (isFollowing) {
            btnFollow.setText("Unfollow");
            btnFollow.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        } else {
            btnFollow.setText("Follow");
            btnFollow.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void toggleFollow(long serieId) {
        if (serieId == 0) return;

        FollowRequest followRequest = new FollowRequest(String.valueOf(serieId), "serie");

        if (isFollowing) {
            // Unfollow
            apiService.unfollow(followRequest).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        isFollowing = false;
                        updateFollowButton();
                        Toast.makeText(getContext(), "Unfollowed successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to unfollow", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Follow
            apiService.follow(followRequest).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        isFollowing = true;
                        updateFollowButton();
                        Toast.makeText(getContext(), "Followed successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to follow", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupRatingBar(long serieId) {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser && rating > 0) {
                    ReviewRequest reviewRequest = new ReviewRequest(String.valueOf(serieId), "serie", (int)rating);
                    var newRating = (currentRating * totalReview + rating) / (totalReview + 1);
                    textRatingValue.setText(String.format(Locale.getDefault(), "%.1f", newRating));
                    apiService.review(reviewRequest).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to submit rating", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
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
                    currentRating = rating;
                    totalReview = detail.getTotalReviews();
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

    private void showReportDialog(long serieId) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_report, null);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Report", (d, which) -> {
                    RadioGroup radioGroup = dialogView.findViewById(R.id.radioReasons);
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    if (selectedId != -1) {
                        RadioButton selectedButton = dialogView.findViewById(selectedId);
                        String reason = selectedButton.getText().toString();
                        submitReport(reason, "https://www.cukhoaito.id.vn/series/" + serieId);
                    } else {
                        Toast.makeText(getContext(), "Please select reason", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }

    private void submitReport(String reason, String description){
        apiService.report(new ReportRequest(reason, description)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getContext(), "Report submitted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
