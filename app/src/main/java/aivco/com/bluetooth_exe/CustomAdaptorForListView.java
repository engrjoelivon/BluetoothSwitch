package aivco.com.bluetooth_exe;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import aivco.com.bluetooth_exe.R;

import java.util.List;

import aivco.com.bluetooth_exe.model.Switch;

/**
 * Created by joel on 1/4/16.
 */
public class CustomAdaptorForListView extends ArrayAdapter<Switch> implements View.OnClickListener{

    List<Switch> switches;
    Activity activity;
    LayoutInflater inflater;
    private int positionSelected;
    TextView switchName;
    CustomizedImageButton switchstate;
    Drawable icon;
    View view;
    public static int count=0;


    public CustomAdaptorForListView(Activity activity, int resource, List<Switch> objects) {
        super(activity, resource, objects);
        this.switches=objects;
        this.activity=activity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position,convertView,parent);
    }

    public View createView(int position, View convertView, ViewGroup parent){
   System.out.println("createView..........................................................." );
        view=inflater.inflate(R.layout.custom_switch_interface,parent,false) ;
        positionSelected=position;
        Switch mySwitch=switches.get(position);
        System.out.println("id is.........................................................." +mySwitch.getId() );


        switchName=(TextView)view.findViewById(R.id.switch_name);
        switchName.setOnClickListener(this);
            final  TextView descriptionName=(TextView)view.findViewById(R.id.description);
        descriptionName.setOnClickListener(this);
        /////On or off Swtich///////////////
        switchstate=(CustomizedImageButton)view.findViewById(R.id.switch_state);
        switchstate.setOnClickListener(this);

        switchstate.setContentDescription(mySwitch.getBluetoothDevice());
        switchstate.setConnectionInstance("myinstance" + String.valueOf(count));
        count++;
        /////dialog that list infos about switch ///////////////
        ImageButton switchinfo=(ImageButton)view.findViewById(R.id.switch_info);
        switchinfo.setOnClickListener(this);
        switchinfo.setContentDescription(mySwitch.getSwitchName());


        switchName.setOnClickListener(this);
        switchName.setText(mySwitch.getSwitchName());
        descriptionName.setText(mySwitch.getDescription());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.switch_state:{
                CustomizedImageButton switchstate=(CustomizedImageButton)v.findViewById(R.id.switch_state);
                System.out.println("Connection instance....is.........................................."+switchstate.getConnectionInstance() );


                Drawable icon=switchstate.getDrawable();
                if(icon.getConstantState().equals(activity.getResources().getDrawable(android.R.drawable.button_onoff_indicator_off).getConstantState()))
                {                System.out.println("position is...................................................switch_state........on");


                    constructBundle(MainActivity.SWITCHSTATESELECTIONKEY,switchstate.getContentDescription().toString());
                    switchstate.setImageDrawable(activity.getResources().getDrawable(android.R.drawable.button_onoff_indicator_on));
                    view.invalidate();
                }
                else{
                    System.out.println("position is...................................................switch_state........off");

                    constructBundle(MainActivity.SWITCHSTATESELECTIONKEY, switchstate.getContentDescription().toString());
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


}
