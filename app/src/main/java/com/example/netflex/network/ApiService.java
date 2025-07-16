package com.example.netflex.network;

import com.example.netflex.model.Movie;
import com.example.netflex.model.Serie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import com.example.netflex.model.PaginatedResponse;
import com.example.netflex.model.Genre;
import com.example.netflex.model.Country;

import retrofit2.http.Query;

public interface ApiService {

    @GET("genres")
    Call<PaginatedResponse<Genre>> getGenres(
            @Query("search") String search,
            @Query("sortby") String sortBy,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );
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

}
