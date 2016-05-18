package aivco.com.bluetooth_exe.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joel on 5/15/16.
 */
public class SwitchModuleDatabase extends Switch
{
    String db_name="db_4_switches";
    private static final String IDCOL="unique_id_for_everybluetooth_mod";
    private static final String SWITCHTABLE="Table_to_store_bluetooth_switch";///actions are when crud operations are performed
    public static  final String BLUETOOTHDEVICECOL="column_for_bluetoothdevice_associated_with_a_name";
    public static  final String NAMECOl="column_to_store_name_for_a_given_bluetoothdevise";
    public static  final String DESCRIPTIONCOL="column_to_store_description_for_a_given_bluetoothdevise";
    public static  final String EXTRACOL1="extracolumn1";//included incase there is a value to store in future
    public static  final String EXTRACOL2="extracolumn2";
    private static String switchTableQuery="create table "+SWITCHTABLE+"("+IDCOL+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+BLUETOOTHDEVICECOL+" text not null,"+
            NAMECOl+" text not null unique,"+DESCRIPTIONCOL+" text,"+EXTRACOL2+" text,"+EXTRACOL1+" text)";
    private SwitchDb switchDb;
    Switch thisSwitch;


    public SwitchModuleDatabase(Context context) {
        int version = 1;
        switchDb=switchDb.getInstance(context,db_name,null, version);


    }

    public boolean addNewSwitch()
    {
     return switchDb.setSwitch(this);
    }

    public List<Switch> getAllSwitch()
    {
      List<Switch> deviceList=new ArrayList<>();
     Cursor c=switchDb.getAllSwitch();
        while(c.moveToNext())
        {
            deviceList.add(generateSwitch(c));
        }
        c.close();
    return deviceList;
    }


    public Switch generateSwitch(Cursor c){


        thisSwitch=new Switch();
        thisSwitch.setSwitchName(c.getString(c.getColumnIndex(NAMECOl)));
        thisSwitch.setDescription(c.getString(c.getColumnIndex(DESCRIPTIONCOL)));
        thisSwitch.setBluetoothDevice(c.getString(c.getColumnIndex(BLUETOOTHDEVICECOL)));
        thisSwitch.setExtra1(c.getString(c.getColumnIndex(EXTRACOL1)));
        thisSwitch.setExtra2(c.getString(c.getColumnIndex(EXTRACOL2)));
        thisSwitch.setId(c.getInt(c.getColumnIndex(IDCOL)));
        return thisSwitch;
    }

    public Switch getSwitch(String name){
       // Cursor c=switchDb.getSwitch(name).moveToNext();
        Cursor c=switchDb.getSwitch(name);

        System.out.println(c.getCount());
        if(c.moveToFirst())
        {
        return generateSwitch(c);

        }

        return null;
    }

    public void deleteSwitch(String name)
    {

        switchDb.removeSwitch(name);
    }




    public static class SwitchDb extends SQLiteOpenHelper {
        private static SwitchDb mInstance=null;
        SQLiteDatabase readsql;
        public static SwitchDb getInstance(Context ctx,String name, SQLiteDatabase.CursorFactory factory, int version) {
            if (mInstance == null) {
                mInstance = new SwitchDb(ctx.getApplicationContext(),name,factory,version);
            }
            return mInstance;


        }
        public SwitchDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
             readsql= getReadableDatabase();


        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        db.execSQL(switchTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        public boolean setSwitch(Switch thisSwitch)
        {
            SQLiteDatabase sdb=getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(BLUETOOTHDEVICECOL,thisSwitch.getBluetoothDevice());
            contentValues.put(NAMECOl,thisSwitch.getSwitchName());
            contentValues.put(DESCRIPTIONCOL, thisSwitch.getDescription());
            long ans=  sdb.insert(SWITCHTABLE, null, contentValues);
            System.out.println("result of setting table"+ans);
            sdb.close();


            return ans != -1;
        }
        public Cursor getAllSwitch()
        {
            SQLiteDatabase readsql= getReadableDatabase();
            return readsql.query(SWITCHTABLE, new String[]{},null,null,null,null,null,null);
        }

        public Cursor getSwitch(String keyword){
            readsql= getReadableDatabase();
           return  readsql.rawQuery("select * from "+SWITCHTABLE+" where "+NAMECOl+" = ?",new String[]{keyword});
        }

        public void removeSwitch(String name)
        {
            SQLiteDatabase readsql= getReadableDatabase();

            readsql.delete(SWITCHTABLE, NAMECOl + " =?", new String[]{name});
            readsql.close();

        }


    }

}
