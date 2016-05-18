package aivco.com.bluetooth_exe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Controllerinterface.OnControllerInterfaceListener} interface
 * to handle interaction events.
 */
public class Controllerinterface extends Fragment {

    private OnControllerInterfaceListener mListener;
    private MainActivity mactivity;

    public Controllerinterface() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mactivity.hideToolBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.controller_layout,container,false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button b=(Button)getView().findViewById(R.id.save);
        final EditText switchName=(EditText)getView().findViewById(R.id.switch_value);
        final EditText description=(EditText)getView().findViewById(R.id.description_value);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                if(!mListener.onControllerFragmentInteraction(switchName.getText().toString(),description.getText().toString())){

                    ConfirmationDialog.createConfirmationDialog(getActivity().getResources().getString(R.string.saving_error),
                            getActivity().getResources().getString(R.string.saving_response),
                            getActivity().getSupportFragmentManager());

                }


            }
        });

    }

    public void createConfirmationDialog(String title,String message)
    {
        Bundle bundle=new Bundle();
        bundle.putString(MainActivity.TITLEKEY,title);
        bundle.putString(MainActivity.MESSAGEKEY,message);
        ConfirmationDialog cd=new ConfirmationDialog();
        cd.setArguments(bundle);
        cd.show(getActivity().getSupportFragmentManager(), title);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnControllerInterfaceListener) {
            mListener = (OnControllerInterfaceListener) context;
            mactivity=(MainActivity)context;
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
    public interface OnControllerInterfaceListener {
        // TODO: Update argument type and name
        boolean onControllerFragmentInteraction(String ... settings);
    }
}
