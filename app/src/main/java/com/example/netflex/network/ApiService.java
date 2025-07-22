package com.example.netflex.network;

import com.example.netflex.model.Movie;
import com.example.netflex.model.ReportRequest;
import com.example.netflex.model.Serie;
import com.example.netflex.model.MovieDetail;
import com.example.netflex.model.FollowRequest;
import com.example.netflex.model.ReviewRequest;
import com.example.netflex.model.SerieDetail; // Added import
import com.example.netflex.model.Episode; // Added import

import java.util.List;
import com.example.netflex.model.SignInRequest;
import com.example.netflex.model.SignInResponse;
import com.example.netflex.model.SignUpRequest;
import com.example.netflex.model.SignUpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.example.netflex.model.PaginatedResponse;
import com.example.netflex.model.Genre;
import com.example.netflex.model.Country;

import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/signin")
    Call<SignInResponse> signIn(@Body SignInRequest request);

    @POST("users")
    Call<SignUpResponse> signUp(@Body SignUpRequest request);

    @GET("genres")
    Call<PaginatedResponse<Genre>> getGenres(
            @Query("search") String search,
            @Query("sortby") String sortBy,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );
    // This nested interface seems unusual, but left as is.
    public interface CountryApiService {
        @GET("all?fields=name,flags,cca3")
        Call<List<Country>> getAllCountries();
    }

    @GET("movies")
    Call<PaginatedResponse<Movie>> getFilteredMovies(
            @Query("search") String search,
            @Query("genres") String genres,
            @Query("countries") String countries,
            @Query("keywords") String keywords,
            @Query("sortby") String sortBy,
            @Query("year") Integer year,
            @Query("pageindex") int pageIndex,
            @Query("pagesize") int pageSize
    );

    @GET("series")
    Call<PaginatedResponse<Serie>> getFilteredSeries(
            @Query("search") String search,
            @Query("genres") String genres,
            @Query("countries") String countries,
            @Query("keywords") String keywords,
            @Query("sortby") String sortBy,
            @Query("year") Integer year,
            @Query("pageindex") int pageIndex,
            @Query("pagesize") int pageSize
    );

    @GET("movies/{id}")
    Call<MovieDetail> getMovieDetails(@Path("id") long movieId);

    @GET("series/{id}")
    Call<SerieDetail> getSerieDetails(@Path("id") long serieId);

    @GET("episodes")
    Call<PaginatedResponse<Episode>> getEpisodesForSeries(
            @Query("seriesId") long seriesId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("search") String search
    );

    @GET("episodes/{id}")
    Call<Episode> getEpisodeDetails(@Path("id") long episodeId);

    @POST("users/report")
    Call<Void> report(@Body ReportRequest reportRequest);
    @POST("users/follow")
    Call<Void> followMovie(@Body FollowRequest followRequest);
    @POST("users/unfollow")
    Call<Void> unfollowMovie(@Body FollowRequest unfollowRequest);

    @POST("users/review")
    Call<Void> reviewMovie(@Body ReviewRequest reviewRequest);

}
