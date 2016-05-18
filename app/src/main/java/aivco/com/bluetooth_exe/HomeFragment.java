package aivco.com.bluetooth_exe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import aivco.com.bluetooth_exe.model.Switch;
import aivco.com.bluetooth_exe.model.SwitchModuleDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private List<Switch> allSwitch;
    private static HomeFragment hm=null;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment getInstance(){
        if(hm == null)
        {
            hm=new HomeFragment();
            return hm;
        }

        return hm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        allSwitch=new SwitchModuleDatabase(getActivity().getBaseContext()).getAllSwitch();

        ListView ls=(ListView)getView().findViewById(R.id.devise_list);
        CustomAdaptorForListView cs=new CustomAdaptorForListView(getActivity(),R.layout.custom_switch_interface,allSwitch);
        ls.setAdapter(cs);
        ImageButton addDevice=(ImageButton)getView().findViewById(R.id.adddevice);
        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity thisActivity = (MainActivity) getActivity();
                thisActivity.setAllConnectedDevice();

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onHomeFragmentInteraction(uri);
        }
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
        void onHomeFragmentInteraction(Uri uri);
    }
}
