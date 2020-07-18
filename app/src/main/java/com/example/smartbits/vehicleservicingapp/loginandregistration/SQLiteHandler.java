package com.example.smartbits.vehicleservicingapp.loginandregistration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Sampler;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 30/3/17.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Name
    private static final String DATABASE_NAME = "vehicleService";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // login table name
    private static final String TABLE_USER = "user";

    // cars table name
    private static final String TABLE_CARS = "CARS";

    // SERVICE CENTERS TABLE NAME
    private static final String TABLE_SERVICE_CENTERS = "service_centers";

    // history table name
    private static final String TABLE_HISTORY = "history";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_MODEL = "model";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_REGNO = "regno";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_COMPANY = "company";
    private static final String KEY_REGISTERED_CAR_ID = "registered_car_id";
    private static final String KEY_CENTER_ID = "centerid";
    private static final String KEY_PICKUP = "pickup";
    private static final String KEY_CHARGES = "charges";
    private static final String KEY_LATITUDE = "lat";
    private static final String KEY_LONGITUDE = "lon";




    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_USERNAME + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT)";

        String CREATE_CARS_TABLE = "CREATE TABLE " + TABLE_CARS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_MODEL + " TEXT," + KEY_REGNO + " TEXT)";

        String CREATE_SERVICE_CENTERS_TABLE = "CREATE TABLE " + TABLE_SERVICE_CENTERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_PHONE + " TEXT,"
                + KEY_COMPANY + " TEXT," + KEY_EMAIL + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT)";

        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_USERNAME + " TEXT," + KEY_REGISTERED_CAR_ID + " TEXT," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT,"
                + KEY_CENTER_ID + " INTEGER," + KEY_PICKUP + " TEXT," + KEY_CHARGES + " INTEGER)";



        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_CARS_TABLE);
        db.execSQL(CREATE_SERVICE_CENTERS_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);

        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_CENTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */

    public void addUser(int id, String name, String username, String email, String lat, String lon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_USERNAME, username);
        values.put(KEY_LATITUDE, lat);
        values.put(KEY_LONGITUDE, lon);
        //Inserting row
        long uid = db.insert(TABLE_USER, null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite: " + uid);
    }

    /**
     * Storing service centers in database
     */

    public void addServiceCenters(int id, String name, String address, String phone, String company, String email, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, name);
        String add = address.replaceAll("[\\t\\n\\r]", " ");
        values.put(KEY_ADDRESS, add);
        values.put(KEY_PHONE, phone);
        values.put(KEY_COMPANY, company);
        values.put(KEY_EMAIL, email);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);
        //Inserting row
        long uid = db.insert(TABLE_SERVICE_CENTERS, null, values);
        db.close();

        Log.d(TAG, "New service centers into sqlite: " + uid);
    }

    /**
     * Storing Car details in database
     */
    public void addCar(int id, String name, String model, String regno) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, name);
        values.put(KEY_MODEL, model);
        values.put(KEY_REGNO, regno);

        //Inserting row
        long uid = db.insert(TABLE_CARS, null, values);
        db.close();

        Log.d(TAG, "New Car inserted into sqlite: " + uid);
    }


    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //move to first row
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            user.put("id", cursor.getString(0));
            user.put("name", cursor.getString(1));
            user.put("username", cursor.getString(3));
            user.put("email", cursor.getString(2));
            user.put("lat", cursor.getString(4));
            user.put("lon", cursor.getString(5));
        }
        cursor.close();
        db.close();
        //return user
        Log.d(TAG, "Fetching user from SQlite: " + user.toString());
        return user;
    }

    /**
     * Getting user data from database
     */
    public HashMap<Integer, String> getServiceCenterDetails(String comp) {
        HashMap<Integer, String> serviceCenters = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_SERVICE_CENTERS + " WHERE company = '" + comp +"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //move to first row
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            do {
                String serviceCenter = cursor.getString(1) + "\n" + cursor.getString(2);
                serviceCenters.put(cursor.getInt(0), serviceCenter);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return serviceCenters;
    }

    /**
     * Getting cars data from database
     */
    public ArrayList<String> getCarDetails() {
        String selectQuery = "SELECT * FROM " + TABLE_CARS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> cars = new ArrayList<>();
        //move to first row
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            do {
                String car = cursor.getString(1) + " " + cursor.getString(2) ;
                cars.add(car);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        //return user
        Log.d(TAG, "Fetching cars from SQlite: " + cars.toString());
        return cars;
    }

    /**
     * Re-create database delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        int count = db.delete(TABLE_USER, "1", null);
        db.close();

        Log.d(TAG, "Deleted all user info from the sqlite");
    }

    /**
     * Re-create database delete all tables and create them again
     */
    public void deleteCars() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        int count = db.delete(TABLE_CARS, "1", null);
        db.close();

        Log.d(TAG, "Deleted all cars info from the sqlite");
    }

    public void deleteServiceCenters() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        int count = db.delete(TABLE_SERVICE_CENTERS, "1", null);
        db.close();

        Log.d(TAG, "Deleted all service centers info from the sqlite");
    }

    public String getCarId(String name, String model) {
        String regno;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT regno FROM " + TABLE_CARS + " WHERE name = '" + name + "' AND model = '" + model + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            regno = cursor.getString(0);
        } else
            regno = "";
        return regno;
    }

    public String getServiceCenterId(String address) {
        String id = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT id from service_centers WHERE address = '" + address + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            id = ""+cursor.getInt(0);
        }
        return id;
    }

    public String getCarById(String id) {
        String car = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * from CARS WHERE regno = '" + id + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            car = cursor.getString(1) + " " + cursor.getString(2);
        }


        return car;
    }

    public String getCenterNameById(int id) {
        String center = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * from service_centers WHERE id = '" + id + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            center = cursor.getString(1) + " " + cursor.getString(2);
        }


        return center;
    }

    public Map<String, String> getCenterById(int id) {
        HashMap<String, String> values = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * from service_centers WHERE id = '" + id + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            values.put(KEY_ID, cursor.getString(0));
            values.put(KEY_NAME, cursor.getString(1));
            values.put(KEY_ADDRESS, cursor.getString(2));
            values.put(KEY_PHONE, cursor.getString(3));
            values.put(KEY_COMPANY, cursor.getString(4));
            values.put(KEY_EMAIL, cursor.getString(5));
            values.put(KEY_LATITUDE, cursor.getString(6));
            values.put(KEY_LONGITUDE, cursor.getString(7));
        }


        return values;
    }

    public void addHistory(String username, String registered_car_id, String date, String time, int centerid, String pickup, int charges) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_REGISTERED_CAR_ID, registered_car_id);
        values.put(KEY_DATE, date);
        values.put(KEY_TIME, time);
        values.put(KEY_CENTER_ID, centerid);
        values.put(KEY_PICKUP, pickup);
        values.put(KEY_CHARGES, charges);
        //Inserting row
        long uid = db.insert(TABLE_HISTORY, null, values);
        db.close();

        Log.d(TAG, "New History inserted into sqlite: " + uid);
    }

    public Map<String, List<String>> getHistory() {
        Map<String, List<String>> histories = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_HISTORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                String carid = cursor.getString(1);
                List<String> history = new ArrayList<>();
                history.add("Car ID: " + cursor.getString(1));
                history.add("Date: " + cursor.getString(2));
                history.add("Time: " + cursor.getString(3));
                history.add("Center ID: " + cursor.getString(4));
                history.add("Pickup: " + cursor.getString(5));
                histories.put(carid, history);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return histories;
    }


    /**
     * Re-create database delete all tables and create them again
     */
    public void deleteHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        int count = db.delete(TABLE_HISTORY, "1", null);
        db.close();

        Log.d(TAG, "Deleted all history info from the sqlite");
    }

}
