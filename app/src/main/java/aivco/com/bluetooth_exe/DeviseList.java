package aivco.com.bluetooth_exe;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


/**
 * class to list all the available bluetooth adaptors bonded to the phone.
 */
public class DeviseList extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private MainActivity mainActivity;

    public DeviseList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity.hideToolBar();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.devicelistfrag,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button cancel=(Button)getView().findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        Button scan=(Button)getView().findViewById(R.id.scan);
        scan.setOnClickListener(this);


        ListView deviselist=(ListView)getView().findViewById(R.id.devise_list);
        ArrayAdapter<String> deviseArray=new ArrayAdapter<>(getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1);

        for(BluetoothDevice devise:mainActivity.getAllconnectedDevices())
        {

            deviseArray.add(devise.getName()+"\n" + devise.getAddress());
        }

        deviselist.setAdapter(deviseArray);

        deviselist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                mListener.onDeviceListFragmentInteraction(position);

            }
        });
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.scan:
            {
                mListener.performDiscovery();
                break;

            }
            case R.id.cancel:
            {
                mListener.onDeviceListFragmentInteraction(-1);
                break;
            }

        }
    }


    public void performReload(){


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
        void onDeviceListFragmentInteraction(int position);

        void   performDiscovery();

    }
}
