package com.dvr.dvralarm.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dvr.dvralarm.Helpers.Vars;
import com.dvr.dvralarm.Holders.LineHolder;
import com.dvr.dvralarm.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 8/3/16.
 */
public class DBManager {
public DBManager(){}

    public DataBase db;
    public SQLiteDatabase sql;
    public Context context;
    public ArrayList<LineHolder> lineHolders;

    public int id;
    public boolean is_actual;
    public boolean is_active;
    public int hour;
    public int minute;
    public boolean sound;
    public boolean vibration;
    public boolean week_days;
    public boolean day1;
    public boolean day2;
    public boolean day3;
    public boolean day4;
    public boolean day5;
    public boolean day6;
    public boolean day7;

    public DBManager(Context context) {
        this.context=context;
    }


    public void editAlarm(){

    }

    public void deleteAlarm(){

    }
    public void saveNewAlarm(Context context) {

        String query;
        SQLiteDatabase db;
        DataBase datab = new DataBase(context);
        db = datab.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();

        Resources res = context.getResources();
        ContentValues newValues = new ContentValues();
        newValues.put("is_actual", res.getBoolean(R.bool.is_actual));
        newValues.put("is_active", res.getBoolean(R.bool.is_active));
        newValues.put("hour", calendar.getTime().getHours());
        newValues.put("minute", calendar.getTime().getMinutes());
        newValues.put("sound", res.getBoolean(R.bool.sound));
        newValues.put("vibration", res.getBoolean(R.bool.vibration));
        newValues.put("week_days", res.getBoolean(R.bool.week_days));
        newValues.put("day1", res.getBoolean(R.bool.day1));
        newValues.put("day2", res.getBoolean(R.bool.day2));
        newValues.put("day3", res.getBoolean(R.bool.day3));
        newValues.put("day4", res.getBoolean(R.bool.day4));
        newValues.put("day5", res.getBoolean(R.bool.day5));
        newValues.put("day6", res.getBoolean(R.bool.day6));
        newValues.put("day7", res.getBoolean(R.bool.day7));
        db.insert("clock_table", null, newValues);

        query = "SELECT MAX(_id) FROM " + "clock_table";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Vars.lastId = cursor.getInt(0);

        cursor.close();
        db.close();
        datab.close();
    }

    public void updateOneLine(Context context, LineHolder editL){

        SQLiteDatabase db;
        DataBase datab = new DataBase(context);
        db = datab.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put("is_actual", editL.is_actual);
        newValues.put("is_active", editL.is_active);
        newValues.put("hour", editL.hour);
        newValues.put("minute", editL.minute);
        newValues.put("sound", editL.sound);
        newValues.put("vibration", editL.vibration);
        newValues.put("week_days", editL.week_days);
        newValues.put("day1", editL.day1);
        newValues.put("day2", editL.day2);
        newValues.put("day3", editL.day3);
        newValues.put("day4", editL.day4);
        newValues.put("day5", editL.day5);
        newValues.put("day6", editL.day6);
        newValues.put("day7", editL.day7);

        String where = "_id="+editL.id;

        db.update("clock_table", newValues, where, null);
        db.close();
        datab.close();
    }

    public void deleteOneLine(Context context, int lineId){
        SQLiteDatabase db;
        DataBase datab = new DataBase(context);
        db = datab.getWritableDatabase();
        db.delete("clock_table", "_id=" + lineId, null);
        db.close();
        datab.close();
    }

    public void getOneLine(){
        String query;
        SQLiteDatabase db;
        DataBase datab = new DataBase(context);
        String tempString = "555";
        db = datab.getWritableDatabase();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void decodeAllDB(Context context){
        SQLiteDatabase db;
        DataBase datab = new DataBase(context);
        db = datab.getWritableDatabase();
        final String query = "select * from clock_table order by _id desc";
        Cursor cursor = db.rawQuery(query, null);

        db.close();
        datab.close();

    }

    public ArrayList parseToArray(Context context){
        SQLiteDatabase db;
        DataBase datab = new DataBase(context);
        db = datab.getWritableDatabase();
        final String query = "select * from clock_table order by _id desc";
        Cursor cursor = db.rawQuery(query, null);

        lineHolders = new ArrayList<LineHolder>();

        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
            is_actual = cursor.getInt(1) > 0;
            is_active = cursor.getInt(2) > 0;
            hour = cursor.getInt(3);
            minute = cursor.getInt(4);
            sound = cursor.getInt(5) > 0;
            vibration = cursor.getInt(6) > 0;
            week_days = cursor.getInt(7) > 0;
            day1 = cursor.getInt(8) > 0;
            day2 = cursor.getInt(9) > 0;
            day3 = cursor.getInt(10) > 0;
            day4 = cursor.getInt(11) > 0;
            day5 = cursor.getInt(12) > 0;
            day6 = cursor.getInt(13) > 0;
            day7 = cursor.getInt(14) > 0;


            lineHolders.add(new LineHolder(id, is_actual, is_active, hour, minute, sound, vibration, week_days, day1, day2, day3, day4, day5, day6, day7));
        }

        Vars.lineHolders = lineHolders;

        db.close();
        datab.close();
        return lineHolders;
    }

}
