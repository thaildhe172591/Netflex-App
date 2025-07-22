package com.example.netflex.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflex.R;
import com.example.netflex.adapter.CountryFilterAdapter;
import com.example.netflex.adapter.GenreFilterAdapter;
import com.example.netflex.adapter.MovieListAdapter;
import com.example.netflex.adapter.NotificationAdapter;
import com.example.netflex.adapter.SerieListAdapter;
import com.example.netflex.adapter.YearFilterAdapter;
import com.example.netflex.model.Country;
import com.example.netflex.model.Genre;
import com.example.netflex.model.Notification;
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

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private View emptyView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        emptyView = view.findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        fetchNotifications();

        return view;
    }

    private void fetchNotifications() {
        // Giả lập gọi API – thay bằng retrofit thực tế
        RetrofitClient.getApiService(requireContext())
                .getMyNotifications()
                .enqueue(new Callback<PaginatedResponse<Notification>>() {
                    @Override
                    public void onResponse(Call<PaginatedResponse<Notification>> call,
                                           Response<PaginatedResponse<Notification>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Notification> result = response.body().getData();
                            notificationList.clear();
                            notificationList.addAll(result);
                            adapter.notifyDataSetChanged();
                            emptyView.setVisibility(result.isEmpty() ? View.VISIBLE : View.GONE);
                        } else {
                            Toast.makeText(requireContext(), "Failed to load notifications", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PaginatedResponse<Notification>> call, Throwable t) {
                        Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

