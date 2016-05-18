package aivco.com.bluetooth_exe;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import aivco.com.bluetooth_exe.model.Switch;


/**
 * Created by joel on 5/15/16.
 */
public class SwitchArrayAdapter extends ArrayAdapter<Switch> implements View.OnClickListener,AdapterView.OnItemClickListener {
    private LayoutInflater inflater;
    private List<Switch> allSwitch;
    private int positionSelected;
    private TextView DescriptionName;
    private String description;
    private View view;
    TextView switchName;




    public SwitchArrayAdapter(Context context, int resource, List<Switch> objects) {
        super(context, resource, objects);
        Log.d(MainActivity.tag, "SwitchArrayAdapter");

        this.inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
       this.allSwitch=objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         //super.getView(position, convertView, parent);

        Log.d(MainActivity.tag, position+"");

        return createCustomView(position, convertView, parent);
    }



    public View createCustomView(int position, View convertView, ViewGroup parent){

        Switch switchList=allSwitch.get(position);
        positionSelected=position;
        view=inflater.inflate(R.layout.custom_switch_interface, parent, false);
        ImageView connectionButton=(ImageView)view.findViewById(R.id.connection_status);
        switchName=(TextView)view.findViewById(R.id.switch_name);
        switchName.setText(switchList.getSwitchName());
        switchName.setOnClickListener(this);
        DescriptionName=(TextView)view.findViewById(R.id.description);
        DescriptionName.setText(switchList.getDescription());
         DescriptionName.setOnClickListener(this);
        ImageButton switchState=(ImageButton)view.findViewById(R.id.switch_state);
        ImageButton switchInfo=(ImageButton)view.findViewById(R.id.switch_info);
        switchInfo.setOnClickListener(this);
        switchName.setText(switchList.getSwitchName());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.switch_state:{
                break;
            }
            case R.id.switch_info:
            {

                Message message= MainActivity.bundleHandler.obtainMessage();
                Bundle bundle=new Bundle();
                bundle.putInt(MainActivity.INFOSELECTIONKEY, positionSelected);
                message.setData(bundle);
                MainActivity.bundleHandler.sendMessage(message);
                break;
            }
            case R.id.description:
            {
                TextView vIEW=(TextView)view.findViewById(R.id.description);

                System.out.println("positon description,"+ vIEW.getText().toString());

                Message message= MainActivity.bundleHandler.obtainMessage();
                Bundle bundle=new Bundle();
                bundle.putString(MainActivity.DESCRIPTIONKEY, DescriptionName.getText().toString());
                message.setData(bundle);
                MainActivity.bundleHandler.sendMessage(message);
                break;
            }
            case R.id.switch_name:{

                System.out.println("swithname,"+ switchName.getText().toString());
                Log.d(MainActivity.tag, switchName.getText().toString());

                break;

            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("onItemClick," +position);


    }
}
