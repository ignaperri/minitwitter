package com.example.minitwitter.retrofit;

import com.example.minitwitter.common.Constantes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiniTwitterClient {
    //por patron singleton (para no andar instanciando cada vez que se usa este objeto dentro de la app)
    private static MiniTwitterClient instance = null;
    //controla las llamadas a la API
    private MiniTwitterService miniTwitterService;
    private Retrofit retrofit;


    public MiniTwitterClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        miniTwitterService = retrofit.create(MiniTwitterService.class);
    }

    //Patron Singleton
    public static MiniTwitterClient getInstance(){
        if(instance == null){
            instance = new MiniTwitterClient();
        }
        return instance;
    }

    //Nos va a permitir consumir los servicios que tenemos definidos en nuestra API
    public MiniTwitterService getMiniTwitterService(){
        return miniTwitterService;
    }

}
