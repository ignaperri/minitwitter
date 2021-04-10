package com.example.minitwitter.retrofit;

import com.example.minitwitter.common.SharedPreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.minitwitter.common.Constantes.PREF_TOKEN;

/**
 * Va a obtener previamente al envio de la peticion al servidor toda la info de esa peticion
 *
 * Este interceptor concatenará a la informacion que enviemos al servidor, la cabecera
 * de autorizacion con el token que hemos recibido y que nos autentica como usuario en el sistema
 */
public class AuthInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPreferencesManager.getSomeStringValue(PREF_TOKEN);
        //estamos tomando la informacion de la lista de twitter y le vamos a añadir una cabecera
        //el objeto chain permite enlazar la peticion que estamos recibiendo con la que vamos a enviar
        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();
        //enviamos la peticion que creamos
        return chain.proceed(request);
    }
}
