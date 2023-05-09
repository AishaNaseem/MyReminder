package com.skilledevelopers.myownreminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import androidx.appcompat.app.AppCompatActivity;


import android.view.LayoutInflater;
import android.view.View;



import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemClickListener;


import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements OnItemClickListener {
//String dbString;
ListView lstView;
MyDBHandler dbHandler;
String selectedItem;
int selectedPosition;
TaskAdapter adapter;
//ArrayList<String> listItems;
ArrayList<ReminderItem> listItems;

    ImageView editimagebutton,deleteimagebutton;

    private AdView mAdView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstView=(ListView)findViewById(R.id.listView);
        dbHandler=new MyDBHandler( this, null,  null,  1 );
 editimagebutton=(ImageView)findViewById(R.id.edtImgBtn);
editimagebutton.setVisibility(View.INVISIBLE);
 deleteimagebutton=(ImageView)findViewById(R.id.deleteImgBtn);
deleteimagebutton.setVisibility(View.INVISIBLE);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        listItems = new ArrayList<ReminderItem>();

        try {
            fill_List();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // set adapter
          adapter = new TaskAdapter(this, listItems);
        lstView.setAdapter(adapter);
        lstView.setOnItemClickListener(this);
        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int position, long id) {
                TextView temp= (TextView)view.findViewById(R.id.tvItemTitle);
                //selectedItem= lstView.getChildAt(position);
                if (temp!=null) {
                    selectedItem = String.valueOf(temp.getText());
                    lstView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
if (selectedPosition>0)
    lstView.getChildAt(selectedPosition).setBackgroundColor(getResources().getColor(R.color.white));
                    selectedPosition=position;

                    deleteimagebutton.setVisibility(View.VISIBLE);
                    editimagebutton.setVisibility(View.VISIBLE);


                }


                return true;
            }
        });

        createNotificationChannel();


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NotificationReminder", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
                   //   NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

