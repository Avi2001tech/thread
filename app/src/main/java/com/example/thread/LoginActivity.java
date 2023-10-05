package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thread.api.ApiService;
import com.example.thread.model.AuthTokenResponse;
import com.example.thread.model.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewError;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewError = findViewById(R.id.textViewError);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (isValidCredentials(username, password)) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://android-messaging.branch.co/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    ApiService apiService = retrofit.create(ApiService.class);

                    LoginRequest loginRequest = new LoginRequest(username, password);

                    // API call
                    Call<AuthTokenResponse> call = apiService.login(loginRequest);
                    call.enqueue(new Callback<AuthTokenResponse>() {
                        @Override
                        public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                            if (response.isSuccessful()) {
                                AuthTokenResponse authTokenResponse = response.body();
                                String authToken = authTokenResponse.getAuthToken();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("auth_token", authToken);
                                editor.apply();

                                Intent intent = new Intent(LoginActivity.this, MessageThreadsActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                textViewError.setVisibility(View.VISIBLE);
                                textViewError.setText("Login failed. Please check your credentials.");
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                            // Handle network errors here
                            textViewError.setVisibility(View.VISIBLE);
                            textViewError.setText("Network error. Please try again later.");
                        }
                    });
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Invalid credentials. Please check your email address.");
                }
            }
        });
    }

    private boolean isValidCredentials(String username, String password) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String reversedEmail = new StringBuilder(username).reverse().toString();
        return username.matches(emailPattern) && password.equals(reversedEmail);
    }
}