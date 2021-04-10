package com.example.minitwitter.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minitwitter.R;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.MiniTwitterClient;
import com.example.minitwitter.retrofit.MiniTwitterService;
import com.example.minitwitter.retrofit.request.RequestSignUp;
import com.example.minitwitter.retrofit.response.ResponseAuth;
import com.example.minitwitter.ui.tweets.FragmentsActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.minitwitter.common.Constantes.PREF_ACTIVE;
import static com.example.minitwitter.common.Constantes.PREF_CREATED;
import static com.example.minitwitter.common.Constantes.PREF_EMAIL;
import static com.example.minitwitter.common.Constantes.PREF_PHOTOURL;
import static com.example.minitwitter.common.Constantes.PREF_TOKEN;
import static com.example.minitwitter.common.Constantes.PREF_USERNAME;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnSignUp;
    TextView tvGoLogin;
    EditText etUsername, etEmail, etPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        retrofitInit();
        findViews();
        events();
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void findViews() {
        btnSignUp = findViewById(R.id.buttonSignUp);
        tvGoLogin = findViewById(R.id.textViewGoLogin);
        etUsername = findViewById(R.id.editTextUserName);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
    }

    private void events() {
        btnSignUp.setOnClickListener(this);
        tvGoLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.buttonSignUp:
                gotToSignUp();
                break;
            case R.id.textViewGoLogin:
                goToLogin();
                break;
        }
    }

    private void gotToSignUp() {
        String userName = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if(userName.isEmpty()){
            etUsername.setError("El nombre de usuario es requerido");
        }else if(email.isEmpty()){
            etEmail.setError("El email es requerido");
        }else if(password.isEmpty() || password.length() < 4){
            etPassword.setError("La contraseña es requerida y debe tener al menos 4 caracteres");
        }else{
            String code = "UDEMYANDROID";
            RequestSignUp requestSignUp = new RequestSignUp(userName, email, password, code);
            Call<ResponseAuth> call = miniTwitterService.doSignUp(requestSignUp);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        SharedPreferencesManager.setSomeStringValue(PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(PREF_ACTIVE, response.body().getActive());

                        Intent i = new Intent(SignUpActivity.this, FragmentsActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(SignUpActivity.this, "Algo salió mal, revise sus datos de acceso.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Problemas de conexión. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void goToLogin() {
        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
