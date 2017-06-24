package com.example.luciano.myhome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


/**
 * Created by Luciano on 01/04/2017.
 */

public class Menu1 extends Fragment {

    Button btnCon, btnDis;
    public static RadioButton radioButtonCloud, radioButtonLocal;
    public static ImageView imgStatus;

    public static TextView textTempInter, textTempExter;




    MainActivity mainActivity;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.title_Menu1));

        mainActivity = (MainActivity) getActivity();

        imgStatus = (ImageView)getActivity().findViewById(R.id.imageView2);
        textTempInter = (TextView)getActivity().findViewById(R.id.textViewTempIner);
        textTempInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mainActivity.checkConnection()) {
                        mainActivity.pub(getString(R.string.topic_temp), getString(R.string.get_temp));
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.error_NotConnected), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ignored){}
            }
        });
        textTempExter = (TextView)getActivity().findViewById(R.id.textViewTempExter);
        textTempExter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.checkConnection()){
                    mainActivity.pub(getString(R.string.topic_temp), getString(R.string.get_temp));
                } else{
                    Toast.makeText(getActivity(), getString(R.string.error_NotConnected),Toast.LENGTH_SHORT).show();
                }
            }
        });

        textTempInter.setText(MainActivity.lastTempInter);
        textTempExter.setText(MainActivity.lastTempExter);

        radioButtonCloud = (RadioButton) getActivity().findViewById(R.id.radioButton);
        radioButtonCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setTextLogger(getString(R.string.txt_SelectedCloud));
                if(radioButtonCloud.isChecked()){
                    MainActivity.clientType = getString(R.string.cloud_name);
                    radioButtonLocal.setChecked(false);
                }else {
                    radioButtonLocal.setChecked(true);
                    MainActivity.clientType = getString(R.string.local_name);
                }
            }
        });

        radioButtonLocal = (RadioButton)getActivity().findViewById(R.id.radioButton2);
        radioButtonLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setTextLogger(getString(R.string.txt_SelectedLocal));
                if(radioButtonCloud.isChecked()){
                    MainActivity.clientType = getString(R.string.local_name);
                    radioButtonCloud.setChecked(false);
                }else {
                    radioButtonCloud.setChecked(true);
                    MainActivity.clientType = getString(R.string.cloud_name);
                }

            }
        });

        if (!MainActivity.isConnectedLocal && !MainActivity.isConnectedCloud) {
            if (radioButtonLocal.isChecked()) {
                MainActivity.clientType = getString(R.string.local_name);
            } else if (radioButtonCloud.isChecked()) {
                MainActivity.clientType = getString(R.string.cloud_name);
            } else {
                mainActivity.setTextLogger(getString(R.string.error_ClientType));
            }
        }


        btnCon = (Button)getActivity().findViewById(R.id.button2);
        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(MainActivity.clientType, getString(R.string.local_name))){
                    if (!MainActivity.isConnectedLocal) {
                        mainActivity.setTextLogger(getString(R.string.txt_StarConnectionLocal) + MainActivity.serverLocal + " /"
                                + MainActivity.usernameLocal + "/" + MainActivity.passwordLocal + "/"
                                + MainActivity.portLocal + getString(R.string.txt_wait));
                        mainActivity.connect(MainActivity.clientType);
                    }else{
                        Toast.makeText(getActivity(),getString(R.string.error_AlreadyConnected),Toast.LENGTH_SHORT).show();
                    }
                }else if (Objects.equals(MainActivity.clientType, getString(R.string.cloud_name))){
                    if (!MainActivity.isConnectedCloud) {
                        mainActivity.setTextLogger(getString(R.string.txt_StarConnectionCloud) + MainActivity.serverCloud + " /"
                                + MainActivity.usernameCloud + "/" + MainActivity.passwordCloud + "/"
                                + MainActivity.portCloud + getString(R.string.txt_wait));
                        mainActivity.connect(MainActivity.clientType);
                    }else {
                        Toast.makeText(getActivity(),getString(R.string.error_AlreadyConnected),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(),getString(R.string.error_ClientType),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDis = (Button)getActivity().findViewById(R.id.button);
        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(MainActivity.clientType, getString(R.string.local_name))){
                    if (MainActivity.isConnectedLocal) {
                        mainActivity.setTextLogger(getString(R.string.txt_ToEndConnectionLocal));
                        mainActivity.disconnect(MainActivity.clientType);
                    }else {
                        Toast.makeText(getActivity(),getString(R.string.error_NotConnected),Toast.LENGTH_SHORT).show();
                    }
                }else if (Objects.equals(MainActivity.clientType, getString(R.string.cloud_name))){
                    if (MainActivity.isConnectedCloud) {
                        mainActivity.setTextLogger(getString(R.string.txt_ToEndConnectionCloud));
                        mainActivity.disconnect(MainActivity.clientType);
                    }else {
                        Toast.makeText(getActivity(),getString(R.string.error_NotConnected),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(),getString(R.string.error_ClientType),Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (mainActivity.checkConnection()){
            imgStatus.setImageResource(R.drawable.on);
        }else{
            imgStatus.setImageResource(R.drawable.off);
        }

        if(MainActivity.isConnectedCloud) {
            radioButtonCloud.setChecked(true);
            radioButtonLocal.setChecked(false);
        }else{
            radioButtonLocal.setChecked(true);
            radioButtonCloud.setChecked(false);
        }

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu1, container, false);
    }


}
