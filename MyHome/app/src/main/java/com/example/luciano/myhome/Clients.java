package com.example.luciano.myhome;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Luciano on 01/04/2017.
 */

public class Clients extends Fragment {

    // public static ListView listViewClients;
    //List<ListViewItem> items;
    //CustomListViewAdapter adapter;

    public static TextView txtKeepAliveNodeRed, txtKeepAliveNodeMcu, txtKeepAliveArduino;
    TextView txtTitleArduino, txtTitleNodeRed, txtTitleNodeMcu;
    public static ImageView imgNodeRed, imgNodeMcu, imgArduino;
    Button btnCheckAll;

    MainActivity mainActivity;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Check Clients");

        mainActivity = (MainActivity) getActivity();

        txtKeepAliveArduino = (TextView)getActivity().findViewById(R.id.txtKeepAliveArduino);
        txtKeepAliveNodeMcu = (TextView)getActivity().findViewById(R.id.txtKeepAliveNodeMcu);
        txtKeepAliveNodeRed = (TextView)getActivity().findViewById(R.id.txtKeepAliveNodeRed);

        txtTitleArduino = (TextView)getActivity().findViewById(R.id.textView7);
        txtTitleArduino.setText("Arduino");
        txtTitleNodeMcu = (TextView)getActivity().findViewById(R.id.textView2);
        txtTitleNodeMcu.setText("NodeMcu");
        txtTitleNodeRed = (TextView)getActivity().findViewById(R.id.textView5);
        txtTitleNodeRed.setText("Node-Red");


        imgArduino = (ImageView)getActivity().findViewById(R.id.imageView5);
        imgArduino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.checkConnection()){
                    mainActivity.pub(getString(R.string.topic_keepAlive), getString(R.string.get_keepAliveArduino));
                    txtKeepAliveArduino.setText(getString(R.string.txt_waitingAnswer));
                }else {
                    Toast.makeText(getActivity(), getString(R.string.error_NotConnected), Toast.LENGTH_SHORT).show();
                }
            }
        });
        imgNodeMcu = (ImageView)getActivity().findViewById(R.id.imageView3);
        imgNodeMcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.checkConnection()){
                    mainActivity.pub(getString(R.string.topic_keepAlive), getString(R.string.get_keepAliveNodeMcu));
                    txtKeepAliveNodeMcu.setText(getString(R.string.txt_waitingAnswer));
                }else {
                    Toast.makeText(getActivity(), getString(R.string.error_NotConnected), Toast.LENGTH_SHORT).show();
                }
            }
        });
        imgNodeRed = (ImageView)getActivity().findViewById(R.id.imageView4);
        imgNodeRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.checkConnection()){
                    mainActivity.pub(getString(R.string.topic_keepAlive), getString(R.string.get_keepAliveNodeRed));
                    txtKeepAliveNodeRed.setText(getString(R.string.txt_waitingAnswer));
                }else {
                     Toast.makeText(getActivity(), getString(R.string.error_NotConnected), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnCheckAll = (Button)getActivity().findViewById(R.id.checkClients);
        btnCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.checkConnection()){
                    mainActivity.pub(getString(R.string.topic_keepAlive),getString(R.string.get_keepAliveAll));
                    txtKeepAliveNodeRed.setText(getString(R.string.txt_waitingAnswer));
                    txtKeepAliveNodeMcu.setText(getString(R.string.txt_waitingAnswer));
                    txtKeepAliveArduino.setText(getString(R.string.txt_waitingAnswer));
                }else {
                Toast.makeText(getActivity(), getString(R.string.error_NotConnected), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clients, container, false);
    }
}
