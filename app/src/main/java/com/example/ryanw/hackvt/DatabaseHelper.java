package com.example.ryanw.hackvt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by ryanw on 2/17/2017.
 */


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    // Database Info
    private static final String DATABASE_NAME = "ParkingLots";
    private static final int DATABASE_VERSION = 1;

    //Table Names
    private static final String PARKING_LOTS = "parkingLots";


    //ParkingLot Table Columns
    private static final String _ID = "_id";
    private static final String NAME = "name";
    private static final String MAX_LATITUDE = "maxLatitude";
    private static final String MAX_LONGITUDE = "maxLongitude";
    private static final String MIN_LATITUDE = "minLatitude";
    private static final String MIN_LONGITUDE = "minLongitude";
    private static final String TIME_RANGE_WITH_PASS_WEEKDAY = "timeRangeWithPassWeekday";
    private static final String TIME_RANGE_WITHOUT_PASS_WEEKDAY= "timeRangeWithoutPassWeekday";
    private static final String TIME_RANGE_WITH_PASS_WEEKEND = "timeRangeWithPassWeekend";
    private static final String TIME_RANGE_WITHOUT_PASS_WEEKEND = "timeRangeWithoutPassWeekend";
    private static final String ALLOWED_ON_WEEKDAYS_WITH_PASS = "allowedOnWeekdaysWithPass";
    private static final String ALLOWED_ON_WEEKDAYS_WITHOUT_PASS = "allowedOnWeekdaysWithoutPass";
    private static final String ALLOWED_ON_WEEKENDS_WITH_PASS = "allowedOnWeekendsWithPass";
    private static final String ALLOWED_ON_WEEKENDS_WITHOUT_PASS = "allowedOnWeekendsWithoutPass";
    private static final String PICTURE = "picture";

    private static DatabaseHelper myDatabaseHelper;

    private static ArrayList<ParkingLot> parkingLots = new ArrayList<ParkingLot>();

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (myDatabaseHelper == null) {
            myDatabaseHelper = new DatabaseHelper(context.getApplicationContext());
        }
        return myDatabaseHelper;
    }

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PARKING_LOTS_TABLE = "CREATE TABLE " + PARKING_LOTS +
                "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                NAME + " TEXT," +
                MAX_LATITUDE + " FLOAT," +
                MAX_LONGITUDE + " FLOAT," +
                MIN_LATITUDE + " FLOAT," +
                MIN_LONGITUDE + " FLOAT," +
                TIME_RANGE_WITH_PASS_WEEKDAY + " TEXT," +
                TIME_RANGE_WITHOUT_PASS_WEEKDAY + " TEXT," +
                TIME_RANGE_WITH_PASS_WEEKEND + " TEXT," +
                TIME_RANGE_WITHOUT_PASS_WEEKEND + " TEXT," +
                ALLOWED_ON_WEEKDAYS_WITH_PASS + " TEXT," +
                ALLOWED_ON_WEEKENDS_WITH_PASS + " TEXT," +
                ALLOWED_ON_WEEKDAYS_WITHOUT_PASS + " TEXT," +
                ALLOWED_ON_WEEKENDS_WITHOUT_PASS + " TEXT," +
                PICTURE + " BLOB" +
                ")";
        db.execSQL(CREATE_PARKING_LOTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + PARKING_LOTS);
        }
    }

    /**
     * Inserts parking lot objects into the table.
     * @param parkingLot the parking lot to be inserted.
     * @throws SQLException if table is not found.
     */
    public void insertParkingLots(ParkingLot parkingLot) throws SQLException {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(NAME, parkingLot.getName());
        values.put(MAX_LATITUDE, parkingLot.getlotMaxLatitude());
        values.put(MAX_LONGITUDE, parkingLot.getlotMaxLongitude());
        values.put(MIN_LONGITUDE, parkingLot.getlotMinLongitude());
        values.put(MIN_LATITUDE, parkingLot.getlotMinLatitude());
        values.put(TIME_RANGE_WITH_PASS_WEEKDAY, parkingLot.getLotTimesAllowedOnWeekdaysSP());
        values.put(TIME_RANGE_WITHOUT_PASS_WEEKDAY, parkingLot.getLotTimesAllowedOnWeekdaysNSP());
        values.put(TIME_RANGE_WITH_PASS_WEEKEND, parkingLot.getLotTimesAllowedOnWeekendsSP());
        values.put(TIME_RANGE_WITHOUT_PASS_WEEKEND, parkingLot.getLotTimesAllowedOnWeekendsNSP());

        values.put(ALLOWED_ON_WEEKDAYS_WITH_PASS, parkingLot.getLotAllowedOnWeekdaysSP());
        values.put(ALLOWED_ON_WEEKENDS_WITH_PASS, parkingLot.getLotAllowedOnWeekendsSP());
        values.put(ALLOWED_ON_WEEKDAYS_WITHOUT_PASS, parkingLot.getLotAllowedOnWeekdaysNSP());
        values.put(ALLOWED_ON_WEEKENDS_WITHOUT_PASS, parkingLot.getLotAllowedOnWeekendsNSP());
        //values.put(PICTURE, parkingLot.getPicture());


        database.insertOrThrow(PARKING_LOTS, null, values);
        database.setTransactionSuccessful();
    }

     /*
        fetch all data from ParkingLotsTable
    */

    public ArrayList<ParkingLot> getAllParkingLots() {

        ArrayList<ParkingLot> parkingLotDetails = new ArrayList<>();

        String LOT_DETAIL_SELECT_QUERY = "SELECT * FROM " + PARKING_LOTS;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(LOT_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    ParkingLot parkingLot = new ParkingLot();
                    parkingLot.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                    parkingLot.setlotMaxLatitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MAX_LATITUDE))));
                    parkingLot.setlotMaxLongitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MAX_LONGITUDE))));
                    parkingLot.setlotMinLatitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MIN_LATITUDE))));
                    parkingLot.setlotMinLongitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MIN_LONGITUDE))));
                    parkingLot.setLotTimesAllowedOnWeekdaysSP(cursor.getString(cursor.getColumnIndex(TIME_RANGE_WITH_PASS_WEEKDAY)));
                    parkingLot.setlotTimesAllowedOnWeekdaysNSP(cursor.getString(cursor.getColumnIndex(TIME_RANGE_WITHOUT_PASS_WEEKDAY)));
                    parkingLot.setLotTimesAllowedOnWeekendsSP(cursor.getString(cursor.getColumnIndex(TIME_RANGE_WITH_PASS_WEEKEND)));
                    parkingLot.setlotTimesAllowedOnWeekendsNSP(cursor.getString(cursor.getColumnIndex(TIME_RANGE_WITHOUT_PASS_WEEKEND)));
                    parkingLot.setLotAllowedOnWeekdaysSP(cursor.getString(cursor.getColumnIndex(ALLOWED_ON_WEEKDAYS_WITH_PASS)));
                    parkingLot.setLotAllowedOnWeekdaysNSP(cursor.getString(cursor.getColumnIndex(ALLOWED_ON_WEEKDAYS_WITHOUT_PASS)));
                    parkingLot.setLotAllowedOnWeekendsSP(cursor.getString(cursor.getColumnIndex(ALLOWED_ON_WEEKENDS_WITH_PASS)));
                    parkingLot.setLotAllowedOnWeekendsNSP(cursor.getString(cursor.getColumnIndex(ALLOWED_ON_WEEKENDS_WITHOUT_PASS)));
                    parkingLotDetails.add(parkingLot);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return parkingLotDetails;

    }

    /**
     * Creation of different parking lot objects.
     */
    public void createParkingLots()
    {
        //Squires Parking Lot.
        ParkingLot squires = new ParkingLot();
        squires.setName("Squires Parking Lot");
        squires.setLotAllowedOnWeekdaysSP("true");
        squires.setLotAllowedOnWeekdaysNSP("true");
        squires.setLotAllowedOnWeekendsSP("true");
        squires.setLotAllowedOnWeekendsNSP("true");
        squires.setLotTimesAllowedOnWeekdaysSP("12am - 11:59pm");
        squires.setlotTimesAllowedOnWeekdaysNSP("5pm - 7am");
        squires.setLotTimesAllowedOnWeekendsSP("12am - 11:59pm");
        squires.setlotTimesAllowedOnWeekendsNSP("12am - 11:59pm");

        squires.setlotMinLongitude(-80.417546f);
        squires.setlotMaxLongitude(-80.416135f);
        squires.setlotMinLatitude(37.229146f);
        squires.setlotMaxLatitude(37.229218f);
        //squires.setPicture(//Blob file);
        try {
            insertParkingLots(squires);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        parkingLots.add(squires);
    }
}
