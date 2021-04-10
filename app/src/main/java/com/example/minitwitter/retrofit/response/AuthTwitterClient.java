package com.example.minitwitter.retrofit.response;

import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.retrofit.AuthInterceptor;
import com.example.minitwitter.retrofit.AuthTwitterService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {
    //por patron singleton (para no andar instanciando cada vez que se usa este objeto dentro de la app)
    private static AuthTwitterClient instance = null;
    //controla las llamadas a la API
    private AuthTwitterService authTwitterService;
    private Retrofit retrofit;


    public AuthTwitterClient() {
        //Incluir en la cabezera de la peticion el TOKEN que autoriza al usuario
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(cliente)
                .build();

        authTwitterService = retrofit.create(AuthTwitterService.class);
    }

    //Patron Singletón
    public static AuthTwitterClient getInstance(){
        if(instance == null){
            instance = new AuthTwitterClient();
        }
        return instance;
    }

    //Nos va a permitir consumir los servicios que tenemos definidos en nuestra API
    public AuthTwitterService getAuthTwitterService(){
        return authTwitterService;
    }

}
