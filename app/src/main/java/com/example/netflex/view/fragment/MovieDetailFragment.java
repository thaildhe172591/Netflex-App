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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflex.R;
import com.example.netflex.adapter.ActorAdapter;
import com.example.netflex.adapter.GenresAdapter;
import com.example.netflex.adapter.RelatedMoviesAdapter;
import com.example.netflex.model.Genre;
import com.example.netflex.model.Movie;
import com.example.netflex.model.MovieDetail;
import com.example.netflex.model.PaginatedResponse;
import com.example.netflex.model.ReportRequest;
import com.example.netflex.network.ApiService;
import com.example.netflex.network.RetrofitClient;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.*;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment {

    private StandardGSYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;
    private ImageView imagePoster;
    private TextView textTitle, textReleaseInfo,textRatingValue, textOverview;
    private TextView btnFollow, btnReport;
    private RatingBar ratingBar;
    private ApiService apiService;
    private RecyclerView rvActors;
    private RecyclerView rvRelatedMovies;
    private RecyclerView rvGenres;
    private RelatedMoviesAdapter relatedMoviesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Bind views
        videoPlayer = view.findViewById(R.id.video_player);
        imagePoster = view.findViewById(R.id.imagePoster);
        textTitle = view.findViewById(R.id.textTitle);
        textReleaseInfo = view.findViewById(R.id.textReleaseInfo);
        rvGenres = view.findViewById(R.id.rvGenres);
        textRatingValue = view.findViewById(R.id.textRatingValue);
        ratingBar = view.findViewById(R.id.ratingBar);
        textOverview = view.findViewById(R.id.textOverview);
        rvActors = view.findViewById(R.id.rvActors);
        rvRelatedMovies = view.findViewById(R.id.rvRelatedMovies);
        btnFollow = view.findViewById(R.id.btnFollow);
        btnReport = view.findViewById(R.id.btnReport);
        apiService = RetrofitClient.getApiService(getActivity());

        long movieId = getArguments() != null ? getArguments().getLong("movie_id", 0L) : 0L;
        if (movieId > 0) {
            loadMovieDetails(movieId);
            btnReport.setOnClickListener(v -> showReportDialog(movieId));
        }
        return view;
    }

    private void loadMovieDetails(long movieId) {
        apiService.getMovieDetails(movieId).enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetail detail = response.body();

                    textTitle.setText(detail.getTitle());
                    textOverview.setText(detail.getOverview());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(detail.getReleaseDate());

                    textReleaseInfo.setText(formattedDate+ " | " + detail.getRuntime() + " mins | " + detail.getCountryIso());
                    rvGenres.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    rvGenres.setAdapter(new GenresAdapter(detail.getGenres()));

                    // Rating
                    float rating = (float) detail.getAverageRating();
                    ratingBar.setRating(rating);
                    textRatingValue.setText(String.format(Locale.getDefault(), "%.1f", rating));

                    // Poster
                    Glide.with(requireContext())
                            .load(detail.getPosterPath())
                            .into(imagePoster);

                    ActorAdapter adapter = new ActorAdapter(detail.getActors());
                    rvActors.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    rvActors.setAdapter(adapter);

                    // Video
                    setupPlayer(detail.getVideoUrl());
                    setupRelatedMovies(detail);
                } else {
                    Toast.makeText(getContext(), "Movie not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRelatedMovies(MovieDetail detail) {
        rvRelatedMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        relatedMoviesAdapter = new RelatedMoviesAdapter(getActivity());
        rvRelatedMovies.setAdapter(relatedMoviesAdapter);
        relatedMoviesAdapter.setOnMovieClickListener(movie -> {
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
        var genres = detail.getGenres().stream().map(g -> String.valueOf(g.getId())).collect(Collectors.toList());
        apiService.getFilteredMovies(null, String.join(",", genres),
                null, null, null, null, null, 1, 10)
                .enqueue(new Callback<PaginatedResponse<Movie>>() {
            @Override
            public void onResponse(Call<PaginatedResponse<Movie>> call, Response<PaginatedResponse<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    relatedMoviesAdapter.setMovies(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<PaginatedResponse<Movie>> call, Throwable t) {

            }
        });
    }

    private void setupPlayer(String videoUrl) {
        videoPlayer.setUp(videoUrl, true, "Movie");
        videoPlayer.getBackButton().setVisibility(View.GONE);
        orientationUtils = new OrientationUtils(getActivity(), videoPlayer);
        orientationUtils.setEnable(true);
        videoPlayer.getFullscreenButton().setOnClickListener(v -> {
            orientationUtils.resolveByClick();
            videoPlayer.startWindowFullscreen(getContext(), false, false
            );
        });
    }

    private void showReportDialog(long movieId) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_report, null);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Report", (d, which) -> {
                    RadioGroup radioGroup = dialogView.findViewById(R.id.radioReasons);
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    if (selectedId != -1) {
                        RadioButton selectedButton = dialogView.findViewById(selectedId);
                        String reason = selectedButton.getText().toString();
                        submitReport(reason, "https://www.cukhoaito.id.vn/movies/" + movieId);
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
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
        GSYVideoManager.releaseAllVideos();
    }
}
