package com.dvr.dvralarm;

        import android.app.Activity;
        import android.app.AlarmManager;
        import android.app.Dialog;
        import android.app.PendingIntent;
        import android.app.TimePickerDialog;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ListView;
        import android.widget.TimePicker;
        import android.widget.Toast;

        import com.dvr.dvralarm.DataBase.DBManager;
        import com.dvr.dvralarm.DataBase.DataBase;
        import com.dvr.dvralarm.Helpers.Vars;
        import com.dvr.dvralarm.Holders.LineHolder;
        import com.dvr.dvralarm.View.LineAdapter;

        import java.io.IOException;
        import java.text.DateFormat;
        import java.text.DateFormatSymbols;
        import java.util.Calendar;
        import java.util.Date;


public class AlarmActivity extends Activity {

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static AlarmActivity inst;

    public static AlarmActivity instance() {
        return inst;
    }


    public DBManager dbm;
    public ListView LinesListView;
    public LineAdapter lineAdapter;


    private int DIALOG_TIME = 1;
    private int editH = 0;
    private int editM = 0;
    public int editId = 0;
    public LineHolder editHolder;
    public TimePickerDialog timeP;


    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_TIME) {
            timeP = new TimePickerDialog(this, myCallBack, editH, editM, true);

            return timeP;
        }
        return super.onCreateDialog(id);
    }

    private TimePickerDialog.OnTimeSetListener myCallBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            editH = hourOfDay;
            editM = minute;
            editHolder.hour = hourOfDay;
            editHolder.minute = minute;
            dbm.updateOneLine(getApplicationContext(), editHolder);
            reloadList(false);
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);



        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //checkData();
        DataBase db = new DataBase(this);
        try {
            if (!db.createDataBase()) {
                System.out.println("database exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbm=new DBManager();

        // E X P O R T   D B
        //db.exportDB();
        db.close();

        LinesListView = (ListView)findViewById(R.id.lstText);
        lineAdapter = new LineAdapter(AlarmActivity.this, dbm.parseToArray(getApplicationContext()), this);
        LinesListView.setAdapter(lineAdapter);

    }

    public void addNewLine(View view){
        dbm.saveNewAlarm(getApplicationContext());
        reloadList(true);
    }
    public void deleteLine(int lineId){
        dbm.deleteOneLine(getApplicationContext(), lineId);
        reloadList(false);
    }

    public void reloadList(boolean isNew){
        LineAdapter lineAdapter = new LineAdapter(AlarmActivity.this, dbm.parseToArray(getApplicationContext()), this);
        LinesListView.setAdapter(lineAdapter);
        if(isNew)
            dialogTime(getLineHolderById());
    }

    public LineHolder getLineHolderById(){

        LineHolder tempL = null;
        for(int i=0; i<Vars.lineHolders.size(); i++){
            if(Vars.lineHolders.get(i).id == Vars.lastId){
                tempL =  Vars.lineHolders.get(i);
            }
        }
        return tempL;
    }

    public void updateLine(){
        dbm.updateOneLine(getApplicationContext(), Vars.lineHolder);
    }

    public void dialogTime(LineHolder newL){
        editHolder = newL;
        editH = editHolder.hour;
        editM = editHolder.minute;
        editId = editHolder.id;
        showDialog(DIALOG_TIME);
        timeP.updateTime(editHolder.hour, editHolder.minute);
    }

    public void setAlarm(LineHolder ln, boolean isOn){
        Intent myIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);

        Calendar calendar = Calendar.getInstance();
        Calendar nowCalendar = Calendar.getInstance();

        if (isOn) {

            calendar.set(Calendar.HOUR_OF_DAY, ln.hour);
            calendar.set(Calendar.MINUTE, ln.minute);
            calendar.set(Calendar.SECOND, 0);

            if(!ln.week_days){
                calendar = checkforNextDay(nowCalendar, calendar);

                String toastText = "Alarm set on : " + new DateFormatSymbols().getShortWeekdays()[calendar.get(calendar.DAY_OF_WEEK)] + " " + ln.hour + ":" + ln.minute;
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();

                pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, convertToInt(ln.id, 0), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24, pendingIntent);
            }
            else {
                String toastText = "Alarm set on :";
                for(int i=0; i<8; i++){
                    if(ln.days[i]){
                        calendar.setTimeInMillis(nowCalendar.getTimeInMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, ln.hour);
                        calendar.set(Calendar.MINUTE, ln.minute);
                        calendar.set(Calendar.SECOND, 0);
                        long difer = nowCalendar.getTimeInMillis() - calendar.getTimeInMillis();

                        calendar.set(Calendar.DAY_OF_WEEK, i+1);
                        calendar = checkforWeekDay(nowCalendar, calendar, difer);

                        pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, convertToInt(ln.id, i+1), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24, pendingIntent);
                        toastText += " " + new DateFormatSymbols().getShortWeekdays()[calendar.get(calendar.DAY_OF_WEEK)] + ",";
                    }
                }
                toastText += " " + ln.hour + ":" + ln.minute;
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
            }
        }
        else {
            alarmManager.cancel(pendingIntent);
        }
    }

    public Calendar checkforWeekDay(Calendar now, Calendar set, long difer){
        int today = now.get(Calendar.DAY_OF_WEEK);
        int newday = set.get(Calendar.DAY_OF_WEEK);

        if(today == newday){
            if(now.getTimeInMillis() > set.getTimeInMillis()){
                set.setTimeInMillis(now.getTimeInMillis()+1000*60*60*24*7 - (now.getTimeInMillis() - set.getTimeInMillis()));
            }
        }
        if(today < newday){
            set.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
            set.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
            set.set(Calendar.SECOND, 0);
            set.setTimeInMillis(now.getTimeInMillis() + 1000*60*60*24 * (newday - today) - difer);
        }
        if(today > newday){
            set.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
            set.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
            set.set(Calendar.SECOND, 0);
            set.setTimeInMillis(now.getTimeInMillis() + 1000*60*60*24 * (7 - today + newday) - difer);
        }
        return set;
    }

    public Calendar checkforNextDay(Calendar now, Calendar set){
        if(now.getTimeInMillis() > set.getTimeInMillis()){
            set.setTimeInMillis(now.getTimeInMillis()+1000*60*60*24- (now.getTimeInMillis() - set.getTimeInMillis()));
        }
        return set;
    }

    public String convertToDate(long propos){
        return DateFormat.getDateTimeInstance().format(new Date((propos)));
    }

    public void cancelAlarm(){
        Intent myIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        for(int i=0; i<8; i++){
            int intentId = convertToInt(Vars.lineHolder.id, i);
            pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, intentId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }

    public int convertToInt(int obj1, int obj2){
        // 0    for single alarm
        // 1-7  for multydays alarm
        String str = "";
        str += String.valueOf(obj1);
        str += String.valueOf(obj2);
        int result = Integer.parseInt(str);
        return result;
    }

}






