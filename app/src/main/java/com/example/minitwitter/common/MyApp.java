package com.example.minitwitter.common;

import android.app.Application;
import android.content.Context;

//para facilitar el acceso al contexto de la aplicacion
public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance(){
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    //Se abre una vez cuando se abre la aplicacion
    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
