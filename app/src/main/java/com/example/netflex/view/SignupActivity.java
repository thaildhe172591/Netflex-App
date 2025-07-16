package com.example.netflex.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
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
import com.example.netflex.model.SignUpRequest;
import com.example.netflex.model.SignUpResponse;
import com.example.netflex.network.ApiService;
import com.example.netflex.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword;
    private ImageView ivTogglePassword, ivToggleConfirmPassword;
    private TextView tvEmailError, tvPasswordError, tvConfirmPasswordError;
    private Button btnSignup;
    private TextView tvSignin;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private ApiService apiService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);
        ivToggleConfirmPassword = findViewById(R.id.iv_toggle_confirm_password);

        tvEmailError = findViewById(R.id.tv_email_error);
        tvPasswordError = findViewById(R.id.tv_password_error);
        tvConfirmPasswordError = findViewById(R.id.tv_confirm_password_error);

        btnSignup = findViewById(R.id.btn_signup);
        tvSignin = findViewById(R.id.tv_signin);

        apiService = RetrofitClient.getApiService();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
        ivToggleConfirmPassword.setOnClickListener(v -> toggleConfirmPasswordVisibility());

        btnSignup.setOnClickListener(v -> attemptSignup());

        tvSignin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });

        // Setup real-time validation
        setupRealTimeValidation();
    }

    private void attemptSignup() {
        // Validate lại một lần nữa trước khi gửi
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        validateEmail(email);
        validatePassword(password);
        validateConfirmPassword(confirmPassword);

        // Kiểm tra form có hợp lệ không
        if (!isFormValid()) {
            return;
        }

        // Disable button để tránh spam
        btnSignup.setEnabled(false);
        btnSignup.setText("Đang đăng ký...");

        // Call API với SignUpResponse
        SignUpRequest request = new SignUpRequest(email, password);
        Log.d("SignUp", "Sending signup request for email: " + email);

        apiService.signUp(request).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                // Re-enable button
                btnSignup.setEnabled(true);
                btnSignup.setText("Đăng ký");

                Log.d("SignUp", "Response code: " + response.code());

                if (response.isSuccessful()) {
                    // Case 1: Response thành công với body
                    if (response.body() != null) {
                        SignUpResponse res = response.body();
                        Log.d("SignUp", "Response body available: " + res.toString());

                        if (res.isSuccess()) {
                            // Thành công với response có data
                            Toast.makeText(SignupActivity.this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            // Thất bại với response có data
                            handleApiError(res);
                        }
                    } else {
                        // Case 2: Response thành công nhưng body null (response rỗng)
                        Log.d("SignUp", "Signup successful with empty response");
                        Toast.makeText(SignupActivity.this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    // Response không thành công (4xx, 5xx)
                    Log.e("SignUp", "Signup failed with code: " + response.code());
                    handleHttpError(response);
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                // Re-enable button
                btnSignup.setEnabled(true);
                btnSignup.setText("Đăng ký");

                Log.e("SignUp", "Network error", t);

                // Kiểm tra nếu lỗi là do JSON parsing
                if (t.getMessage() != null && t.getMessage().contains("End of input")) {
                    Log.d("SignUp", "JSON parsing error - treating as success");
                    Toast.makeText(SignupActivity.this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleApiError(SignUpResponse res) {
        if (res.getDetail() != null && !res.getDetail().isEmpty()) {
            String detail = res.getDetail();
            Log.e("SignUp", "API error: " + detail);

            if (detail.toLowerCase().contains("email")) {
                tvEmailError.setText(detail);
                tvEmailError.setVisibility(View.VISIBLE);
            } else if (detail.toLowerCase().contains("password")) {
                tvPasswordError.setText(detail);
                tvPasswordError.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, detail, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleHttpError(Response<?> response) {
        String errorMessage = "Đăng ký không thành công";

        // Xử lý theo HTTP status code
        switch (response.code()) {
            case 400:
                errorMessage = "Dữ liệu không hợp lệ";
                break;
            case 409:
                errorMessage = "Email đã tồn tại";
                // Hiển thị lỗi ở field email
                tvEmailError.setText("Email đã được sử dụng");
                tvEmailError.setVisibility(View.VISIBLE);
                return; // Không show toast nữa
            case 422:
                errorMessage = "Dữ liệu không hợp lệ";
                break;
            case 500:
                errorMessage = "Lỗi server, vui lòng thử lại sau";
                break;
            default:
                errorMessage = "Lỗi: " + response.code();
        }

        // Thử đọc error body nếu có
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                Log.e("SignUp", "Error body: " + errorBody);

                if (!errorBody.isEmpty()) {
                    // Nếu error body chứa thông tin cụ thể
                    if (errorBody.toLowerCase().contains("email")) {
                        tvEmailError.setText(errorBody);
                        tvEmailError.setVisibility(View.VISIBLE);
                        return;
                    } else if (errorBody.toLowerCase().contains("password")) {
                        tvPasswordError.setText(errorBody);
                        tvPasswordError.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        errorMessage = errorBody;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("SignUp", "Cannot read error body", e);
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void clearErrors() {
        tvEmailError.setVisibility(View.GONE);
        tvPasswordError.setVisibility(View.GONE);
        tvConfirmPasswordError.setVisibility(View.GONE);
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivTogglePassword.setImageResource(android.R.drawable.ic_menu_view);
            isPasswordVisible = false;
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivTogglePassword.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            isPasswordVisible = true;
        }
        etPassword.setSelection(etPassword.getText().length());
        etPassword.setTypeface(Typeface.DEFAULT);
    }

    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivToggleConfirmPassword.setImageResource(android.R.drawable.ic_menu_view);
            isConfirmPasswordVisible = false;
        } else {
            etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivToggleConfirmPassword.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            isConfirmPasswordVisible = true;
        }
        etConfirmPassword.setSelection(etConfirmPassword.getText().length());
        etConfirmPassword.setTypeface(Typeface.DEFAULT);
    }

    private void setupRealTimeValidation() {
        // Email validation
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateEmail(s.toString().trim());
            }
        });

        // Password validation
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validatePassword(s.toString().trim());
                // Validate confirm password nếu đã có text
                if (!etConfirmPassword.getText().toString().isEmpty()) {
                    validateConfirmPassword(etConfirmPassword.getText().toString().trim());
                }
            }
        });

        // Confirm password validation
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateConfirmPassword(s.toString().trim());
            }
        });
    }

    private void validateEmail(String email) {
        if (email.isEmpty()) {
            tvEmailError.setText("Email không được để trống");
            tvEmailError.setVisibility(View.VISIBLE);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvEmailError.setText("Email không đúng định dạng");
            tvEmailError.setVisibility(View.VISIBLE);
        } else {
            tvEmailError.setVisibility(View.GONE);
        }
    }

    private void validatePassword(String password) {
        if (password.isEmpty()) {
            tvPasswordError.setText("Mật khẩu không được để trống");
            tvPasswordError.setVisibility(View.VISIBLE);
        } else if (password.length() < 6) {
            tvPasswordError.setText("Mật khẩu phải từ 6 ký tự trở lên");
            tvPasswordError.setVisibility(View.VISIBLE);
        } else {
            tvPasswordError.setVisibility(View.GONE);
        }
    }

    private void validateConfirmPassword(String confirmPassword) {
        String password = etPassword.getText().toString().trim();
        if (confirmPassword.isEmpty()) {
            tvConfirmPasswordError.setText("Vui lòng xác nhận mật khẩu");
            tvConfirmPasswordError.setVisibility(View.VISIBLE);
        } else if (!password.equals(confirmPassword)) {
            tvConfirmPasswordError.setText("Xác nhận mật khẩu không khớp");
            tvConfirmPasswordError.setVisibility(View.VISIBLE);
        } else {
            tvConfirmPasswordError.setVisibility(View.GONE);
        }
    }

    private boolean isFormValid() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        boolean isEmailValid = !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean isPasswordValid = !password.isEmpty() && password.length() >= 6;
        boolean isConfirmPasswordValid = !confirmPassword.isEmpty() && password.equals(confirmPassword);

        return isEmailValid && isPasswordValid && isConfirmPasswordValid;
    }
}