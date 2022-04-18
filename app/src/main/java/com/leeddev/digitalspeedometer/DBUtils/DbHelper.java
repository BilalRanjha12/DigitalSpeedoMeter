package com.leeddev.digitalspeedometer.DBUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.leeddev.digitalspeedometer.Model.HistoryLocationModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {


    // public static final String DATABASE_NAME = "MyDBName.db";
    //  public static final String CONTACTS_TABLE_NAME = "contacts";
    //  public static final String CONTACTS_COLUMN_ID = "id";
    //  public static final String CONTACTS_COLUMN_LONGITUDE = "Longitude";
    //  public static final String CONTACTS_COLUMN_LATITUDE = "latitude";
    //  public static final String CONTACTS_COLUMN_STREET = "speed";
//    public static final String DATABASE_NAME = "speedometerDBName.db";
//    public static final String HISTORY_TABLE_NAME = "DbHistory";
//    public static final String HISTORY_COLUMN_ID = "id";
//    public static final String HISTORY_COLUMN_LONGITUDE = "Longitude";
//    public static final String HISTORY_COLUMN_LATITUDE = "latitude";
//    public static final String HISTORY_COLUMN_SPEED = "speed";
    public static final String DATABASE_NAME = "digitalSpeedoMeterDBName.db";
    public static final String HISTORY_TABLE_NAME = "speedData";
    public static final String HISTORY_COLUMN_ID = "id";
    public static final String HISTORY_COLUMN_DATE= "Date";
    public static final String HISTORY_COLUMN_MAXIMUM_SPEED = "Maximum_Speed";
    public static final String HISTORY_COLUMN_AVERAGE_SPEED= "Average_Speed";
    public static final String HISTORY_COLUMN_DURATION= "Duration";
    public static final String HISTORY_COLUMN_DISTANCE= "Distance";
    private HashMap hp;


    /* public DbHelper(@Nullable Context context) {
         super(context, "contacts", null, 1);
     }*/
    //public DbHelper(@Nullable Context context) {
      //  super(context, "History", null, 1);
  //  }
    public DbHelper(Context context){
        super(context,HISTORY_TABLE_NAME,null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + HISTORY_TABLE_NAME + "("
                        + HISTORY_COLUMN_DATE + " TEXT, " + HISTORY_COLUMN_MAXIMUM_SPEED+ " TEXT, "
                        + HISTORY_COLUMN_AVERAGE_SPEED+ " TEXT, " +HISTORY_COLUMN_DURATION+ " TEXT, " +HISTORY_COLUMN_DISTANCE+ " TEXT "+ ")"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
  //   sqLiteDatabase.execSQL("DROP TABLE IF EXISTS DbHistory");
        onCreate(sqLiteDatabase);
    }

    public boolean insertContact(HistoryLocationModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTORY_COLUMN_DATE,model.getDate());
        contentValues.put(HISTORY_COLUMN_MAXIMUM_SPEED, model.getMaxSpeed());
        contentValues.put(HISTORY_COLUMN_AVERAGE_SPEED, model.getAvgSpeed());
        contentValues.put(HISTORY_COLUMN_DURATION, model.getDuration());
        contentValues.put(HISTORY_COLUMN_DISTANCE, model.getDistance());
        db.insert(HISTORY_TABLE_NAME, null, contentValues);
        return true;
    }



    public boolean updateContact(Integer id,String Date, String Maximum_Speed,
                                 String Average_Speed,String Duration,String Distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(1), Date);
        contentValues.put("Maximum_Speed", Maximum_Speed);
        contentValues.put("Average_Speed", Average_Speed);
        contentValues.put("Duration", Duration);
        contentValues.put("Distance", Distance);
        db.update(HISTORY_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(HISTORY_TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<HistoryLocationModel> getAllContacts() {

        ArrayList<HistoryLocationModel> array_list = new ArrayList<HistoryLocationModel>();


        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+HISTORY_TABLE_NAME, null);

        while (res.moveToNext())
        {
            HistoryLocationModel history = new HistoryLocationModel(String.valueOf(res.getInt(res.getColumnIndex(HISTORY_COLUMN_DATE)))
                    ,String.valueOf(res.getInt(res.getColumnIndex(HISTORY_COLUMN_MAXIMUM_SPEED)))
                    ,String.valueOf(res.getInt(res.getColumnIndex(HISTORY_COLUMN_DATE))),
                    String.valueOf(res.getInt(res.getColumnIndex(HISTORY_COLUMN_DURATION))),
                            String.valueOf(res.getInt(res.getColumnIndex(HISTORY_COLUMN_DISTANCE))));
            array_list.add(history);
        }
        res.moveToFirst();
        res.close();
        return array_list;
    }
}




