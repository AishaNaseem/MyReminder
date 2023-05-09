package com.skilledevelopers.myownreminder;

public class Items {
    private int _id;
    private String _itemname;
    private String description;

    private int enabled;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
private String repeat;
    public Items(){}

    public Items (String itemname, String itemdescription ,int statusEnabled,int itemYear,int itemMonth,int itemDay,int itemHour,int itemMinute,String itemRepeat){
        this._itemname=itemname;
       // this.store=storeName;
        this.description=itemdescription;
        //this.range =reminderRange;
        this.enabled=statusEnabled;
        this.year=itemYear;
        this.month=itemMonth;
        this.day=itemDay;
        this.hour=itemHour;
        this.minute=itemMinute;
        this .repeat=itemRepeat;

    }
    public String getDescription(){return description;}
    public void setDescription(String description){ this.description=description; }
    public int getEnabled() { return enabled; }
    public void setEnabled(int enabled){ this.enabled=enabled;}
    public String get_itemname() {
        return _itemname;
    }
    public void set_itemname(String _itemname){this._itemname=_itemname;}
    public int get_id() { return _id;}
    public void set__id(int _id) {this._id=_id;}
    public int getYear(){return year;}
    public void setYear(int year1){ this.year=year1;}

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
    public String getRepeat(){
        return repeat;
    }
}
