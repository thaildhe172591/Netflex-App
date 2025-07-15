package com.example.netflex.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.netflex.model.Notification;
import com.example.netflex.network.ApiService;
import com.example.netflex.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends ViewModel {

    private final MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();

    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    public void loadNotificationsFromApi(String userId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getNotificationsForUser(userId).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    notifications.setValue(response.body());
                } else {
                    notifications.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                notifications.setValue(null);
            }
        });
    }

    public void loadNotificationsFake() {
        List<Notification> list = new ArrayList<>();

        list.add(new Notification("Khuyến mãi HOT",false , "2025-06-12 15:10", "Giảm giá 50% khi xem phim hôm nay."));
        list.add(new Notification("Chào mừng", true, "2025-06-12 14:00","Chào mừng bạn đến với ứng dụng Netflex!" ));
        list.add(new Notification("Thông báo hệ thống",false , "2025-06-11 20:00", "Bảo trì hệ thống từ 01:00 đến 03:00."));

        notifications.setValue(list);
    }

}
