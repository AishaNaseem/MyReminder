package com.skilledevelopers.myownreminder;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.app.PendingIntent.getActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;




public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String contTitle,contText,repeat;
        int idkey;
        contTitle =intent.getStringExtra("title");
        contText=intent.getStringExtra("text");
        idkey=Integer.parseInt(intent.getStringExtra("key"));
        repeat=intent.getStringExtra("repeatation");
        Intent tapResultIntent=new Intent(context,MainActivity.class);
        tapResultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent=getActivity(context,0,tapResultIntent,FLAG_IMMUTABLE);

        if (repeat=="Monthly")
        {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"NotificationReminder")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(contTitle)
                .setContentText(contText)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(idkey,builder.build());

            Long futureTimeDifference = intent.getLongExtra("futureTimeDifference", 0); // Receive the time difference in milliseconds from currenttime in milliseconds and the future set date milliseconds
            futureTimeDifference = futureTimeDifference + System.currentTimeMillis();// get the next schedule date time inmilliseconds
            String repeatType = intent.getStringExtra("getRepeatType");// Receive the repeat type

            Date todaysDate = new Date();// initialize a new date object
            Calendar getCurrentDate = Calendar.getInstance();// Initialize a new Calendar object
            getCurrentDate.setTime(todaysDate); //Set the calendar to todays date
            int currentMonth = getCurrentDate.get(Calendar.MONTH); // Assign the current month in integer

            if (currentMonth == Calendar.JANUARY || currentMonth == Calendar.MARCH || currentMonth == Calendar.MAY || currentMonth == Calendar.JULY || currentMonth == Calendar.AUGUST || currentMonth == Calendar.OCTOBER || currentMonth == Calendar.DECEMBER)
                {
                futureTimeDifference = System.currentTimeMillis() + (AlarmManager.INTERVAL_DAY * 31);
                }
            if (currentMonth == Calendar.APRIL || currentMonth == Calendar.JUNE || currentMonth == Calendar.SEPTEMBER || currentMonth == Calendar.NOVEMBER)
                {
                futureTimeDifference = System.currentTimeMillis() + (AlarmManager.INTERVAL_DAY * 30);
                }

            if (currentMonth == Calendar.FEBRUARY)
                {//for february month)
                GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
                 if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                    {//for leap year february month
                    futureTimeDifference = System.currentTimeMillis() + (AlarmManager.INTERVAL_DAY * 29);
                    }
                 else { //for non leap year february month
                    futureTimeDifference = System.currentTimeMillis() + (AlarmManager.INTERVAL_DAY * 28);
                    }
                }

            PendingIntent displayIntent = PendingIntent.getBroadcast(context, idkey, intent,  FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (displayIntent != null && alarmManager != null)
                {
                alarmManager.cancel(displayIntent);
                }
            assert alarmManager != null;
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureTimeDifference, displayIntent);
            //Toast.makeText(context, "Notification Set Monthly", Toast.LENGTH_SHORT).show();
        }
    else{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"NotificationReminder")
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(contTitle)
                    .setContentText(contText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(idkey,builder.build());

        }
        /*if (repeat=="Monthly")
        {


            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), idkey, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (pendingIntent != null && alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
            long duration=getDuration();
            alarmManager.set(AlarmManager.RTC_WAKEUP, duration, pendingIntent);
        }*/

    }

}
