package aivco.com.bluetooth_exe;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by joel on 5/13/16.
 */
public class ServerThread extends Thread {

    private final BluetoothServerSocket mmServerSocket;
    private static final UUID uuid=UUID.fromString("a48b9d7d-2372-4ef0-ae85-d8a0ec369449");
    private BluetoothAdapter mBluetoothAdapter;


    public ServerThread() {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final

        BluetoothServerSocket tmp = null;
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        try {
            System.out.println("started........");

            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("First", uuid);
        } catch (IOException e)
        {System.out.println("There is error"); }
        mmServerSocket = tmp;
    }



    @Override
    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                System.out.println("Listening........");

                socket = mmServerSocket.accept();
                System.out.println("Now ther is connection");
            } catch (IOException e) {
                System.out.println("There is error");
                break;
            }

            // If a connection was accepted

           /* if (socket != null)
            {
                // Do work to manage the connection (in a separate thread)
                //manageConnectedSocket(socket);
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error while closing");
                }
                break;
            }
            */
            /***/
            HandleCommunications hc=new HandleCommunications(socket,HandleCommunications.SERVER,"");
            new Thread(hc).start();
        }
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }
}
