package com.example.netflex.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.netflex.R;
import com.example.netflex.model.SignInRequest;
import com.example.netflex.model.SignInResponse;
import com.example.netflex.network.ApiService;
import com.example.netflex.network.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvForgotPassword, tvSignup, tvErrorMessage;
    private ImageView ivTogglePassword;
    private ApiService apiService;
    private boolean isPasswordVisible = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_signin);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvSignup = findViewById(R.id.tv_signup);
        tvErrorMessage = findViewById(R.id.tv_error_message);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);

        apiService = RetrofitClient.getApiService();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Xử lý sự kiện toggle password visibility
        ivTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String deviceId = "12345678";

            // Xóa lỗi cũ
            tvErrorMessage.setVisibility(View.GONE);
            tvErrorMessage.setText("");
            etEmail.setError(null);
            etPassword.setError(null);

            if (email.isEmpty() || password.isEmpty()) {
                tvErrorMessage.setText("Vui lòng nhập đầy đủ email và mật khẩu!");
                tvErrorMessage.setVisibility(View.VISIBLE);
                return;
            }

            SignInRequest request = new SignInRequest(email, password, deviceId);
            Call<SignInResponse> call = apiService.signIn(request);

            call.enqueue(new Callback<SignInResponse>() {
                @Override
                public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        tvErrorMessage.setVisibility(View.GONE);
                    } else {
                        handleApiErrors(response);
                    }
                }

                @Override
                public void onFailure(Call<SignInResponse> call, Throwable t) {
                    tvErrorMessage.setText("Lỗi kết nối: " + t.getMessage());
                    tvErrorMessage.setVisibility(View.VISIBLE);
                }
            });
        });

        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Ẩn mật khẩu
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivTogglePassword.setImageResource(android.R.drawable.ic_menu_view);
            isPasswordVisible = false;
        } else {
            // Hiển thị mật khẩu
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivTogglePassword.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            isPasswordVisible = true;
        }

        // Đặt con trỏ về cuối text
        etPassword.setSelection(etPassword.getText().length());

        // Giữ font cho EditText (tránh font thay đổi)
        etPassword.setTypeface(Typeface.DEFAULT);
    }

    private void handleApiErrors(Response<?> response) {
        try {
            if (response.errorBody() == null) {
                tvErrorMessage.setText("Lỗi: " + response.code());
                tvErrorMessage.setVisibility(View.VISIBLE);
                return;
            }

            String errorJson = response.errorBody().string();
            JSONObject jsonObject = new JSONObject(errorJson);

            boolean knownFieldError = false;

            if (jsonObject.has("errors")) {
                JSONObject errorsObject = jsonObject.getJSONObject("errors");
                Iterator<String> keys = errorsObject.keys();

                while (keys.hasNext()) {
                    String field = keys.next();
                    JSONArray messages = errorsObject.getJSONArray(field);
                    StringBuilder combinedMsg = new StringBuilder();

                    for (int i = 0; i < messages.length(); i++) {
                        combinedMsg.append(messages.getString(i).replace("'", "")).append("\n");
                    }

                    if (field.equalsIgnoreCase("Email")) {
                        etEmail.setError(combinedMsg.toString().trim());
                        knownFieldError = true;
                    } else if (field.equalsIgnoreCase("Password")) {
                        etPassword.setError(combinedMsg.toString().trim());
                        knownFieldError = true;
                    }
                }
            }

            if (!knownFieldError) {
                if (jsonObject.has("detail")) {
                    tvErrorMessage.setText(jsonObject.getString("detail"));
                    tvErrorMessage.setVisibility(View.VISIBLE);
                } else {
                    tvErrorMessage.setText("Lỗi: " + response.code());
                    tvErrorMessage.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvErrorMessage.setText("Lỗi: " + response.code());
            tvErrorMessage.setVisibility(View.VISIBLE);
        }
    }
}