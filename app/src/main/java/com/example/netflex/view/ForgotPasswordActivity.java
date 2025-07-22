package com.example.netflex.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import com.example.netflex.model.ForgotPasswordRequest;
import com.example.netflex.model.ResetPasswordRequest;
import com.example.netflex.network.ApiService;
import com.example.netflex.network.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etOtp;
    private Button btnSendOtp, btnConfirm;
    private TextView tvErrorMessage, tvLogin, tvSignup;
    private ImageView ivTogglePassword;
    private ApiService apiService;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        // Ánh xạ view
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etOtp = findViewById(R.id.et_otp);
        btnSendOtp = findViewById(R.id.btn_send_otp);
        btnConfirm = findViewById(R.id.btn_confirm);
        tvErrorMessage = findViewById(R.id.tv_error_message);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);
        tvLogin = findViewById(R.id.tv_login);
        tvSignup = findViewById(R.id.tv_signup);

        apiService = RetrofitClient.getApiService();

        // Xử lý window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Xử lý toggle password visibility
        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

        // Xử lý nút Send OTP
        btnSendOtp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            clearErrors();

            if (email.isEmpty()) {
                etEmail.setError("Vui lòng nhập email");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Email không hợp lệ");
                return;
            }

            btnSendOtp.setEnabled(false);
            btnSendOtp.setText("Đang gửi...");

            ForgotPasswordRequest request = new ForgotPasswordRequest(email);
            Call<Void> call = apiService.sendOtp(request);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    runOnUiThread(() -> {
                        btnSendOtp.setEnabled(true);
                        btnSendOtp.setText("Send OTP");
                        if (response.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "OTP đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();
                            etOtp.requestFocus();
                        } else {
                            handleApiErrors(response);
                        }
                    });
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    runOnUiThread(() -> {
                        btnSendOtp.setEnabled(true);
                        btnSendOtp.setText("Send OTP");
                        tvErrorMessage.setText("Lỗi kết nối: " + t.getMessage());
                        tvErrorMessage.setVisibility(View.VISIBLE);
                    });
                }
            });
        });

        // Xử lý nút Xác nhận
        btnConfirm.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String otp = etOtp.getText().toString().trim();
            clearErrors();

            if (!validateInputs(email, password, otp)) return;

            btnConfirm.setEnabled(false);
            btnConfirm.setText("Đang xử lý...");

            ResetPasswordRequest request = new ResetPasswordRequest(email, otp, password);
            Call<Void> call = apiService.resetPassword(request);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    runOnUiThread(() -> {
                        btnConfirm.setEnabled(true);
                        btnConfirm.setText("Xác nhận");
                        if (response.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Đặt lại mật khẩu thành công", Toast.LENGTH_LONG).show();
                            navigateToLogin();
                        } else {
                            handleApiErrors(response);
                        }
                    });
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    runOnUiThread(() -> {
                        btnConfirm.setEnabled(true);
                        btnConfirm.setText("Xác nhận");
                        tvErrorMessage.setText("Lỗi kết nối: " + t.getMessage());
                        tvErrorMessage.setVisibility(View.VISIBLE);
                    });
                }
            });
        });

        // Xử lý navigation
        tvLogin.setOnClickListener(v -> navigateToLogin());
        tvSignup.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
    }

    private boolean validateInputs(String email, String password, String otp) {
        boolean isValid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            isValid = false;
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            etPassword.setError("Mật khẩu phải từ 6 đến 20 ký tự");
            isValid = false;
        }

        if (otp.isEmpty() || otp.length() != 6) {
            etOtp.setError("OTP phải có đúng 6 ký tự");
            isValid = false;
        }

        return isValid;
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivTogglePassword.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivTogglePassword.setImageResource(android.R.drawable.ic_menu_view);
        }
        etPassword.setSelection(etPassword.getText().length());
        etPassword.setTypeface(Typeface.DEFAULT);
    }

    private void clearErrors() {
        tvErrorMessage.setText("");
        tvErrorMessage.setVisibility(View.GONE);
        etEmail.setError(null);
        etPassword.setError(null);
        etOtp.setError(null);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
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

                    if (field.equalsIgnoreCase("email")) {
                        etEmail.setError(combinedMsg.toString().trim());
                        knownFieldError = true;
                    } else if (field.equalsIgnoreCase("password")) {
                        etPassword.setError(combinedMsg.toString().trim());
                        knownFieldError = true;
                    } else if (field.equalsIgnoreCase("otp")) {
                        etOtp.setError(combinedMsg.toString().trim());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}