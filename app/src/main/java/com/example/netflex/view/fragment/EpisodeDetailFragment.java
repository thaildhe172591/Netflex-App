package com.example.netflex.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflex.R;
import com.example.netflex.adapter.ActorAdapter;
import com.example.netflex.adapter.EpisodesAdapter;
import com.example.netflex.model.Actor;
import com.example.netflex.model.Episode;
import com.example.netflex.model.EpisodeDetail;
import com.example.netflex.model.PaginatedResponse;
import com.example.netflex.network.ApiService;
import com.example.netflex.network.RetrofitClient;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodeDetailFragment extends Fragment {

    private StandardGSYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;

    private TextView textEpisodeTitle, textEpisodeMeta, textOverview, btnNextText, btnPreText, textSerieLabel, btnMore;
    private RecyclerView rvActors, rvEpisodes;
    private EpisodesAdapter episodesAdapter;
    private View btnPrev, btnNext;


    private ApiService apiService;
    private long currentEpisodeId = 0;

    public EpisodeDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode_detail, container, false);

        videoPlayer = view.findViewById(R.id.video_player);
        textEpisodeTitle = view.findViewById(R.id.textEpisodeTitle);
        textEpisodeMeta = view.findViewById(R.id.textEpisodeMeta);
        textOverview = view.findViewById(R.id.textOverview);
        rvActors = view.findViewById(R.id.rvActors);
        rvEpisodes = view.findViewById(R.id.rvEpisodes);
        btnPrev = view.findViewById(R.id.btnPrev);
        btnNext = view.findViewById(R.id.btnNext);
        btnNextText = view.findViewById(R.id.btnNextText);
        btnPreText = view.findViewById(R.id.btnPrevText);
        btnMore = view.findViewById(R.id.btnMore);
        textSerieLabel = view.findViewById(R.id.textEpisodesLabel);

        apiService = RetrofitClient.getApiService(getContext());

        currentEpisodeId = getArguments() != null ? getArguments().getLong("episode_id", 0L) : 0L;

        if (currentEpisodeId > 0) {
            loadEpisodeDetails(currentEpisodeId);
        }

        return view;
    }

    private void loadEpisodeDetails(long episodeId) {
        apiService.getEpisodeDetails(episodeId).enqueue(new Callback<EpisodeDetail>() {
            @Override
            public void onResponse(Call<EpisodeDetail> call, Response<EpisodeDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EpisodeDetail detail = response.body();
                    currentEpisodeId = detail.getId();

                    textEpisodeTitle.setText("Episode "+ detail.getEpisodeNumber()+ ": "+detail.getName());
                    textSerieLabel.setText("Serie: "+detail.getSeriesName());
                    btnMore.setOnClickListener(v -> {
                        SerieDetailFragment fragment = new SerieDetailFragment();
                        Bundle args = new Bundle();
                        args.putLong("series_id", detail.getSeriesId());
                        fragment.setArguments(args);

                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    });

                    String release = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(detail.getAirDate());
                    String meta = "Release: " + release + "   " + detail.getRuntime() + " mins";
                    textEpisodeMeta.setText(meta);

                    textOverview.setText(detail.getOverview());

                    setupPlayer(detail.getVideoUrl());

                    List<Actor> actors = detail.getActors();
                    rvActors.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    rvActors.setAdapter(new ActorAdapter(actors, actor -> {
                        Fragment actorDetailFragment = new com.example.netflex.view.fragment.ActorDetailFragment();
                        Bundle args = new Bundle();
                        args.putLong("actor_id", actor.getId());
                        actorDetailFragment.setArguments(args);
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, actorDetailFragment)
                                .addToBackStack(null)
                                .commit();
                    }));
                    loadEpisodes(detail.getSeriesId(), episodeId);
                } else {
                    Toast.makeText(getContext(), "Episode not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EpisodeDetail> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEpisodes(long serieId, long episodeId) {
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
                            var list = response.body().getData();
                            episodesAdapter.setEpisodeList(list);
                            var ids = list.stream().map(Episode::getId).collect(Collectors.toList());
                            int index = ids.indexOf(episodeId);
                            long prev = 0, next = 0;
                            if (index != -1) {
                                if (index > 0) {
                                    prev = ids.get(index - 1);
                                }
                                if (index < ids.size() - 1) {
                                    next = ids.get(index + 1);
                                }
                            }
                            setupNavigation(prev, next);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PaginatedResponse<Episode>> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupNavigation(long prev, long next) {
        if(prev != 0){
            btnPrev.setOnClickListener(v -> {
                Fragment fragment = new EpisodeDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("episode_id", prev);
                fragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });
            btnPrev.setEnabled(true);
        }else {
            btnPreText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
            btnPrev.setEnabled(false);
        }

        if(next != 0){
            btnNext.setOnClickListener(v -> {
                Fragment fragment = new EpisodeDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("episode_id", next);
                fragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });
            btnNext.setEnabled(true);
        }else {
            btnNextText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
            btnNext.setEnabled(false);
        }
    }

    private void setupPlayer(String videoUrl) {
        videoPlayer.setUp(videoUrl, true, "Episode");
        videoPlayer.getBackButton().setVisibility(View.GONE);
        orientationUtils = new OrientationUtils(getActivity(), videoPlayer);
        orientationUtils.setEnable(true);
        videoPlayer.getFullscreenButton().setOnClickListener(v -> {
            orientationUtils.resolveByClick();
            videoPlayer.startWindowFullscreen(getContext(), false, false);
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
