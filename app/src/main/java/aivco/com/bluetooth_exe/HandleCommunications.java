package aivco.com.bluetooth_exe;

import android.bluetooth.BluetoothSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import android.util.Log;

/**
 * Created by joel on 5/14/16.
 */
public class HandleCommunications implements  Runnable{
    BluetoothSocket thissocket;
    public static int CLIENT=0;
    public static int SERVER=1;
    private int whoisconnecting;

    private String mess;
    public static final String CHECKSTATUS="on or off";
            ;
    public static final String ON="on";
    public static final String OFF="off";
    public static final String OTHERMESS="unmeaningful message";//
    public String tag="HandleCommunications";



    /**
     * whos is connection coud be client or server,use one of the field static variebles to show if its client connection or serer connections
     * */
    public HandleCommunications(BluetoothSocket socket,int whichConnection,String mess) {
        System.out.println("HandleCommunications constructor");
        Log.d(tag,"HandleCommunications constructor");
        this.thissocket=socket;
        this.whoisconnecting=whichConnection;
        this.mess=mess;

    }

    @Override
    public void run() {
        Log.d(tag,"run method");
        try{

        if(whoisconnecting == CLIENT)
            {write(mess);
              String res= read();
                Log.d(tag,"RESULT FROM READING " + res);
                       // close();
            }
        //server connection whoisconnecting ==SERVER
        else{


            liveServer();

            }
        }

        catch (IOException e) {
            Log.e(tag,"there was error when read ",e.fillInStackTrace());
            e.printStackTrace();
        }

    }


    public String read() throws IOException {
        Log.d(tag,"in read method RESULT FROM READING ");
        BufferedReader br=new BufferedReader(new InputStreamReader(thissocket.getInputStream()));
        return br.readLine();
    }
    public void write(String message) throws IOException {
        Log.d(tag,"RESULT FROM READING " +message);
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(thissocket.getOutputStream()));
        bw.write(message);
        bw.newLine();
        bw.flush();
        Log.d(tag,"write complete");



    }

    public void close() throws IOException {
        Log.d(tag,"I am closing");
        thissocket.close();


    }




    //simulating a server
    public String verifyClientMessage(String clientMess){
        String res=null;
        //would respond normally at app startup to verify status of app
        if (clientMess.equals(CHECKSTATUS))
        {
            res="cant return any status now because i am not connected to hardware";
        }
        else if(clientMess.equals(ON))
        {

            res="i have turned on the switch";
        }
        else if(clientMess.equals(OFF))
        {

            res="i have turned OFF the switch";
        }

        return res;
    }



    /**live server runs inside a loop,any client that connects to it,will have its connection live*/

    public void liveServer()throws IOException{
        while (true)
        {
            String clientMess=read();
            Log.d(tag,"done reading message " +clientMess);
            verifyClientMessage(clientMess);
            write(verifyClientMessage(clientMess));

        }

    }
    /**used only during testing process runs once and closes*/
    public void developmentalServer() throws IOException{
        String clientMess=read();
        Log.d(tag,"done reading message " +clientMess);
        verifyClientMessage(clientMess);
        write(verifyClientMessage(clientMess));
    }
}

