package aivco.com.bluetooth_exe;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by joel on 5/18/16.
 * Extended the ImageButton class so as to be able to add the getConnection instance and
 * set connection Instance method.
 * Need the connection Instance which is a bluetooth socket,to connect to bluetooth to communicate
 * with remoteHardware
 */
public class CustomizedImageButton extends ImageButton {
    BluetoothSocket bts;
    String name;
    public CustomizedImageButton(Context context) {
        super(context);
    }

    Handler handler;

    public CustomizedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomizedImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }





    public BluetoothSocket getConnectionInstance(){
        return this.bts;
    }

    public void setConnectionInstance(BluetoothSocket connectionInstance){
        this.bts=connectionInstance;
    }





}
