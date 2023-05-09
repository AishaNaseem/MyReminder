package com.skilledevelopers.myownreminder;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class Edit_Item_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
EditText itemTitle;
EditText itemDesc;

TextView itemDate;
TextView itemTime;
private final String[] repeatRemind={"Once","Daily","Weekly","Monthly"};
Spinner selRepeatRemind;
CheckBox itemEnabled;
MyDBHandler dbHandler;
int selYear,selMonth,selDay,selHour,selMinute;
TextView er;
String value,s,selrepeat;


String [] strng =new String[15];

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=  Calendar.getInstance();
        selYear=year;
        selMonth=month+1  ;
        selDay=dayOfMonth;


        TextView textView= (TextView) findViewById(R.id.textViewDTDisplay);

        textView.setText(selDay+ "/" + selMonth +"/"+selYear);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        selHour=hourOfDay;
        selMinute=minute;
        TextView textView= (TextView)findViewById(R.id.textViewTDisplay);
        textView.setText("Hour: " + hourOfDay + " Minute : " + minute);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_item);
    Intent intent =getIntent();
    value =intent.getStringExtra("key");





    itemTitle=(EditText) findViewById(R.id.itemTitle);
    itemDesc=(EditText)findViewById(R.id.txtDesc);

    itemEnabled=(CheckBox) findViewById(R.id.chkEnabled);
    itemDate= (TextView)findViewById((R.id.textViewDTDisplay));
    itemTime= (TextView)findViewById(R.id.textViewTDisplay);
    er=(TextView)findViewById(R.id.txtviewerror);
    er.setText("");
    er.setFocusable(false);
    dbHandler= new MyDBHandler(this,null,null,1);
        if(value!=null){

            strng=dbHandler.viewItem(value);
        }

    itemTitle.setFocusable(true);
        selRepeatRemind = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,repeatRemind);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if( value==null){
            selRepeatRemind.setAdapter(adapter);
            selRepeatRemind.setSelection(0);}

    else{
            if(strng[9].equals("Expired")){
                selRepeatRemind.setAdapter(adapter);
                selRepeatRemind.setSelection(0);
            }
            if(strng[9].equals("Once"))
            {
                selRepeatRemind.setAdapter(adapter);
                selRepeatRemind.setSelection(0);
            }

            if(strng[9].equals("Daily")){
                selRepeatRemind.setAdapter(adapter);
            selRepeatRemind.setSelection(1);}
            if(strng[9].equals("Weekly")){
                selRepeatRemind.setAdapter(adapter);
            selRepeatRemind.setSelection(2);}
            if(strng[9].equals("Monthly")){
                selRepeatRemind.setAdapter(adapter);
            selRepeatRemind.setSelection(3);}
            /*if(strng[9].equals("Never")){
                selRepeatRemind.setAdapter(adapter);
            selRepeatRemind.setSelection(4);}*/

        itemTitle.setText(strng[1]);
        itemTitle.setFocusable(false);
        itemDesc.setText(strng[2]);

         s=strng[6]+ "/" + strng[5] +"/" + strng[4];
        itemDate.setText(s);
        selYear=Integer.valueOf(strng[4]);
        selMonth=Integer.valueOf(strng[5]);
        selDay=Integer.valueOf(strng[6]);
        s="Hour: " + strng[7]+ " Minute : " + strng[8];
        selHour=Integer.valueOf(strng[7]);
        selMinute=Integer.valueOf(strng[8]);
        itemTime.setText(s);
        if (Integer.valueOf(strng[3])==1)
        {itemEnabled.setChecked(true);}
        else {itemEnabled.setChecked(false);}




    }
        TextView textViewDate = (TextView) findViewById(R.id.textViewDT);
        textViewDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogFragment datePicker;
                if (value!=null) {
                    datePicker = new DatePickerFragment(selYear, selMonth-1, selDay);
                }
                else {
                    Calendar c= Calendar.getInstance();

                    datePicker=new DatePickerFragment(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                }

                datePicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });
