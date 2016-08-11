package com.dvr.dvralarm.View;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.dvr.dvralarm.AlarmActivity;
import com.dvr.dvralarm.Helpers.Vars;
import com.dvr.dvralarm.Holders.LineHolder;
import com.dvr.dvralarm.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class LineAdapter extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private ArrayList<LineHolder> list;
    private AlarmActivity alarmClass;

    public LineAdapter(Activity activity, ArrayList list, AlarmActivity alarmClass) {
        assert activity != null;

        this.alarmClass = alarmClass;
        this.list = list;
        this.activity = activity;

    }

    public void checkBoxes(int position){
        list.get(position).day1 = true;
        list.get(position).day2 = true;
        list.get(position).day3 = true;
        list.get(position).day4 = true;
        list.get(position).day5 = true;
        list.get(position).day6 = true;
        list.get(position).day7 = true;
        list.get(position).week_days = false;
    }



    @Override public int getCount() {
        if(null==list)
            return 0;
        else
            return list.size();
    }

    @Override public LineHolder getItem(int position) {
        if(null==list) return null;
        else
            return list.get(position);
    }

    @Override public long getItemId(int position) {
        LineHolder line = getItem(position);

        return line.id;
    }

    @Override public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        HashMap<String, CheckBox> days = new HashMap<String, CheckBox>();

        if (convertView == null) {
            LayoutInflater ll = LayoutInflater.from(activity);
            convertView = ll.inflate(R.layout.compact_line, null);
            holder = new ViewHolder();

            holder.selected = 0;
            holder.power = (Switch) convertView.findViewById(R.id.on_off);
            holder.grid = (GridLayout) convertView.findViewById(R.id.grid);
            holder.week = (CheckBox) convertView.findViewById(R.id.week);
            holder.day1 = (CheckBox) convertView.findViewById(R.id.day1);
            holder.day2 = (CheckBox) convertView.findViewById(R.id.day2);
            holder.day3 = (CheckBox) convertView.findViewById(R.id.day3);
            holder.day4 = (CheckBox) convertView.findViewById(R.id.day4);
            holder.day5 = (CheckBox) convertView.findViewById(R.id.day5);
            holder.day6 = (CheckBox) convertView.findViewById(R.id.day6);
            holder.day7 = (CheckBox) convertView.findViewById(R.id.day7);

            days.put("day1", holder.day1);
            days.put("day2", holder.day2);
            days.put("day3", holder.day3);
            days.put("day4", holder.day4);
            days.put("day5", holder.day5);
            days.put("day6", holder.day6);
            days.put("day7", holder.day7);

            holder.but = (Button) convertView.findViewById(R.id.delbut);
            //holder.but.setTag(position, position);


            holder.timeText = (TextView) convertView.findViewById(R.id.time_txt);
            holder.timeText.setTag(position);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //// //// //// ////
        // VISUALIZATION
        holder.power.setChecked(list.get(position).is_active);

        if(list.get(position).day1) holder.selected++;
        if(list.get(position).day2) holder.selected++;
        if(list.get(position).day3) holder.selected++;
        if(list.get(position).day4) holder.selected++;
        if(list.get(position).day5) holder.selected++;
        if(list.get(position).day6) holder.selected++;
        if(list.get(position).day7) holder.selected++;

        holder.week.setChecked(list.get(position).week_days);
        if(list.get(position).week_days) {
            holder.grid.setVisibility(View.VISIBLE);
            holder.day1.setChecked(list.get(position).day1);
            holder.day2.setChecked(list.get(position).day2);
            holder.day3.setChecked(list.get(position).day3);
            holder.day4.setChecked(list.get(position).day4);
            holder.day5.setChecked(list.get(position).day5);
            holder.day6.setChecked(list.get(position).day6);
            holder.day7.setChecked(list.get(position).day7);
        }
        else{
            holder.grid.setVisibility(View.GONE);
        }



        holder.calendar = Calendar.getInstance();
        holder.calendar.set(Calendar.MINUTE,list.get(position).minute);
        holder.calendar.set(Calendar.HOUR_OF_DAY,list.get(position).hour);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        //holder.small.setVisibility(View.INVISIBLE);
        //holder.text.setText(""+list.get(position).id);
        holder.timeText.setText(""+sdf.format(holder.calendar.getTime()));

        //// //// //// ////
        // CONTROL
        holder.but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vars.lineHolder = list.get(position);
                alarmClass.cancelAlarm();
                alarmClass.deleteLine(list.get(position).id);
            }
        });

        holder.power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(position).is_active = isChecked;
                Vars.lineHolder = list.get(position);
                update(position);
                alarmClass.setAlarm(list.get(position), isChecked);

            }
        });

        holder.timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmClass.dialogTime(list.get(position));
            }
        });

        // WEEK SWITCHER
        holder.week.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                     list.get(position).week_days = isChecked;

                     if(list.get(position).week_days) {
                         holder.grid.setVisibility(View.VISIBLE);
                         holder.day1.setChecked(list.get(position).day1);
                         holder.day2.setChecked(list.get(position).day2);
                         holder.day3.setChecked(list.get(position).day3);
                         holder.day4.setChecked(list.get(position).day4);
                         holder.day5.setChecked(list.get(position).day5);
                         holder.day6.setChecked(list.get(position).day6);
                         holder.day7.setChecked(list.get(position).day7);
                     }
                     else{
                         holder.grid.setVisibility(View.GONE);
                     }
                     holder.selected = 0;
                     if(list.get(position).day1) holder.selected++;
                     if(list.get(position).day2) holder.selected++;
                     if(list.get(position).day3) holder.selected++;
                     if(list.get(position).day4) holder.selected++;
                     if(list.get(position).day5) holder.selected++;
                     if(list.get(position).day6) holder.selected++;
                     if(list.get(position).day7) holder.selected++;


                     list.get(position).is_active = false;
                     holder.power.setChecked(false);
                     update(position);
                 }
             }
        );
        // DAYS SWITCHERS
        holder.day1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                if (isChecked) {
                                                                    holder.selected++;
                                                                }
                                                                else {
                                                                    holder.selected--;
                                                                }
                                                                list.get(position).day1 = isChecked;
                                                                list.get(position).days[0] = isChecked;
                                                                list.get(position).is_active = false;
                                                                holder.power.setChecked(false);
                                                                update(position);
                                                                if(holder.selected == 0){
                                                                    checkBoxes(position);
                                                                    holder.grid.setVisibility(View.GONE);
                                                                    holder.week.setChecked(false);
                                                                }
                                                            }
                                                        }
        );
        holder.day2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                if (isChecked) {
                                                                    holder.selected++;
                                                                }
                                                                else {
                                                                    holder.selected--;
                                                                }
                                                                list.get(position).day2 = isChecked;
                                                                list.get(position).days[1] = isChecked;
                                                                list.get(position).is_active = false;
                                                                holder.power.setChecked(false);
                                                                update(position);
                                                                if(holder.selected == 0){
                                                                    checkBoxes(position);
                                                                    holder.grid.setVisibility(View.GONE);
                                                                    holder.week.setChecked(false);
                                                                }
                                                            }
                                                        }
        );
        holder.day3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                if (isChecked) {
                                                                    holder.selected++;
                                                                }
                                                                else {
                                                                    holder.selected--;
                                                                }
                                                                list.get(position).day3 = isChecked;
                                                                list.get(position).days[2] = isChecked;
                                                                list.get(position).is_active = false;
                                                                holder.power.setChecked(false);
                                                                update(position);
                                                                if(holder.selected == 0){
                                                                    checkBoxes(position);
                                                                    holder.grid.setVisibility(View.GONE);
                                                                    holder.week.setChecked(false);
                                                                };
                                                            }
                                                        }
        );
        holder.day4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                if (isChecked) {
                                                                    holder.selected++;
                                                                }
                                                                else {
                                                                    holder.selected--;
                                                                }
                                                                list.get(position).day4 = isChecked;
                                                                list.get(position).days[3] = isChecked;
                                                                list.get(position).is_active = false;
                                                                holder.power.setChecked(false);
                                                                update(position);
                                                                if(holder.selected == 0){
                                                                    checkBoxes(position);
                                                                    holder.grid.setVisibility(View.GONE);
                                                                    holder.week.setChecked(false);
                                                                }
                                                            }
                                                        }
        );
        holder.day5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                if (isChecked) {
                                                                    holder.selected++;
                                                                }
                                                                else {
                                                                    holder.selected--;
                                                                }
                                                                list.get(position).day5 = isChecked;
                                                                list.get(position).days[4] = isChecked;
                                                                list.get(position).is_active = false;
                                                                holder.power.setChecked(false);
                                                                update(position);
                                                                if(holder.selected == 0){
                                                                    checkBoxes(position);
                                                                    holder.grid.setVisibility(View.GONE);
                                                                    holder.week.setChecked(false);
                                                                }
                                                            }
                                                        }
        );
        holder.day6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                if (isChecked) {
                                                                    holder.selected++;
                                                                }
                                                                else {
                                                                    holder.selected--;
                                                                }
                                                                list.get(position).day6 = isChecked;
                                                                list.get(position).days[5] = isChecked;
                                                                list.get(position).is_active = false;
                                                                holder.power.setChecked(false);
                                                                update(position);
                                                                if(holder.selected == 0){
                                                                    checkBoxes(position);
                                                                    holder.grid.setVisibility(View.GONE);
                                                                    holder.week.setChecked(false);
                                                                }
                                                            }
                                                        }
        );
        holder.day7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                if (isChecked) {
                                                                    holder.selected++;
                                                                }
                                                                else {
                                                                    holder.selected--;
                                                                }
                                                                list.get(position).day7 = isChecked;
                                                                list.get(position).days[6] = isChecked;
                                                                list.get(position).is_active = false;
                                                                holder.power.setChecked(false);
                                                                update(position);
                                                                if(holder.selected == 0){
                                                                    checkBoxes(position);
                                                                    holder.grid.setVisibility(View.GONE);
                                                                    holder.week.setChecked(false);
                                                                }
                                                            }
                                                        }
        );


        return convertView;
    }

    public void update(int pos){

        Vars.lineHolder = list.get(pos);
        alarmClass.cancelAlarm();
        alarmClass.updateLine();
    }


    class ViewHolder{
        Switch power;
        TextView timeText;
        Button but;
        Calendar calendar;
        GridLayout grid;
        CheckBox week;
        CheckBox day1;
        CheckBox day2;
        CheckBox day3;
        CheckBox day4;
        CheckBox day5;
        CheckBox day6;
        CheckBox day7;
        int selected;

    }

}

