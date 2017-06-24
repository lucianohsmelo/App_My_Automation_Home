package com.example.luciano.myhome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Luciano on 01/04/2017.
 */

public class Tools extends Fragment {

    public static TextView textTile, textServer, textUser, textPass, textPort;

    Button btnConfigLocal, btnConfigCloud, btnChange;

    boolean configLocal, configCloud;

    MainActivity mainActivity;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.title_Config));

        mainActivity = (MainActivity) getActivity();

        textTile = (TextView)getActivity().findViewById(R.id.textViewConfigTitle);
        textServer = (TextView)getActivity().findViewById(R.id.textViewServer);
        textUser = (TextView)getActivity().findViewById(R.id.textViewUser);
        textPass = (TextView)getActivity().findViewById(R.id.textViewPass);
        textPort = (TextView)getActivity().findViewById(R.id.textViewPort);

        Toast.makeText(getActivity(), "Selecione um Modo",Toast.LENGTH_SHORT).show();
        btnConfigLocal = (Button)getActivity().findViewById(R.id.btnConfigLocal);
        btnConfigLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textTile.setText("Configurações Modo Local");
                textServer.setText("Server: " + MainActivity.serverLocal);
                textUser.setText("User: " + MainActivity.usernameLocal);
                textPass.setText("Password: " + MainActivity.passwordLocal);
                textPort.setText("Port: " + MainActivity.portLocal);
                configLocal = true;
                configCloud = false;
            }
        });

        btnConfigCloud = (Button)getActivity().findViewById(R.id.btnConfigCloud);
        btnConfigCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textTile.setText("Configurações Modo Cloud");
                textServer.setText("Server: " + MainActivity.serverCloud);
                textUser.setText("User: " + MainActivity.usernameCloud);
                textPass.setText("Password: " + MainActivity.passwordCloud);
                textPort.setText("Port: " + MainActivity.portCloud);
                configCloud = true;
                configLocal = false;
            }
        });

        btnChange = (Button)getActivity().findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfig();
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tools, container, false);
    }

    public void showConfig(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getActivity().getLayoutInflater().inflate(R.layout.config_dialog, null);
        final EditText mServer = (EditText)mView.findViewById(R.id.editTextServer);
        final EditText mUser = (EditText)mView.findViewById(R.id.editTextUser);
        final EditText mPass = (EditText)mView.findViewById(R.id.editTextPass);
        final EditText mPort = (EditText)mView.findViewById(R.id.editTextPort);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        Button mDefault = (Button)mView.findViewById(R.id.btnDefault);
        mDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(configLocal) {
                    mServer.setText(MainActivity.serverLocal);
                    mUser.setText(MainActivity.usernameLocal);
                    mPass.setText(MainActivity.passwordLocal);
                    mPort.setText(MainActivity.portLocal);
                }else if (configCloud){
                    mServer.setText(MainActivity.serverCloud);
                    mUser.setText(MainActivity.usernameCloud);
                    mPass.setText(MainActivity.passwordCloud);
                    mPort.setText(MainActivity.portCloud);
                }
            }
        });
        Button mConfig = (Button)mView.findViewById(R.id.btnConfig);
        mConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mServer.getText().toString().isEmpty() && !mUser.getText().toString().isEmpty()
                        && !mPass.getText().toString().isEmpty() && !mPort.getText().toString().isEmpty()){
                    if(configLocal) {

                        MainActivity.serverLocal = String.valueOf((mServer.getText()));
                        MainActivity.usernameLocal = String.valueOf(mUser.getText());
                        MainActivity.passwordLocal = String.valueOf(mPass.getText());
                        MainActivity.portLocal = String.valueOf(mPort.getText());

                        textServer.setText("Server: " + mServer.getText());
                        textUser.setText("User: " + mUser.getText());
                        textPass.setText("Password: " + mPass.getText());
                        textPort.setText("Port: " + mPort.getText());

                        configLocal = false;

                        mainActivity.setTextLogger("Configurações Modo Local alteradas");

                        Toast.makeText(getActivity(), "Configurado", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else if (configCloud){
                        MainActivity.serverCloud = String.valueOf(mServer.getText());
                        MainActivity.usernameCloud = String.valueOf(mUser.getText());
                        MainActivity.passwordCloud = String.valueOf(mPass.getText());
                        MainActivity.portCloud = String.valueOf(mPort.getText());

                        textServer.setText("Server: "+mServer.getText());
                        textUser.setText("User: "+mUser.getText());
                        textPass.setText("Password: "+mPass.getText());
                        textPort.setText("Port: "+mPort.getText());

                        mainActivity.setTextLogger("Configurações Modo Cloud alteradas");

                        configCloud = false;
                        Toast.makeText(getActivity(), "Configurado", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        Toast.makeText(getActivity(), "Já configurado", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }else {
                    Toast.makeText(getActivity(), "Erro com os valores", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