TextView textvDatelbl=(TextView) findViewById(R.id.textViewDTDisplay);
        textvDatelbl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogFragment datePicker;
                if (value!=null) {
                    datePicker = new DatePickerFragment(selYear, selMonth-1, selDay);
                }
                else {
                    Calendar c= Calendar.getInstance();

                    datePicker=new DatePickerFragment(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                }

                datePicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });

        TextView textViewTime=(TextView)findViewById(R.id.textViewTime);

        textViewTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment timePicker;
                if (value!=null){
                    timePicker=new TimePickerFragment(selHour,selMinute);
                }
                else{
                    Calendar c= Calendar.getInstance();
                    timePicker=new TimePickerFragment(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
                }

                timePicker.show(getSupportFragmentManager(),"Time Picker");
            }
        });
        TextView textVTimeLbl=(TextView)findViewById(R.id.textViewTDisplay);

        textVTimeLbl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment timePicker;
                if (value!=null){
                    timePicker=new TimePickerFragment(selHour,selMinute);
                }
                else{
                    Calendar c= Calendar.getInstance();
                    timePicker=new TimePickerFragment(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
                }

                timePicker.show(getSupportFragmentManager(),"Time Picker");
            }
        });

        selRepeatRemind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // er.setText(repeatRemind[position]);
                selrepeat = repeatRemind[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // er.setText(repeatRemind[0]);
                selrepeat = repeatRemind[0];
            }
        });


}
public void SaveButtonClicked(View view){
    boolean errorcheck=false;
     int en=0;
    int code;
    String str =new String();
    String itm= new String();
    String desc = new String();

    er.setText("");

    if (itemTitle.getText().length()>0){
        itm=itemTitle.getText().toString();

    }
    else {
        errorcheck=true;
    }

    if((value==null) && (dbHandler.checkItemName(itm)==false)){
        errorcheck=true;
        er.setText("This Title is already in the reminder list");
        itemTitle.setFocusable(true);
    }
    if (itemDesc.getText().length()>0) {
        desc=itemDesc.getText().toString();
    } else {errorcheck=true;}
//
    if (itemEnabled.isChecked()){ en=1;}
    //... checking for time and date
    Calendar c=  Calendar.getInstance();
    c.set(selYear,selMonth-1,selDay,selHour,selMinute);

    if (c.getTimeInMillis()<=System.currentTimeMillis()){
        er.setText("Please select an upcoming Date/Time");
    }
    else {


        if (errorcheck == false) {
            Items item = new Items(itm, desc, en, selYear, selMonth, selDay, selHour, selMinute,selrepeat);
            if (value != null) {
                item.set__id(Integer.valueOf(strng[0]));
                dbHandler.updateItem(item);


                Toast.makeText(this, " Item updated Successfully ", Toast.LENGTH_SHORT).show();


            } else {
                dbHandler.addItem(item);
                Toast.makeText(this, "Item saved Successfully", Toast.LENGTH_SHORT).show();
                strng=dbHandler.viewItem(itm);
            }




            // set reminder in milliseconds

           code=Integer.parseInt(strng[0]);

            if (en == 1)
            {
                Intent intent = new Intent(this, MyBroadcastReceiver.class);
                intent.putExtra("title", itm);
                intent.putExtra("text", desc);
                intent.putExtra("key", strng[0]);
                intent.putExtra("repeatation", selrepeat);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), code, intent, FLAG_IMMUTABLE);
               // PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), code, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                //cancel previous alarms
                if (pendingIntent != null && alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                }
                assert alarmManager != null;
                if (selrepeat == "Once") {


                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                } else if (selrepeat == "Daily") {
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


                } else if (selrepeat == "Weekly") {
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

                } else if (selrepeat == "Monthly") {
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY * c.getActualMaximum(Calendar.DAY_OF_MONTH), pendingIntent);

                }
            }
            setResult(RESULT_OK, null);
            finish();
        } else if (er.getText()=="") {
            er.setText("Please input required fields(*)");
        }
    }
}

public void CancelButtonClicked (View view){finish();}


public long getDuration(){
        // get todays date
        Calendar cal = Calendar.getInstance();
        // get current month
        int currentMonth = cal.get(Calendar.MONTH);

        // move month ahead
        currentMonth++;
        // check if has not exceeded threshold of december

        if(currentMonth > Calendar.DECEMBER){
            // alright, reset month to jan and forward year by 1 e.g fro 2013 to 2014
            currentMonth = Calendar.JANUARY;
            // Move year ahead as well
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
        }

        // reset calendar to next month
        cal.set(Calendar.MONTH, currentMonth);
        // get the maximum possible days in this month
        int maximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // set the calendar to maximum day (e.g in case of fEB 28th, or leap 29th)
        cal.set(Calendar.DAY_OF_MONTH, maximumDay);
        long thenTime = cal.getTimeInMillis(); // this is time one month ahead



        return (thenTime); // this is what you set as trigger point time i.e one month after

    }

}