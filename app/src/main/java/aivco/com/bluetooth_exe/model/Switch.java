package aivco.com.bluetooth_exe.model;

import android.bluetooth.BluetoothDevice;

/**
 * Created by joel on 5/15/16.
 * Switch represents an hardware device.
 */
public class Switch
{

   protected String bluetoothDevice;
    protected String switchName;
    protected String description;
    protected String extra1;//extra1 and extra2 represents properties that might be introduced in future
    protected String extra2;
    protected int id;


    public String getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(String bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
