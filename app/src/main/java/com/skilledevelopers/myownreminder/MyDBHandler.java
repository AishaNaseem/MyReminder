package com.skilledevelopers.myownreminder;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=8;
    private static final String DATABASE_NAME="items.db";
    private static final String TABLE_ITEMS ="items";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_ITEMNAME="_itemname";
    private static final String COLUMN_ITEMDESC="itemDescription";

    private static final String COLUMN_ITEMENABLED="itemEnabled";
    private static final String COLUMN_ITEMYEAR="itemYear";
    private static final String COLUMN_ITEMMONTH="itemMonth";
    private static final String COLUMN_ITEMDAY="itemDay";
    private static final String COLUMN_ITEMHOUR="itemHour";
    private static final String COLUMN_ITEMMINUTE="itemMinute";
private static final String COLUMN_REPEAT="itemRepeat";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,DATABASE_NAME, factory, DATABASE_VERSION);
    }
    @Override public void onCreate(SQLiteDatabase db){
        String query="CREATE TABLE " + TABLE_ITEMS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ITEMNAME + " TEXT, " + COLUMN_ITEMDESC + " TEXT, " +
                COLUMN_ITEMENABLED + " INTEGER, " + COLUMN_ITEMYEAR + " INTEGER, "+ COLUMN_ITEMMONTH + " INTEGER, " +
                COLUMN_ITEMDAY + " INTEGER, " + COLUMN_ITEMHOUR + " INTEGER, " + COLUMN_ITEMMINUTE + " INTEGER, " + COLUMN_REPEAT + " TEXT " + ");" ;
        db.execSQL(query);


    }

    @ Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }
    @SuppressLint("Range")
    public String[] viewItem(String itemName){
        int i=0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor =null;
        String[] str = new String [15];

            cursor = db.rawQuery( "SELECT * FROM "+ TABLE_ITEMS + " WHERE " + COLUMN_ITEMNAME + "=\"" + itemName + "\";" ,null);
            cursor.moveToFirst();
            if (cursor.getCount()>0){
                do{
                    str[i]=cursor.getString(cursor.getColumnIndex(cursor.getColumnName(i)));
                i=i+1;

                }while(i<=9);
            }
        cursor.close();
        db.close();
            return str;



    }

    public void addItem(Items item){
       ContentValues values= new ContentValues();
       values.put(COLUMN_ITEMNAME,item.get_itemname());
       values.put(COLUMN_ITEMDESC,item.getDescription());

       values.put(COLUMN_ITEMENABLED,item.getEnabled());
       values.put(COLUMN_ITEMYEAR,item.getYear());
       values.put(COLUMN_ITEMMONTH,item.getMonth());
       values.put(COLUMN_ITEMDAY,item.getDay());
       values.put(COLUMN_ITEMHOUR,item.getHour());
       values.put(COLUMN_ITEMMINUTE,item.getMinute());
       values.put(COLUMN_REPEAT,item.getRepeat());
       SQLiteDatabase db =getWritableDatabase();
       db.insert(TABLE_ITEMS,null,values);
       db.close();

    }

    //delete an iteem from database
    public void deleteitem(String itemName){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEMNAME + "=\"" + itemName + "\";");
        db.close();
    }

    public void updateItem(Items item){
        SQLiteDatabase db =getWritableDatabase();
    String query ="UPDATE " + TABLE_ITEMS + " SET " + COLUMN_ITEMDESC + "=\"" + item.getDescription() + "\"," +  COLUMN_ITEMENABLED + "=" + item.getEnabled()+ ", " +
            COLUMN_ITEMYEAR + "=" + item.getYear() + ", " +  COLUMN_ITEMMONTH + "=" + item.getMonth() + ", " +  COLUMN_ITEMDAY + "=" + item.getDay()
             + ", " +  COLUMN_ITEMHOUR + "=" + item.getHour() + ", " +  COLUMN_ITEMMINUTE + "=" + item.getMinute() + "," +  COLUMN_REPEAT + "=\"" + item.getRepeat() + "\"" + " WHERE " +  COLUMN_ID + "= " + item.get_id() + " ;";
    db.execSQL(query);
    db.close();
    }


    public class displayData{
        public  String  dbString;
        public int enabled;
        public displayData(){}
        public displayData(String str,int i){
            dbString=str;
            enabled=i;
        }
    }

public ArrayList<displayData> databaseToString(String header){
        int i=0;
        ArrayList<displayData> displaydataList = new ArrayList<>();
       // displayData dp =new displayData();
        SQLiteDatabase db = getWritableDatabase();
        String query= "SELECT * FROM " + TABLE_ITEMS + " WHERE itemRepeat =\"" + header + "\";" ;
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();

        if (( c!= null) && (c.getCount()>0)){
            do{

                //dp.dbString=c.getString(1);
                //dp.enabled=c.getInt(3);
                displaydataList.add(new displayData(c.getString(1),c.getInt(3)));
           i=i+1;
            }while(c.moveToNext());
        }
        c.close();
        db.close();
        return displaydataList;
}

public void update_expired() {
int id ;
    String query ,mydatetime;

    Calendar clndr= Calendar.getInstance();



    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String getCurrentDateTime = sdf.format(clndr.getTime());

    SQLiteDatabase db = getWritableDatabase();
    query="SELECT * FROM \"" + TABLE_ITEMS + "\" WHERE " + COLUMN_REPEAT + "=\"Once\" ;";
    Cursor cur= db.rawQuery(query,null);
    cur.moveToFirst();

    if (( cur!= null) && (cur.getCount()>0)){
        do{try {
            mydatetime=cur.getString(4)+ "-" +cur.getString(5) +"-" + cur.getString(6)+" " + cur.getString(7) + ":" + cur.getString(8)+ ":00" ;

                Date d1 = sdf.parse(getCurrentDateTime);

            Date d2=sdf.parse(mydatetime);
        if(d1.compareTo(d2) > 0){
            query = "UPDATE " + TABLE_ITEMS + " SET " + COLUMN_REPEAT + "=\"Expired\"" + " WHERE " + COLUMN_ITEMNAME + "= \"" + cur.getString(1) + "\" ;";
            db.execSQL(query);
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        }while(cur.moveToNext());
    }
    cur.close();
    db.close();


}
public boolean checkItemName(String st){
    SQLiteDatabase db = getWritableDatabase();
    String query= "SELECT * FROM " + TABLE_ITEMS + " WHERE _itemname =\"" + st + "\";" ;
    Cursor c= db.rawQuery(query,null);


    if (( c!= null) && (c.getCount()>0)){
        c.close();
        db.close();
        return false;
    }

    else{
        c.close();
        db.close();
        return true;
    }

}

}