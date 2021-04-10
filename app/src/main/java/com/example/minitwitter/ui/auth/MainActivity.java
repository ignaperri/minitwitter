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
import com.example.minitwitter.retrofit.request.RequestLogin;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnLogin;
    TextView tvGoSignUp;
    EditText etEmail, etPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        btnLogin = findViewById(R.id.buttonLogin);
        tvGoSignUp = findViewById(R.id.textViewGoSignUp);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
    }

    private void events() {
        btnLogin.setOnClickListener(this);
        tvGoSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.buttonLogin:
                goToLogin();
                break;
            case R.id.textViewGoSignUp:
                goToSignUp();
                break;
        }
    }

    private void goToLogin() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if(email.isEmpty()){
            etEmail.setError("El email es requerido");
        }else if(password.isEmpty()){
            etPassword.setError("La contraseña es requerida");
        }else{
            RequestLogin requestLogin = new RequestLogin(email, password);
            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);
            //peticion asyncronica
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Sesión iniciada correctamente.", Toast.LENGTH_SHORT).show();
                        SharedPreferencesManager.setSomeStringValue(PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(PREF_ACTIVE, response.body().getActive());

                        Intent i = new Intent(MainActivity.this, FragmentsActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, "Algo salió mal, revise sus datos de acceso.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Problemas de conexión. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void goToSignUp() {
        Intent i = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(i);
        finish();
    }
}
