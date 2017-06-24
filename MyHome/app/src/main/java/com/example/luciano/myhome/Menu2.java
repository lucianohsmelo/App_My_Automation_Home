package com.example.luciano.myhome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Luciano on 01/04/2017.
 */

public class Menu2 extends Fragment {

    ImageButton btnOpen;
    public static TextView textDoor;

    MainActivity mainActivity;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.title_Menu2));
        mainActivity = (MainActivity) getActivity();

        textDoor = (TextView)getActivity().findViewById(R.id.textViewDoor);

        btnOpen = (ImageButton)getActivity().findViewById(R.id.btnOpenDoor);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.checkConnection()){
                    showConfirm();
                }else{
                    Toast.makeText(getActivity(),getString(R.string.error_NotConnected),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void showConfirm() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        final View mView = getActivity().getLayoutInflater().inflate(R.layout.confirm_dialog, null);
        Button mYes = (Button) mView.findViewById(R.id.openYes);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setTextLogger(getString(R.string.txt_openDoor));
                mainActivity.pub(getString(R.string.topic_door),getString(R.string.sendTopic_openDoor));
                textDoor.setText(getString(R.string.txt_waitingAnswer));
                dialog.cancel();
            }
        });

        Button mNo = (Button) mView.findViewById(R.id.openNo);
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu2, container, false);
    }
}
