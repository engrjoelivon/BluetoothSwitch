package aivco.com.bluetooth_exe;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple fragment that carries out the process of discoevering bluetooth devices around
 * to handle interaction events.
 */
public class DiscoveringDevice extends Fragment  {

    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> devicenames;
     BroadcastReceiver broadcastReceiver;
    public final String tag="DiscoveringDevicedebug";
    public final String tagError="DiscoveringDeviceError";
    public List<String> macAddress;
    private List<BluetoothDevice> broadcastResult;

    public DiscoveringDevice() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Log.d(tag, "oncreate");
        macAddress=new ArrayList<>();
        broadcastResult=new ArrayList<>();
        devicenames=new ArrayAdapter<>(getActivity().getBaseContext(),android.R.layout.simple_list_item_1);
         broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(tag,"onReceive outer");
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_FOUND .equals(action))
                {
                    Log.d(tag,"onReceive");
                    BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    broadcastResult.add(device);
                    Log.d(tag,   device.getAddress());
                    devicenames.add(device.getAddress());

                }

            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(broadcastReceiver, filter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discovering_device, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       ListView lv=(ListView) getView().findViewById(R.id.deviselist);
        lv.setAdapter(devicenames);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.device(broadcastResult.get(position));

            }
        });
       Button bt=(Button) getView().findViewById(R.id.cancel);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.device(null);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name


        void  device(BluetoothDevice device);
    }



}
