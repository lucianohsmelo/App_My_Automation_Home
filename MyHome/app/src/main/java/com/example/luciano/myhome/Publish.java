package com.example.luciano.myhome;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Luciano on 01/04/2017.
 */

public class Publish extends Fragment implements DialogInterface.OnClickListener {

    Button btnKeepAlive, btnPub, btnSelecTopic;
    EditText editTextPub;

    MainActivity mainActivity;

    AlertDialog alertDialog;
    String selectedTopic;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.title_Publish));

        mainActivity = (MainActivity) getActivity();

        editTextPub = (EditText)getActivity().findViewById(R.id.editTextPub);

        btnKeepAlive = (Button)getActivity().findViewById(R.id.btnKeepAlive);
        btnKeepAlive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                if (mainActivity.checkConnection()){
                    mainActivity.setTextLogger("Enviando Keep Alive");
                    mainActivity.pub("keepAlive",dateFormat.format(date) + " - " + "From Android");
                }else {
                    Toast.makeText(getActivity(), "Erro: Não conectado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPub = (Button)getActivity().findViewById(R.id.btnPub);
        btnPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.checkConnection()){
                    if(selectedTopic != null){
                        mainActivity.pub(selectedTopic, String.valueOf(editTextPub.getText()));
                        mainActivity.setTextLogger("Publish: " +selectedTopic+"/"+editTextPub.getText());
                    }else {
                        Toast.makeText(getActivity(), "Selecione um Tópico",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getActivity(),getString(R.string.error_NotConnected),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSelecTopic = (Button)getActivity().findViewById(R.id.btnSelecTopicPub);
        btnSelecTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Topic");
        builder.setItems(MainActivity.topics, this);
        builder.setNegativeButton("Cancel", null);
        alertDialog=builder.create();

    }


    @Override
    public void onClick(DialogInterface dialog, int pos) {
        selectedTopic = MainActivity.topics[pos];
        btnSelecTopic.setText(selectedTopic);
        Toast.makeText(getActivity(), selectedTopic,Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.publish, container, false);
    }

}
