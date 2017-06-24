package com.example.luciano.myhome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String serverLocal = "192.168.1.10";
    public static String usernameLocal = "localBroker";
    public static String passwordLocal = "1234";
    public static String portLocal = "1883";

    public static String serverCloud = "m11.cloudmqtt.com";
    public static String usernameCloud = "jgojknep";
    public static String passwordCloud = "muJlxlc58d0v";
    public static String portCloud = "14458";

    public static String clientType, lastKeepAlive;
    public static String outTextLogger;
    public static boolean isConnectedLocal, isConnectedCloud;

    public static ArrayList<Integer> graphTempInterYAxis;
    public static ArrayList<String> graphTempInterXAxis;
    public static ArrayList<Integer> graphTempExterYAxis;
    public static ArrayList<String> graphTempExterXAxis;

    public static Integer[] imgLamps;

    MqttAndroidClient clientLocal, clientCloud;
    MqttConnectOptions optionsLocal, optionsCloud;

    public static String lastTempInter = "°C", lastTempExter = "°C";

    public static String topics[] = new String[]{
            "keepAlive",
            "outTopic",
            "inTopic",
            "door",
            "temp",
            "lamp"
    };

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH:mm:ss");
    Date date;

    Button btnClearLogger;
    public static TextView textLogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lastKeepAlive = null;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textLogger = (TextView)findViewById(R.id.textViewLogger);
        textLogger.setMovementMethod(new ScrollingMovementMethod());

        btnClearLogger = (Button)findViewById(R.id.btnClear);
        btnClearLogger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textLogger.setText("");
            }
        });

        displaySelectedScreen(R.id.nav_menu1);

        graphTempInterYAxis = new ArrayList<>();
        graphTempInterXAxis = new ArrayList<>();

        graphTempExterYAxis = new ArrayList<>();
        graphTempExterXAxis = new ArrayList<>();

    }

    public void connect(String clientType){
        if (Objects.equals(clientType, getString(R.string.local_name))) {

            if(!isConnectedLocal) {
                if (isConnectedCloud) {
                    disconnect(getString(R.string.cloud_name));
                }
                try {
                    String clientIdLocal = MqttClient.generateClientId();
                    clientLocal =
                            new MqttAndroidClient(this.getApplicationContext(), "tcp://"+serverLocal+":"+portLocal,
                                    clientIdLocal);

                    optionsLocal = new MqttConnectOptions();
                    optionsLocal.setUserName(usernameLocal);
                    optionsLocal.setPassword(passwordLocal.toCharArray());
                    final IMqttToken tokenLocal = clientLocal.connect();
                    tokenLocal.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // We are connected
                            isConnectedLocal = true;
                            setSubscription();
                            callbackLocal();
                            setTextLogger(getString(R.string.txt_ConnectedLocal));
                            Menu1.imgStatus.setImageResource(R.drawable.on);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            // Something went wrong e.g. connection timeout or firewall problems
                            isConnectedLocal = false;
                            Toast.makeText(MainActivity.this, tokenLocal.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            setTextLogger(getString(R.string.txt_failConnectionLocal));
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                    isConnectedLocal = false;
                }

            }else{
                Toast.makeText(MainActivity.this,getString(R.string.error_AlreadyConnected), Toast.LENGTH_SHORT).show();
            }
        }else if (Objects.equals(clientType, getString(R.string.cloud_name))) {
            if (!isConnectedCloud) {
                if (isConnectedLocal) {
                    disconnect(getString(R.string.local_name));
                }
                try {
                    String clientIdCloud = MqttClient.generateClientId();
                    clientCloud = new MqttAndroidClient(this.getApplicationContext(), ("tcp://"+serverCloud+":"+portCloud),
                            clientIdCloud);

                    optionsCloud = new MqttConnectOptions();
                    optionsCloud.setUserName(usernameCloud);
                    optionsCloud.setPassword(passwordCloud.toCharArray());

                    IMqttToken token = clientCloud.connect(optionsCloud);
                    token.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // We are connected
                            isConnectedCloud = true;
                            setSubscription();
                            callbackCloud();
                            setTextLogger(getString(R.string.txt_ConnectedCloud));
                            Menu1.imgStatus.setImageResource(R.drawable.on);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            // Something went wrong e.g. connection timeout or firewall problems
                            isConnectedCloud = false;
                            setTextLogger(getString(R.string.txt_failConnectionCloud));
                            Menu1.imgStatus.setImageResource(R.drawable.off);
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                    isConnectedCloud = false;
                }

            } else {
                Toast.makeText(MainActivity.this,getString(R.string.error_ClientType), Toast.LENGTH_SHORT).show();
            }
        }else   {
            Toast.makeText(MainActivity.this,getString(R.string.error_AlreadyConnected), Toast.LENGTH_SHORT).show();
        }

    }

    public void disconnect(String clientType){
        if (Objects.equals(clientType, getString(R.string.local_name))) {
            if (isConnectedLocal) {
                try {
                    IMqttToken tokenLocal = clientLocal.disconnect();
                    tokenLocal.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // We are connected
                            isConnectedLocal = false;
                            setTextLogger(getString(R.string.txt_EndConnectionLocal));
                            Menu1.imgStatus.setImageResource(R.drawable.off);

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            // Something went wrong e.g. connection timeout or firewall problems
                            setTextLogger(getString(R.string.txt_failConnectionLocal));
                            Menu1.imgStatus.setImageResource(R.drawable.off);
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(MainActivity.this, getString(R.string.error_NotConnectedLocal), Toast.LENGTH_SHORT).show();
            }
        }else if (Objects.equals(clientType, getString(R.string.cloud_name))){
            if (isConnectedCloud) {
                try {
                    IMqttToken tokenCloud = clientCloud.disconnect();
                    tokenCloud.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // We are connected
                            isConnectedCloud = false;
                            setTextLogger(getString(R.string.txt_EndConnectionCloud));
                            Menu1.imgStatus.setImageResource(R.drawable.off);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            // Something went wrong e.g. connection timeout or firewall problems
                            setTextLogger(getString(R.string.txt_failConnectionCloud));
                            Menu1.imgStatus.setImageResource(R.drawable.off);
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(MainActivity.this, getString(R.string.error_NotConnectedLocal), Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(MainActivity.this, getString(R.string.error_ClientType), Toast.LENGTH_SHORT).show();
        }

    }

    private void setSubscription(){
        try{
            if (isConnectedLocal) {
                for (int i = 0; i <= (topics.length - 1); i++) {
                    clientLocal.subscribe(topics[i], 0);
                }
            }else if(isConnectedCloud){
                for (int i = 0; i <= (topics.length - 1); i++) {
                    clientCloud.subscribe(topics[i], 0);
                }
            }else {
                Toast.makeText(MainActivity.this, getString(R.string.error_ClientType), Toast.LENGTH_SHORT).show();
            }
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void pub(String strTopic, String strMsg){
        if (Objects.equals(clientType, getString(R.string.local_name))) {
            String topic = strTopic;
            String msg = strMsg;
            try {
                clientLocal.publish(topic, msg.getBytes(), 0, false);
                Toast.makeText(MainActivity.this, "Publish - " +topic+"/"+msg, Toast.LENGTH_SHORT).show();
            } catch (MqttException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"Publish Error", Toast.LENGTH_SHORT).show();
            }
        }else if (Objects.equals(clientType, getString(R.string.cloud_name))){
            String topic = strTopic;
            String msg = strMsg;
            try {
                clientCloud.publish(topic, msg.getBytes(), 0, false);
                Toast.makeText(MainActivity.this, "Publish - " +topic+"/"+msg, Toast.LENGTH_SHORT).show();
            } catch (MqttException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"Publish Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void callbackLocal(){
        clientLocal.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Toast.makeText(MainActivity.this, "Subscribe - "+topic+"/"+ message, Toast.LENGTH_SHORT).show();
                setTextValues(topic, message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
    }

    public void callbackCloud(){
        clientCloud.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Toast.makeText(MainActivity.this, "Subscribe - "+topic+"/"+message, Toast.LENGTH_SHORT).show();
                setTextValues(topic, message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void setTextValues(String topic, MqttMessage message){
        switch (topic){
            case "temp":
                String temp[] = message.toString().split("/");
                if (Objects.equals(temp[0], getString(R.string.respTopic_tempInter))) {
                    lastTempInter = temp[1]+"°C";
                    Menu1.textTempInter.setText(lastTempInter);
                    date = new Date();
                    graphTempInterXAxis.add(dateFormatHour.format(date));
                    graphTempInterYAxis.add(Integer.parseInt(temp[1]));
                    setTextLogger("Temp. Interna: "+String.valueOf(temp[1])+"°C");
                    GraphTemp.txtRefresh.setText(getString(R.string.txt_newDataTempInter));
                }else if (Objects.equals(temp[0], getString(R.string.respTopic_tempExter))){
                    lastTempExter = temp[1]+"°C";
                    Menu1.textTempExter.setText(lastTempExter);
                    date = new Date();
                    graphTempExterXAxis.add(dateFormatHour.format(date));
                    graphTempExterYAxis.add(Integer.parseInt(temp[1]));
                    setTextLogger("Temp. Externa: "+String.valueOf(temp[1])+"°C");
                    GraphTemp.txtRefresh.setText(getString(R.string.txt_newDataTempExter));
                }
                break;
            case "door":
                if (Objects.equals(message.toString(), getString(R.string.respTopic_openSucess))){
                    setTextLogger(getString(R.string.txt_openGateSucess));
                    Menu2.textDoor.setText(getString(R.string.txt_openGateSucess));
                }
            case "keepAlive":
                date = new Date();
                if (Objects.equals(message.toString(), getString(R.string.respTopic_NodeMcuAlive))) {
                    Clients.txtKeepAliveNodeMcu.setText(dateFormat.format(date));
                    setTextLogger(getString(R.string.txt_nodeMcuConnected));
                }else if(Objects.equals(message.toString(), getString(R.string.respTopic_NodeRedAlive))){
                    Clients.txtKeepAliveNodeRed.setText(dateFormat.format(date));
                    setTextLogger(getString(R.string.txt_nodeRedConnected));
                }else if(Objects.equals(message.toString(), getString(R.string.respTopic_ArduinoAlive))){
                    Clients.txtKeepAliveArduino.setText(dateFormat.format(date));
                    setTextLogger(getString(R.string.txt_ArduinoConnected));
                }
                break;
            case "lamp":
                date = new Date();
                String lamp[] = message.toString().split("/");
                if (Objects.equals(lamp[0],"01")){
                    if (Objects.equals(lamp[1], "ON")){
                        setTextLogger("Lâmpada 01 acessa");
                    }else if (Objects.equals(lamp[1], "OFF")){
                        setTextLogger("Lâmpada 01 apagada");
                    }
                }else if (Objects.equals(lamp[0],"02")){
                    if (Objects.equals(lamp[1], "ON")){
                        setTextLogger("Lâmpada 02 acessa");
                    }else if (Objects.equals(lamp[1], "OFF")){
                        setTextLogger("Lâmpada 02 apagada");
                    }
                }
        }
    }

    public void setTextLogger(String str){
        date = new Date();
        outTextLogger = dateFormat.format(date) + " - " + str + "\n" + textLogger.getText();
        textLogger.setText(outTextLogger);
    }

    public boolean checkConnection(){
        if(isConnectedLocal || isConnectedCloud){
            return true;
        }else{
            return false;
        }
    }

    private void displaySelectedScreen(int id){
        Fragment fragment = null;
        switch (id){
            case R.id.nav_menu1:
                fragment = new Menu1();
                textLogger.setVisibility(View.VISIBLE);
                btnClearLogger.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_menu2:
                fragment = new Menu2();
                textLogger.setVisibility(View.VISIBLE);
                btnClearLogger.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_menu3:
                fragment = new Menu3();
                textLogger.setVisibility(View.VISIBLE);
                btnClearLogger.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_tools:
                fragment = new Tools();
                textLogger.setVisibility(View.VISIBLE);
                btnClearLogger.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_pub:
                fragment = new Publish();
                textLogger.setVisibility(View.VISIBLE);
                btnClearLogger.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_cli:
                fragment = new Clients();
                textLogger.setVisibility(View.INVISIBLE);
                btnClearLogger.setVisibility(View.INVISIBLE);
                break;
            case R.id.nav_graphTemp:
                fragment = new GraphTemp();
                textLogger.setVisibility(View.INVISIBLE);
                btnClearLogger.setVisibility(View.INVISIBLE);
        }

        if (fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }
}
