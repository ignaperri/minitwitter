package com.example.minitwitter.ui.tweets;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.TweetViewModel;

public class NuevoTweetDialogFragment extends DialogFragment implements View.OnClickListener {
    ImageView ivClose, ivAvatar;
    Button btTwittear;
    EditText etMensaje;

    public NuevoTweetDialogFragment() {
    }

    public static NuevoTweetDialogFragment newInstance(String param1, String param2) {
        NuevoTweetDialogFragment fragment = new NuevoTweetDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nuevo_tweet_full_dialog, container, false);
        ivClose = view.findViewById(R.id.imageViewClose);
        ivAvatar = view.findViewById(R.id.imageViewAvatar);
        btTwittear = view.findViewById(R.id.buttonTwittear);
        etMensaje = view.findViewById(R.id.editTextMensaje);

        btTwittear.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        //seteamos la imagen de perfil
        String photoURL = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTOURL);
        if(!photoURL.isEmpty()){
            Glide.with(getActivity())
                    .load(Constantes.API_MINITWITTER_FILES_URL + photoURL)
                    .into(ivAvatar);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String mensaje = etMensaje.getText().toString();
        switch (id){
            case R.id.buttonTwittear:
                if(mensaje.isEmpty()){
                    Toast.makeText(getActivity(), "Debe escribir un mensaje", Toast.LENGTH_SHORT).show();
                }else{
                    TweetViewModel tweetViewModel = ViewModelProviders.of(getActivity()).get( TweetViewModel.class);
                    tweetViewModel.insertTweet(mensaje);
                    getDialog().dismiss();
                }
                break;
            case R.id.imageViewClose:
                if(!mensaje.isEmpty())
                    showDialogConfirm();
                else
                    getDialog().dismiss();
                break;
        }
    }

    private void showDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Desea cancelar el tweet?, el mesnaje se eliminará")
                .setTitle("Cancelar Tweet");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                getDialog().dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
