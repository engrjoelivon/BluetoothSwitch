package aivco.com.bluetooth_exe;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import aivco.com.bluetooth_exe.model.SwitchModuleDatabase;

public class ClientSocketService extends Service {
    private static final UUID uuid=UUID.fromString("a48b9d7d-2372-4ef0-ae85-d8a0ec369449");
    private BluetoothSocket bsocket;
    private static String tag="ClientSocketServicetag";
    private List<BluetoothSocket> bluetoothsockets=new ArrayList<>();
    public ClientSocketService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(tag, "onStartCommand");
        if(bsocket != null && bsocket.isConnected())
        { Log.d(tag, "bsocket is initially connected so will close");

            try {
                bsocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //get all the bluetoothdev available in the db
         for(String bluetoothdev:intent.getStringArrayListExtra(MainActivity.BLUETOOTHDEVICEKEY))
         {


             try
               {

                   setBluetoothSocket(getDeviceFromGson(bluetoothdev));
                   connectToServer();
               }


             catch (IOException e)
                {
                 e.printStackTrace();
                }
         }



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(tag, "bound to service");
        // TODO: Return the communication channel to the service.
        return new BluetoothSocketsList();
    }



    /**return a Bluetooth device from Gson Object*/
    private BluetoothDevice getDeviceFromGson(String bluetoothDeviceAsString){
        return new Gson().fromJson(bluetoothDeviceAsString, BluetoothDevice.class);
    }




    //step1
    private void setBluetoothSocket(BluetoothDevice remotedevice) throws IOException
    {



        bsocket=remotedevice.createRfcommSocketToServiceRecord(uuid);

    }

    private void connectToServer() throws IOException
    {

        //if(!bsocket.isConnected())

            bsocket.connect();
            bluetoothsockets.add(bsocket);
            Log.d(tag, "connection was successful");


        Log.d(tag, "done connecting");
    }




    public class BluetoothSocketsList extends Binder
    {

        public BluetoothSocketsList()
        {
            super();
        }


        public List<BluetoothSocket> getSocketList()
        {
            Log.d(tag, "BluetoothSocketsListclass");
            return bluetoothsockets;
        }
    }




}
