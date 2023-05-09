package com.skilledevelopers.myownreminder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {
    private static int SPLASH_SCREEN=2000;
    TextView slogan;
    ImageView logo;
Animation topAnim,bottomAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
bottomAnim=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
logo=findViewById(R.id.logoPic);
slogan=findViewById(R.id.tvName);
logo.setAnimation(topAnim);
slogan.setAnimation(bottomAnim);
new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        Intent intent= new Intent(Main2Activity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
},SPLASH_SCREEN);
    }

}
