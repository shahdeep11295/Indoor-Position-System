package com.example.deeps.ips;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SpinnerDatabaseHandler extends SQLiteOpenHelper {

    public SpinnerDatabaseHandler(Context context) {
        // TODO Auto-generated constructor stub
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);
    }


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Spinner.db";
    private static final String TABLE_CLASS="FETR";
    private static final String TABLE_SPINNER= "SpinnerValues";
    public static final String KEY_ID = "id";
    public static final String CLASS = "class";
    public static final String LAT = "lat";
    public static final String LNG="lng";
    public static final String UUID = "uuid";


    public static final String CREATE_TABLE1="CREATE TABLE " + TABLE_CLASS + "("
            + KEY_ID + " INTEGER PRIMARY KEY autoincrement ," + CLASS + " TEXT unique," +  UUID + " TEXT " + ")";

    public static final String CREATE_TABLE="CREATE TABLE " + TABLE_SPINNER + "("
            + KEY_ID + " INTEGER PRIMARY KEY autoincrement ," + CLASS + " TEXT,"+LAT + " TEXT,"+LNG+ " TEXT,"
            +  UUID + " TEXT " + ")";
        SpinnerGet spin;


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE1);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPINNER);

        onCreate(db);
    }

    public void addclass(String clname, String uuid)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CLASS, clname);
        values.put(UUID, uuid);
        db.insert(TABLE_CLASS, null, values);

    }

    public void addvalue(String classNm, String lat, String lon, String uuid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CLASS, classNm);
        values.put(LAT, lat);
        values.put(LNG, lon);
        values.put(UUID, uuid);
        db.insert(TABLE_SPINNER, null, values);
        //db.close();
    }

    public ArrayList<SpinnerGet> getBecoan(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur=db.rawQuery("select * from "+TABLE_SPINNER+" where "+KEY_ID+" ='"+id+"'",null);
        cur.moveToFirst();
        ArrayList<SpinnerGet> list=new ArrayList<>();
        do{
            SpinnerGet data=new SpinnerGet();
            data.setID(cur.getInt(0));
            data.set_class(cur.getString(1));
            data.set_lat(Double.parseDouble(cur.getString(2)));
            data.set_lng(Double.parseDouble(cur.getString(3)));
            data.set_uuid(cur.getString(4));
            list.add(data);
        }while ((cur.moveToNext()));
        return list;
    }
    /*public ArrayList<SpinnerGet> getAllBecoan()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<SpinnerGet> list=new ArrayList<>();

        Cursor cur1=db.rawQuery("select * from "+TABLE_CLASS+"",null);
        cur1.moveToFirst();

        if(cur1.isFirst()) {

            Log.e("spintable",""+cur1.getCount());
            Log.e("spintable",""+cur1.getString(1));

            do {
                Cursor cur = db.rawQuery("select * from " + TABLE_SPINNER + " where " + CLASS + " ='" + cur1.getString(1) + "'", null);
                cur.moveToFirst();

                Log.e("spintable",""+cur.getCount());
                if(cur.isFirst()) {
                    do {
                        SpinnerGet data = new SpinnerGet();
                        data.setID(cur.getInt(0));
                        data.set_class(cur.getString(1));
                        data.set_lat(Double.parseDouble(cur.getString(2)));
                        data.set_lng(Double.parseDouble(cur.getString(3)));
                        data.set_uuid(cur.getString(4));
                        list.add(data);

                    } while ((cur.moveToNext()));
                }
            } while ((cur1.moveToNext()));
        }
        return list;
    }*/
    public ArrayList<SpinnerGet> getAllBecoan()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur=db.rawQuery("select * from "+TABLE_SPINNER+"",null);
        cur.moveToFirst();
        ArrayList<SpinnerGet> list=new ArrayList<>();
        do{
            SpinnerGet data=new SpinnerGet();
            data.setID(cur.getInt(0));
            data.set_class(cur.getString(1));
            data.set_lat(Double.parseDouble(cur.getString(2)));
            data.set_lng(Double.parseDouble(cur.getString(3)));
            data.set_uuid(cur.getString(4));
            list.add(data);
        }while ((cur.moveToNext()));
        return list;
    }

    public void deletebecoanAll()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_CLASS,null,null);
    }
    public void deleteAll()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_SPINNER,null,null);
    }
    //populating the database
    public void vlaueDB(){
        this.addvalue("HOD OFFICE", "21.091624", "73.104548" , "dsd");
        this.addvalue("A201", "21.091679", "73.104609" , "dsd");
        this.addvalue("A202", "21.091852", "73.104633" , "dsd");
        this.addvalue("A203", "21.091863", "73.104579" , "dsd");
        this.addvalue("A204", "21.091881", "73.104502" , "dsd");
        this.addvalue("A205", "21.091877", "73.104478" , "dsd");
        this.addvalue("A206", "21.091714", "73.104434" , "dsd");
        this.addvalue("LAB7&8", "21.091452", "73.104537" , "dsd");

    }






    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static String getKeyId() {
        return KEY_ID;
    }

    public static String getTableSpinner() {
        return TABLE_SPINNER;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

}