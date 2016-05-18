package aivco.com.bluetooth_exe;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import aivco.com.bluetooth_exe.model.SwitchModuleDatabase;
import aivco.com.bluetooth_exe.model.Switch;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements DeviseList.OnFragmentInteractionListener,DiscoveringDevice.OnFragmentInteractionListener,
        Controllerinterface.OnControllerInterfaceListener,HomeFragment.OnFragmentInteractionListener,HardwareInterfaceDialog.OnFragmentInteractionListener  {

    private static final int STARTINGBT = 1;
    public static final int CONNECTIONCOMPLETED =1;
    public static final int CONNECTIONUNSUCCESSFUL =2;
    public static final int ACTIONCOMPLETED=3;
    public static final int SWITCHSTATEKEY =5;//position from the array representing user selection
    private static final int ADDTOBACKSTACK = 1;
    private static final int DONTADDTOBACKSTACK=0;
    public static final String INFOSELECTIONKEY ="user selection";//position from the array representing user selection
    public static final String DESCRIPTIONKEY ="switch description";//position from the array representing user selection
    public static final String TITLEKEY ="key for title passed confirmation dialog" ;
    public static final String MESSAGEKEY="key for message passed to confirmation dialog" ;
    public static final String SWITCHNAMEKEY ="key for switchname passed to confirmation dialog";
    public static final String SWITCHDESCRIPTIONKEY="key for switchdescription passed to confirmation dialog";;
    public static final String SWITCHSTATESELECTIONKEY="key for switchstate passed to confirmation dialog";;

    private BluetoothAdapter bs;
    private Set<BluetoothDevice> bondedDevise;
    private List<BluetoothDevice> bluetoothDevice;//created a bluetooth device because cant get items from set
    private BluetoothDevice connectedDevice;
    Toolbar toolbar;
    private static final String MAINFRAGMENT="mainfragment";
    public static final String tag="bluetoothMainActivity";
    public static final String BLUETOOTHDEVICEKEY="remotebluetoothdevice";
    public static final String TYPEOFACTIONKEY="keyforvalidationaaction";//when contacting the clientservice,it needs to know what kind of action is intended
    private ProgressDialog pg;
    public static Handler handler,bundleHandler;
    private SwitchModuleDatabase switchModuleDatabase;
    static List<Switch> allSwitch;
    Switch btswitch;
    Bundle bundle;
    HomeFragment hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchModuleDatabase=new SwitchModuleDatabase(this);
        btswitch=new Switch();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(tag, "onCreate");



        hm=HomeFragment.getInstance();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.container, hm);
        ft.addToBackStack("mainfragment");
        ft.commit();
        //mainFragment=MainFragment.getInstance();
        //startFragment(mainFragment,"mainfragment",ADDTOBACKSTACK);

        //return true if bluetooth exist and is enabled,if not enabled carry out actions inside on activity result
      if(checkIfBluetoothExist())
      {

      }
      handleMessages();
        handleComplexMessages();

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("back from ........onResume..................of parent");


    }



    /**Used to handle empty messages*/
    public void handleMessages(){
        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                int a=message.what;
                switch (a)
                {
                    case CONNECTIONCOMPLETED:{
                        pg.cancel();

                        startFragment(new Controllerinterface(),"Lightinterface",ADDTOBACKSTACK);

                        break;
                    }
                    case CONNECTIONUNSUCCESSFUL:{
                        pg.cancel();
                        createConfirmationDialog(getResources().getString(R.string.connection_unsuccessfull),getResources().getString(R.string.connection_error));

                        break;
                    }
                    case SWITCHSTATEKEY:{

                        break;
                    }
                    case ACTIONCOMPLETED:{

                        break;
                    }
                }
                return false;
            }
        });
    }



    /**Used to handle messages that includes bundles*/
    public void handleComplexMessages(){
        bundleHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                Bundle messageb=message.getData();
                for(String key:messageb.keySet())
                {
                    switch (key){
                        case INFOSELECTIONKEY:
                        {
                            loadInfo(messageb.getString(INFOSELECTIONKEY));
                            break;
                        }
                        case DESCRIPTIONKEY:
                        {
                           System.out.println("position of key"+messageb.getString(DESCRIPTIONKEY));
                            createConfirmationDialog("", messageb.getString(DESCRIPTIONKEY));
                            break;
                        }
                        case SWITCHSTATESELECTIONKEY:
                        {
                            System.out.println("position of key"+messageb.getString(DESCRIPTIONKEY));
                            connectToServer(messageb.getString(SWITCHSTATESELECTIONKEY));
                            break;
                        }
                    }

                }


            return false;
            }});


    }

    //1.check if host supports bluetooth
    public boolean checkIfBluetoothExist()
    {

         bs=BluetoothAdapter.getDefaultAdapter();
        if(bs != null)
        {
            //if bluetooth is not enabled return false
           if(!bs.isEnabled())
           {
              enableBluetoothIfExist();
               return false;
           }
           //if bluetooth is already enabled return true
            else{

               return true;
           }

        }
        else{

            finish();
        }
        //never get returned
       return false;
    }
   //2.if bluetooth exist enable bluetooth
   public void enableBluetoothIfExist()
   {
       Intent intent=new Intent();
       intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
       startActivityForResult(intent, STARTINGBT);
   }


    //3. If bluetooth is enabled get all bonded bluetooth module from the host device.
    public void setAllConnectedDevice(){
       bondedDevise=bs.getBondedDevices();

        if(bondedDevise.size() > 1)
        {
            bluetoothDevice=new ArrayList<>(bondedDevise);
            startFragment(new DeviseList(),"bondedDevise",ADDTOBACKSTACK );
        }
    }


    //if a bluetooth module is not yet bonded scan for all nearyby bluetooth
    public void discoverBluetooth(){
        bs.startDiscovery();
    }

    public List<BluetoothDevice>  getAllconnectedDevices(){

        return bluetoothDevice;
    }


    /**@param fragment the fragment to create
     *  @param backstackorNot A constant that determines if the previous fragment should be added to backstack
     * */
    public void startFragment(Fragment fragment,String name,int backstackorNot){
        Log.d(tag, "startFragment");

        if(backstackorNot == ADDTOBACKSTACK){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.addToBackStack(name);
        ft.replace(R.id.container,fragment);
        ft.commit();}
        else{
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.container,fragment);
            ft.commit();

        }

    }

    public void removeFragment(){
        FragmentManager fm=getSupportFragmentManager();
        fm.popBackStack(MAINFRAGMENT, 0);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case STARTINGBT:
                {
                    break;
                }

            }
        }


    }

    public void hideToolBar() {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void showToolBar(){

        //toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
        getSupportActionBar().show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }


    /**@param  remoteDevice bluetooth device to connect to.If connection is successful execution returns to handler*/
    public void connectToServer(BluetoothDevice remoteDevice){
        bs.cancelDiscovery();//ensure to cancel discovery before making connection
        connectedDevice=remoteDevice;
        Intent intent=new Intent();
        intent.putExtra(BLUETOOTHDEVICEKEY, new Gson().toJson(remoteDevice));
        intent.putExtra(TYPEOFACTIONKEY, HandleCommunications.CHECKSTATUS);
        intent.setClass(this, ClientService.class);
        startService(intent);
        createDialog(getResources().getString(R.string.connection_status), this);

    }



    /**@param  remoteDevice String representing bluetoothdevice,If connection is successful execution returns to handler*/
    public void connectToServer(String remoteDevice){
        bs.cancelDiscovery();//ensure to cancel discovery before making connection
        Intent intent=new Intent();
        intent.putExtra(BLUETOOTHDEVICEKEY, remoteDevice);
        intent.putExtra(TYPEOFACTIONKEY, HandleCommunications.ON);
        intent.setClass(this, ClientService.class);
        startService(intent);
        createDialog(getResources().getString(R.string.connection_status), this);

    }

    /**starts the server*/
    public void startServer()
    {
       new ServerThread().start();


    }

    /**<h1>method to create a list for every switch hardware registered for the app</h1>
     * @param settings settings is an array that holds all the user entered values*/
    public boolean newSwitchToDb(String...settings)
    {
        System.out.println("username returned by user" + settings[0]);

        switchModuleDatabase=new SwitchModuleDatabase(this);
        switchModuleDatabase.setSwitchName(settings[0]);
        switchModuleDatabase.setDescription(settings[1]);
        switchModuleDatabase.setBluetoothDevice(new Gson().toJson(connectedDevice));

        System.out.println("bluetooth"+(connectedDevice.toString()));

        return switchModuleDatabase.addNewSwitch();
    }

    public void createDialog(String status,Context context)
    {

        pg=new ProgressDialog(context);
        pg.setMessage(status);
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.setCanceledOnTouchOutside(false);
        pg.show();

    }

    /**loads the info fragment based on user selection
     * @param name this is an string value.Since names are unique inside the db,the name will be used to search all the other values
     *
     * */
    public void loadInfo(String name){
        bundle=new Bundle();
        btswitch=switchModuleDatabase.getSwitch(name);
        HardwareInterfaceDialog hardwareInterfaceDialog
                =new HardwareInterfaceDialog();


        bundle.putString(SWITCHNAMEKEY,btswitch.getSwitchName());
        bundle.putString(SWITCHDESCRIPTIONKEY,btswitch.getDescription());
        bundle.putString(BLUETOOTHDEVICEKEY, btswitch.getBluetoothDevice());
        hardwareInterfaceDialog.setArguments(bundle);
        hardwareInterfaceDialog.show(getSupportFragmentManager(), "");
        bundle=null;
        btswitch=null;

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        if(id == R.id.server)
        {
        startServer();

        }





        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeviceListFragmentInteraction(int position) {
        if(position != -1)
        {
            connectToServer(bluetoothDevice.get(position));
        }

        removeFragment();
         showToolBar();
    }


    @Override
    public void  performDiscovery(){
        discoverBluetooth();
        startFragment(new DiscoveringDevice(), "DiscoveringDevice", ADDTOBACKSTACK);

    }

    /**handles return from DiscoveryDevice fragment*/
    @Override
    public void device(BluetoothDevice device) {
        if(device != null)
        {connectToServer(device);}
        removeFragment();
        showToolBar();

    }

    /**handles response from Controller interface
     * @param settings represents the entered user values
     * */
    @Override
    public boolean onControllerFragmentInteraction(String ... settings) {
        if(settings != null)
        {
         if(!newSwitchToDb(settings))
         {
             return false;
         }
        }
        removeFragment();
        showToolBar();

      return true;
    }

    public void createConfirmationDialog(String title,String message)
    {
        Bundle bundle=new Bundle();
        bundle.putString(TITLEKEY,title);
        bundle.putString(MESSAGEKEY,message);
        ConfirmationDialog cd=new ConfirmationDialog();
        cd.setArguments(bundle);
        cd.show(getSupportFragmentManager(),title);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showToolBar();


    }

    @Override
    public void onHomeFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPerformReload() {
        hm=HomeFragment.getInstance();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.container, hm);
        ft.addToBackStack("mainfragment");
        ft.commit();

    }







    }



