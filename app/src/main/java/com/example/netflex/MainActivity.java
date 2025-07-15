package com.example.netflex;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.netflex.view.NotificationFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new NotificationFragment())
                    .commit();
        }
    }
}
