package com.example.netflex.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.netflex.R;

public class NotificationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvContent = findViewById(R.id.tvDetailContent);
        TextView tvTime = findViewById(R.id.tvDetailTime);

        // Nhận data từ Intent
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String createdAt = getIntent().getStringExtra("createdAt");

        // Set data lên View
        tvTitle.setText(title);
        tvContent.setText(content);
        tvTime.setText(createdAt);
    }
}
