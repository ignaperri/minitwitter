package com.example.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.MyApp;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.AuthTwitterService;
import com.example.minitwitter.retrofit.request.RequestCreateTweet;
import com.example.minitwitter.retrofit.response.AuthTwitterClient;
import com.example.minitwitter.retrofit.response.Like;
import com.example.minitwitter.retrofit.response.Tweet;
import com.example.minitwitter.retrofit.response.TweetDeleted;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {
    private AuthTwitterService authTwitterService;
    private AuthTwitterClient authTwitterClient;
    private MutableLiveData<List<Tweet>> allTweets;
    private MutableLiveData<List<Tweet>> favTweets;
    String userName;

    public TweetRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
        userName = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME);
    }

    public MutableLiveData<List<Tweet>> getAllTweets(){
        if(allTweets == null){
            allTweets = new MutableLiveData<>();
        }

        Call<List<Tweet>> call = authTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful()){
                    allTweets.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(), "Algo salió mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

        return allTweets;
    }

    public MutableLiveData<List<Tweet>> getFavsTweets(){
        if(favTweets == null){
            favTweets = new MutableLiveData<>();
        }

        List<Tweet> newFavTweet = new ArrayList<>();
        Iterator itTweets = allTweets.getValue().iterator();

        while (itTweets.hasNext()){
            Tweet current = (Tweet) itTweets.next();
            Iterator itLikes = current.getLikes().iterator();
            boolean enc = false;
            while(itLikes.hasNext() && !enc){
                Like like = (Like) itLikes.next();
                if(like.getUsername().equals(userName)){
                    enc = true;
                    newFavTweet.add(current);
                }
            }
        }

        //para comunicarle a cualquier observador que este pendiente de esta lista
        favTweets.setValue(newFavTweet);

        return favTweets;
    }

    public void createTweet(String mensaje){
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet(mensaje);
        Call<Tweet> call = authTwitterService.createTweet(requestCreateTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> listaClonada = new ArrayList<>();
                    //añadimos en primer lugar el nuevo tweet que nos llega del server
                    listaClonada.add(response.body());
                    for (Tweet tweet : allTweets.getValue()) {
                        listaClonada.add(new Tweet(tweet));
                    }
                    allTweets.setValue(listaClonada);
                }else{
                    Toast.makeText(MyApp.getContext(), "Algo salió mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteTweet(final int idTweet){
        Call<TweetDeleted> call = authTwitterService.deleteTweet(idTweet);
        call.enqueue(new Callback<TweetDeleted>() {
            @Override
            public void onResponse(Call<TweetDeleted> call, Response<TweetDeleted> response) {
                if(response.isSuccessful()){
                    List<Tweet> clonedTweets = new ArrayList<>(); //clonamos una lista de tweets para llenar todos los tweets menos el que eliminamos
                    for (Tweet tweet : allTweets.getValue()) {
                        if(tweet.getId() != idTweet){
                            clonedTweets.add(tweet);
                        }
                    }
                    //le comunicamos a todos los observadores que estan pendientes a esta accion
                    allTweets.setValue(clonedTweets);
                    //actualizamos tambien la lista de favoritos por si el que eliminamos justo era uno de ellos
                    getFavsTweets();
                }else{
                    Toast.makeText(MyApp.getContext(), "Algo salió mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TweetDeleted> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void likeTweet(int idTweet){
        Call<Tweet> call = authTwitterService.likeTweet(idTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> listaClonada = new ArrayList<>();

                    for (Tweet tweet : allTweets.getValue()) {
                        if(tweet.getId() == idTweet){
                            /*  si hemos encontrado en la lista original
                                el elemento sobre el que hemos hecho like,
                                introducimos el elemento que nos ha llegado del servidor
                             */
                            listaClonada.add(response.body());
                        }else{
                            listaClonada.add(new Tweet(tweet));
                        }
                    }
                    allTweets.setValue(listaClonada);
                    getFavsTweets();
                }else{
                    Toast.makeText(MyApp.getContext(), "Algo salió mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