//

    public void editItemMain(View view){

        if (selectedItem!= null) {

            Intent intent = new Intent(this, Edit_Item_Activity.class);
            intent.putExtra("key", selectedItem);
            startActivityForResult(intent,1);

            lstView.getChildAt(selectedPosition).setBackgroundColor(getResources().getColor(R.color.white));
            deleteimagebutton.setVisibility(View.INVISIBLE);
            editimagebutton.setVisibility(View.INVISIBLE);
        }
        else{
            Toast.makeText(this,"First select an item! ", Toast.LENGTH_SHORT).show();

        }
        //displayList();

    }
    public void deleteItemMain(View view){
        if (selectedItem != null) {
            dbHandler.deleteitem(selectedItem);
            deleteimagebutton.setVisibility(View.INVISIBLE);
            editimagebutton.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "" + selectedItem + "Deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "First select an item !", Toast.LENGTH_SHORT).show();

        }
       /* Intent refresh= new Intent (this, MainActivity.class);
        startActivity(refresh);
        this.finish();*/
        try {
            displayList();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void addButtonClicked(View view){
        Intent intent = new Intent (this, Edit_Item_Activity.class);
        startActivityForResult(intent,1);

    }
@Override protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            Intent refresh= new Intent (this, MainActivity.class);
            startActivity(refresh);
            this.finish();

            /*try {
                displayList();
            } catch (ParseException e) {
                e.printStackTrace();
            }*/

        }
    }

    @Override public void onItemClick(AdapterView<?> parent,View view, int position,long id){
        TextView temp= (TextView)view.findViewById(R.id.tvItemTitle);
      //selectedItem= lstView.getChildAt(position);
        if (temp!=null) {
            selectedItem = String.valueOf(temp.getText());

            if (selectedPosition>0){
                lstView.getChildAt(selectedPosition).setBackgroundColor(getResources().getColor(R.color.white));
            selectedPosition=-1;}

            deleteimagebutton.setVisibility(View.INVISIBLE);
            editimagebutton.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(this, Edit_Item_Activity.class);

                intent.putExtra("key", selectedItem);
                //startActivity(intent);
                startActivityForResult(intent, 1);

        }
    }



    public  void displayList() throws ParseException {

        listItems.clear();
        fill_List();
        adapter.notifyDataSetChanged();



    }

    public void fill_List() throws ParseException {
        int k;
        ArrayList<MyDBHandler.displayData> dataString = new ArrayList<>();
dbHandler.update_expired();
        dataString= dbHandler.databaseToString("Once");
        if(dataString!=null && dataString.size()!=0){
            listItems.add(new SectionItem("Once"));
            for ( k=0;k<dataString.size();k++){

                listItems.add(new EntryItem(dataString.get(k).dbString,dataString.get(k).enabled));
            }
        }
        dataString=null;
        dataString= dbHandler.databaseToString("Daily");
        if(dataString!=null && dataString.size()!=0){
            listItems.add(new SectionItem("Daily"));
            for ( k=0;k<dataString.size();k++){

                listItems.add(new EntryItem(dataString.get(k).dbString,dataString.get(k).enabled));
            }
        }
        dataString=null;
        dataString= dbHandler.databaseToString("Weekly");
        if(dataString!=null && dataString.size()!=0){
            listItems.add(new SectionItem("Weekly"));
            for ( k=0;k<dataString.size();k++){

                listItems.add(new EntryItem(dataString.get(k).dbString,dataString.get(k).enabled));
            }
        }
        dataString= dbHandler.databaseToString("Monthly");
        if(dataString!=null && dataString.size()!=0){
            listItems.add(new SectionItem("Monthly"));
            for ( k=0;k<dataString.size();k++){

                listItems.add(new EntryItem(dataString.get(k).dbString,dataString.get(k).enabled));
            }
        }

        dataString= dbHandler.databaseToString("Expired");
        if(dataString!=null && dataString.size()!=0){
            listItems.add(new SectionItem("Expired"));
            for ( k=0;k<dataString.size();k++){

                listItems.add(new EntryItem(dataString.get(k).dbString,dataString.get(k).enabled));

            }
        }

    }




    /**
     * row item
     */
    public interface ReminderItem {
        public boolean isSection();
        public String getTitle();

        public int  getActive();
    }
    /**
     * Section Item
     */
    public class SectionItem implements ReminderItem {
        private final String title;

        public SectionItem(String title) {
            this.title = title;
        }
@Override
        public String getTitle() {
            return title;
        }

        @Override
        public boolean isSection() {
            return true;
        }

        @Override
        public int getActive() {
            return 0;
        }
    }

    /**
     * Entry Item
     */
    public class EntryItem implements ReminderItem {
        public final String title;
        public int active;

        public EntryItem(String title,int active) {
            this.title = title;
            this.active=active;
        }
@Override
        public String getTitle() {
            return title;
        }
@Override
        public int getActive() {
            return active;
        }

        @Override
        public boolean isSection() {
            return false;
        }
    }

    /**
     * Adapter
     */
    public class TaskAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<ReminderItem> item;
        private ArrayList<ReminderItem> originalItem;

        public TaskAdapter() {
            super();
        }

        public TaskAdapter(Context context, ArrayList<ReminderItem> item) {
            this.context = context;
            this.item = item;
            //this.originalItem = item;
        }

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public Object getItem(int position) {
            return item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (item.get(position).isSection()) {
                // if section header
                convertView = inflater.inflate(R.layout.layout_section, parent, false);
                TextView tvSectionTitle = (TextView) convertView.findViewById(R.id.tvSectionTitle);
                tvSectionTitle.setText(((SectionItem) item.get(position)).getTitle());
            } else {
                // if item
                convertView = inflater.inflate(R.layout.layout_item, parent, false);
                TextView tvItemTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);

                if( item.get(position).getActive()==0){
                    tvItemTitle.setTextColor(getResources().getColor(R.color.divider));}
                tvItemTitle.setText(((EntryItem) item.get(position)).getTitle());

            }

            return convertView;
        }
    }
}
