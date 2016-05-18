package aivco.com.bluetooth_exe;

import android.app.IntentService;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import java.util.logging.Logger;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.UUID;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ClientService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_CONNECT = "aivco.com.bluetooth_exe.action.connect";
    private static final UUID uuid=UUID.fromString("a48b9d7d-2372-4ef0-ae85-d8a0ec369449");
    private BluetoothSocket bsocket;
    // TODO: Rename parameters
    private BluetoothDevice thisdevice;
    public ClientService() {
        super("ClientService");
    }





    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("....................ON hANDLE INTENT.....................");

        try {

            startServerConnection(getDeviceFromGson(intent));
            connectToServer();
            System.out.println("connection was successful"+bsocket.getRemoteDevice().getName());
            HandleCommunications hc=new HandleCommunications(bsocket,HandleCommunications.CLIENT,HandleCommunications.CHECKSTATUS);
            new Thread(hc).start();
            MainActivity.handler.sendEmptyMessage(MainActivity.CONNECTIONCOMPLETED);
        } catch (IOException e)
        {
           // e.printStackTrace();
            System.out.println("there is error while client is connecting");
            MainActivity.handler.sendEmptyMessage(MainActivity.CONNECTIONUNSUCCESSFUL);
            try {
                bsocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**return a Bluetooth device from Gson Object*/
    private BluetoothDevice getDeviceFromGson(Intent intent){
        String extras = intent.getStringExtra(MainActivity.BLUETOOTHDEVICEKEY);
        BluetoothDevice bdevice=new Gson().fromJson(extras, BluetoothDevice.class);
        System.out.println("device is "+bdevice);
        return bdevice;
    }

    /**
     * startServerConnection runs in the background thread
     */
    private void startServerConnection(BluetoothDevice thisdevice) throws IOException {

     setBluetoothSocket(thisdevice);

        //throw new UnsupportedOperationException("Not yet implemented");
    }


    //step1
    private void setBluetoothSocket(BluetoothDevice remotedevice) throws IOException
    {
        bsocket=remotedevice.createRfcommSocketToServiceRecord(uuid);

    }

    private void connectToServer() throws IOException {

        bsocket.connect();


    }





}
