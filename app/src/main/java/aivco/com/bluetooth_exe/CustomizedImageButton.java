package aivco.com.bluetooth_exe;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by johnanderson1 on 5/18/16.
 */
public class CustomizedImageButton extends ImageButton {
    BluetoothSocket bts;
    String name;
    public CustomizedImageButton(Context context) {
        super(context);
    }

    public CustomizedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomizedImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomizedImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    public String getConnectionInstance(){

  return this.name;
    }

    public void setConnectionInstance(String connectionInstance){

    this.name=connectionInstance;
    }
}
