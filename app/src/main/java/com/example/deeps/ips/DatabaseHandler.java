package com.example.deeps.ips;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context, Object name,
                           Object factory, int version) {
        // TODO Auto-generated constructor stub
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);
    }


    String password;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Mydatabase.db";

    private static final String TABLE_REGISTER= "register";
    public static final String KEY_ID = "id";
    public static final String KEY_USER_NAME = " user_name";
    public static final String KEY_EMAIL_ID = "email_id";
    public static final String KEY_USER_ID="user_id";
    public static final String KEY_MOB_NO = "mobile_number";
    public static final String KEY_PASSWORD = "password";
    public static final String CREATE_TABLE="CREATE TABLE " + TABLE_REGISTER + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_NAME + " TEXT,"+KEY_EMAIL_ID + " TEXT,"+KEY_USER_ID+ " TEXT,"
            + KEY_MOB_NO + " TEXT," + KEY_PASSWORD + " TEXT " + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);

        onCreate(db);
    }

    void addregister(Registerdata registerdata)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME,registerdata.getuserName());
        values.put(KEY_EMAIL_ID, registerdata. getEmailId() );
        values.put(KEY_USER_ID, registerdata.getUserId());
        values.put(KEY_MOB_NO, registerdata.getMobNo());
        values.put(KEY_PASSWORD, registerdata.getPassword());


        db.insert(TABLE_REGISTER, null, values);
        db.close();

    }



    String getregister(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_REGISTER,null,  "user_id=?",new String[]{username},null, null, null, null);

        if(cursor.getCount()<1){
            cursor.close();
            return "Not Exist";
        }
        else if(cursor.getCount()>=1 && cursor.moveToFirst()){

            password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            cursor.close();

        }
        return password;


    }


    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static String getKeyId() {
        return KEY_ID;
    }

    public static String getTableContacts() {
        return TABLE_REGISTER;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

}