package com.example.shesafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String CONTACT_TABLE = "CONTACT_TABLE";
    public static final String COLUMN_CONTACT_NAME = "CONTACT_NAME";
    public static final String COLUMN_CONTACT_PHONE = "CONTACT_PHONE";
    public static final String COLUMN_ID = "ID";
    public static final String POLICE_TABLE = "POLICE_TABLE";
    public static final String POLICE_NO = "POLICE_NO";


    public DataBaseHelper(@Nullable Context context) {
        super(context, "contacts_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String createTableStatement = "CREATE TABLE " + CONTACT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CONTACT_NAME + " TEXT, " + COLUMN_CONTACT_PHONE + " INTEGER)";
        String createTableStatement = "CREATE TABLE " + CONTACT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CONTACT_NAME + " TEXT, " + COLUMN_CONTACT_PHONE + " TEXT)";
        String createTableStatement1 = "CREATE TABLE " + POLICE_TABLE + " (" + POLICE_NO + " TEXT)";

        db.execSQL(createTableStatement);
        db.execSQL(createTableStatement1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+CONTACT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+POLICE_TABLE);
        onCreate(db);

    }

    public ArrayList getPoliceNo(){
        SQLiteDatabase db = this.getReadableDatabase();
        String policePhone;
        ArrayList<String> polcontacts = new ArrayList<String>();

        String queryString = "SELECT " + POLICE_NO + " FROM " + POLICE_TABLE ;
        Cursor cursor = db.rawQuery(queryString, null);



        if (cursor.moveToFirst()){
            //loop through results(If there are any)

            do{
                policePhone = cursor.getString(0);

                /*Contact_model newContact = new Contact_model(contactID, contactName, contactPhone);
                returnList.add(newContact);*/
                polcontacts.add(policePhone);

            }while(cursor.moveToNext());
        }
        else{
            //nothing to add
        }
        //Log.d(TAG, "PHONE NOS: " + contacts);
        cursor.close();
        db.close();
        return polcontacts;

    }

    //---------------------------------------------------------
    public ArrayList getContacts(){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT " + COLUMN_CONTACT_PHONE + " FROM " + CONTACT_TABLE ;

        Cursor cursor = db.rawQuery(queryString, null);

        final String TAG = "MyActivity";

        String contactPhone;
        ArrayList<String> contacts = new ArrayList<String>();

        if (cursor.moveToFirst()){
            //loop through results(If there are any)

            do{
                contactPhone = cursor.getString(0);

                /*Contact_model newContact = new Contact_model(contactID, contactName, contactPhone);
                returnList.add(newContact);*/
                contacts.add(contactPhone);

            }while(cursor.moveToNext());
        }
        else{
            //nothing to add
        }
        //Log.d(TAG, "PHONE NOS: " + contacts);
        cursor.close();
        db.close();
        return contacts;

    }
    //-----------------------------------------------

    public boolean addOne(Contact_model contact_model){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(COLUMN_CONTACT_NAME, contact_model.getName());
        cv.put(COLUMN_CONTACT_PHONE, contact_model.getPhone());

        long insert = db.insert(CONTACT_TABLE, null, cv);
        if (insert ==-1){
            return false;
        }
        else{
            return true;
        }

    }

    public boolean policeaddOne(String polNo){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(POLICE_NO, polNo);

        long insert = db.insert(POLICE_TABLE, null, cv);

        if (insert ==-1){
            return false;
        }
        else{
            return true;
        }


    }


    public boolean deleteOne(Contact_model contact_model){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CONTACT_TABLE + " WHERE " + COLUMN_ID + " = " + contact_model.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public List<Contact_model> getEveryone(){
        List<Contact_model> returnList = new ArrayList<>();

        //get query from db
        String queryString= "SELECT * FROM " + CONTACT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            //loop through results(If there are any)
            do{
                int contactID = cursor.getInt(0);
                String contactName = cursor.getString(1);
                String contactPhone = cursor.getString(2);

                Contact_model newContact = new Contact_model(contactID, contactName, contactPhone);
                returnList.add(newContact);


            }while(cursor.moveToNext());
        }
        else{
            //nothing to add
        }

        cursor.close();
        db.close();
        return returnList;
    }


}
