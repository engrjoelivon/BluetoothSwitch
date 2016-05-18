package aivco.com.bluetooth_exe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import aivco.com.bluetooth_exe.model.SwitchModuleDatabase;

/**
 * Created by joel on 5/16/16.
 * Dialog to show full details about a switch,eg names,distribution,bluetooth address etc
 */
public class HardwareInterfaceDialog extends DialogFragment implements View.OnClickListener {
    private String switchname,description,bluetoothdevice,bluetoothMacAddress;
    private MainActivity mainActivity;
    private OnFragmentInteractionListener mListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle b=getArguments();
        switchname=b.getString(MainActivity.SWITCHNAMEKEY);
        description=b.getString(MainActivity.SWITCHDESCRIPTIONKEY);
        bluetoothdevice=b.getString(MainActivity.BLUETOOTHDEVICEKEY);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hardware_interface_diaglog,container,false);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditText switchvalue=(EditText)getView().findViewById(R.id.switch_value);
        switchvalue.setText(switchname);
        EditText dis_value=(EditText)getView().findViewById(R.id.dis_value);
        dis_value.setText(description);
        TextView socket_name_value=(TextView)getView().findViewById(R.id.socket_name_value);
        socket_name_value.setText(bluetoothdevice);
        Button delete=(Button)getView().findViewById(R.id.delete);
        delete.setOnClickListener(this);
        Button apply=(Button)getView().findViewById(R.id.apply);
        apply.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.delete:
            {
                SwitchModuleDatabase swd=new SwitchModuleDatabase(getActivity().getBaseContext());
                swd.deleteSwitch(switchname);
                getDialog().cancel();
                break;

            }
            case R.id.apply:
            {   getDialog().cancel();
                break;

            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mainActivity=(MainActivity)context;
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPerformReload();



    }
}
