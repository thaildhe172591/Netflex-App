package com.example.netflex.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflex.R;
import com.example.netflex.adapter.RelatedMoviesForGenreAndActorAdapter;
import com.example.netflex.network.ApiService;
import com.example.netflex.network.RetrofitClient;
import com.example.netflex.model.Actor;
import com.example.netflex.model.Movie;
import com.example.netflex.model.PaginatedResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorDetailFragment extends Fragment {

    private ImageView actorImageView;
    private TextView actorNameTextView;
    private TextView actorBioTextView;
    private RecyclerView rvRelatedMovies;
    private EditText edtSearch;
    private Spinner spinnerSort;

    private RelatedMoviesForGenreAndActorAdapter relatedMoviesAdapter;
    private ApiService apiService;

    public ActorDetailFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actor_detail, container, false);

        actorImageView = view.findViewById(R.id.imgActorPhoto);
        actorNameTextView = view.findViewById(R.id.txtActorName);
        actorBioTextView = view.findViewById(R.id.txtActorBio);
        rvRelatedMovies = view.findViewById(R.id.recyclerFilmography);
        edtSearch = view.findViewById(R.id.edtSearch);
        spinnerSort = view.findViewById(R.id.spinnerSort);


        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Name", "Year", "Rating"}
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        rvRelatedMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        relatedMoviesAdapter = new RelatedMoviesForGenreAndActorAdapter(getContext());
        rvRelatedMovies.setAdapter(relatedMoviesAdapter);
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            fetchRelatedMovies(getActorId(), edtSearch.getText().toString(), getSelectedSort());
            return true;
        });

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchRelatedMovies(getActorId(), edtSearch.getText().toString(), getSelectedSort());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        apiService = RetrofitClient.getApiService(getActivity());

        int actorId = getArguments() != null ? getArguments().getInt("actorId", 1) : 1;
        if (actorId > 0) {
            fetchActorDetails(actorId);
            fetchRelatedMovies(actorId, null, null);
        }
        return view;
    }

    private void fetchActorDetails(int actorId) {
        apiService.getActorById(actorId).enqueue(new Callback<Actor>() {
            @Override
            public void onResponse(@NonNull Call<Actor> call, @NonNull Response<Actor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Actor actor = response.body();
                    actorNameTextView.setText(actor.getName());
                    actorBioTextView.setText(actor.getBiography());

                    Glide.with(requireContext())
                            .load(actor.getImage())
                            .placeholder(R.drawable.ic_placeholder_actor)
                            .into(actorImageView);
                } else {
                    Toast.makeText(getContext(), "Failed to load actor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Actor> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRelatedMovies(int actorId, String search, String sortBy)

    {
        rvRelatedMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        relatedMoviesAdapter = new RelatedMoviesForGenreAndActorAdapter(getActivity());
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
        apiService.getFilteredMovies(
                search,
                null,
                null,
                null,
                String.valueOf(actorId),
                sortBy,
                null,
                1,
                10
        ).enqueue(new Callback<PaginatedResponse<Movie>>() {
            @Override
            public void onResponse(Call<PaginatedResponse<Movie>> call, Response<PaginatedResponse<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    relatedMoviesAdapter.setMovies(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<PaginatedResponse<Movie>> call, Throwable t) {
                Log.e("ActorDetail", "Failed to fetch movies: " + t.getMessage());
            }
        });

    }

    private int getActorId() {
        return getArguments() != null ? getArguments().getInt("actorId", 1) : 1;
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