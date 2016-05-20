package aivco.com.bluetooth_exe;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import aivco.com.bluetooth_exe.R;
import aivco.com.bluetooth_exe.ClientSocketService.BluetoothSocketsList;

import java.util.ArrayList;
import java.util.List;

import aivco.com.bluetooth_exe.model.Switch;

/**
 * Created by joel on 1/4/16.
 */
public class CustomAdaptorForListView extends ArrayAdapter<Switch> implements View.OnClickListener,ServerResponce{

    List<Switch> switches;
    Activity activity;
    LayoutInflater inflater;
    private int positionSelected;
    TextView switchName;
    CustomizedImageButton switchstate;
    Drawable icon;
    View view;
    ServiceConnection conn;
    BluetoothSocket socket;
    private List<BluetoothSocket> bluetoothsockets=new ArrayList<>();

    public String tag="CustomAdaptorForListView";


    public CustomAdaptorForListView(Activity activity, int resource, List<Switch> objects) {
        super(activity, resource, objects);
        Log.d(tag, "Constructor");
        this.switches=objects;
        this.activity=activity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bindToService();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position,convertView,parent);
    }

    public View createView(int position, View convertView, ViewGroup parent){
        Log.d(tag, "Oncreate view");
        view=inflater.inflate(R.layout.custom_switch_interface,parent,false) ;
        Log.d(tag, "After inflating");
        Switch mySwitch=switches.get(position);
        socket=getBluetoothSocket(mySwitch.getBluetoothDevice());

        switchName=(TextView)view.findViewById(R.id.switch_name);
        switchName.setOnClickListener(this);
            final  TextView descriptionName=(TextView)view.findViewById(R.id.description);
        descriptionName.setOnClickListener(this);
        /////setDrawable for bluetoothdev as on or off by sending a message to the bluetooth socket to confirm if it is on or off ///////////////
        if(socket != null){
        switchstate=(CustomizedImageButton)view.findViewById(R.id.switch_state);
        switchstate.setOnClickListener(this);
        switchstate.setConnectionInstance(getBluetoothSocket(mySwitch.getBluetoothDevice()));
        switchstate.setContentDescription(mySwitch.getBluetoothDevice());}

        /////dialog that list infos about switch ///////////////
        ImageButton switchinfo=(ImageButton)view.findViewById(R.id.switch_info);
        switchinfo.setOnClickListener(this);
        switchinfo.setContentDescription(mySwitch.getSwitchName());


        switchName.setOnClickListener(this);
        switchName.setText(mySwitch.getSwitchName());
        descriptionName.setText(mySwitch.getDescription());


        /////////////////setting image drawable for each row.The bluetooth icon will only be added to a row if a connection already exist for the bluetooth device//////////
        ImageView connection_status=(ImageView) view.findViewById(R.id.connection_status);
        if(socket!=null)
        {
            connection_status.setImageDrawable(activity.getResources().getDrawable(android.R.drawable.stat_sys_data_bluetooth));
        }


        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.switch_state:{
                CustomizedImageButton switchstate=(CustomizedImageButton)v.findViewById(R.id.switch_state);
                System.out.println("Connection instance....is.........................................."+switchstate.getConnectionInstance() );
                Log.d(tag, "switch has been clicked");

                Drawable icon=switchstate.getDrawable();
                if(icon.getConstantState().equals(activity.getResources().getDrawable(android.R.drawable.button_onoff_indicator_off).getConstantState()))
                {                System.out.println("position is...................................................switch_state........on");


                    //constructBundle(MainActivity.SWITCHSTATESELECTIONKEY,switchstate.getContentDescription().toString());
                    checkStatus(socket);
                    switchstate.setImageDrawable(activity.getResources().getDrawable(android.R.drawable.button_onoff_indicator_on));
                    view.invalidate();
                }
                else{
                    System.out.println("position is...................................................switch_state........off");
                    checkStatus(socket);
                    //constructBundle(MainActivity.SWITCHSTATESELECTIONKEY, switchstate.getContentDescription().toString());
                    switchstate.setImageDrawable(activity.getResources().getDrawable(android.R.drawable.button_onoff_indicator_off));
                    view.invalidate();


                }



                break;
            }
            case R.id.switch_info:
            {
                ImageButton switchDescription=(ImageButton)v.findViewById(R.id.switch_info);
                System.out.println("info is" + switchDescription.getContentDescription());
                Message message= MainActivity.bundleHandler.obtainMessage();
                Bundle bundle=new Bundle();
                bundle.putString(MainActivity.INFOSELECTIONKEY, switchDescription.getContentDescription().toString());
                message.setData(bundle);
                MainActivity.bundleHandler.sendMessage(message);
                break;
            }
            case R.id.description:
            {
                TextView vIEW=(TextView)v.findViewById(R.id.description);

                System.out.println("positon description,"+ vIEW.getText().toString());

                Message message= MainActivity.bundleHandler.obtainMessage();
                Bundle bundle=new Bundle();
                bundle.putString(MainActivity.DESCRIPTIONKEY, vIEW.getText().toString());
                message.setData(bundle);
                MainActivity.bundleHandler.sendMessage(message);
                break;
            }
            case R.id.switch_name:{
                TextView vIEW=(TextView)v.findViewById(R.id.switch_name);


                System.out.println("swithname," + vIEW.getText().toString());

                break;

            }

        }
    }
    public Drawable getSwitchDrawable(){

        return icon;
    }

    public Bundle constructBundle(String key,String val){
        Message message= MainActivity.bundleHandler.obtainMessage();
        Bundle bundle=new Bundle();
        bundle.putString(key,val);
        message.setData(bundle);
        MainActivity.bundleHandler.sendMessage(message);

        return null;
    }

     public void listenForConnection(){

         conn=new ServiceConnection() {

             @Override

             public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                 Log.d(tag, "onServiceConnected");
                 //Service_Bind.DownloadPicture downloadPicture = (Service_Bind.DownloadPicture) iBinder;

                 //ans = downloadPicture.delay();
                 BluetoothSocketsList bSL=(BluetoothSocketsList)iBinder;
                 bluetoothsockets= bSL.getSocketList();
                 Log.d(tag, "done onservice connected");
                 activity.unbindService(conn);

             }

             @Override
             public void onServiceDisconnected(ComponentName componentName) {
                 Log.d(tag, "unibinding from service");
             }
         };

     }

    public void bindToService(){
        listenForConnection();
        Log.d(tag, "binding to service");
        Intent intent=new Intent() ;
        intent.setClass(activity.getBaseContext(),ClientSocketService.class);
       activity.bindService(intent,conn,0);
    }


    /*method called to compare every bluetooth string device from the db to every remotedevice returned by the bluetoothsocket returned from ibinder object
    * if it matches it returns the socket
    * **/
    public BluetoothSocket getBluetoothSocket(String bluetoothAsString){
        Log.d(tag, "getBluetoothSocket");
        BluetoothDevice bdevice=getDeviceFromGson(bluetoothAsString);
        for(BluetoothSocket socket:bluetoothsockets )
        {
            Log.d(tag, "bluetoothsockets list has sockets");
            if(socket.getRemoteDevice().equals(bdevice))
            {
                Log.d(tag, "found a match so returning a socket");
               return socket;
            }
        }
      return null;
    }



    /**return a Bluetooth device from Gson Object*/
    private BluetoothDevice getDeviceFromGson(String bluetoothDeviceAsString){
        return new Gson().fromJson(bluetoothDeviceAsString, BluetoothDevice.class);
    }

    /*sends a message to the remote socket to confirm if its switch is on or off*/
    public void checkStatus(BluetoothSocket btS)
    {
        Log.d(tag, "calling handle communication");

        HandleCommunications hc=new HandleCommunications(btS,HandleCommunications.CLIENT,HandleCommunications.CHECKSTATUS);
        new  Thread(hc).start();
    }

    @Override
    public String response(String response) {
        Log.d(tag, "message after ccommunication   "+response);

        return null;
    }
}
