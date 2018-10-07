package com.autodidact.developers.festivalwish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView image = findViewById(R.id.Splash);
//        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        Thread timerThread1 = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        image.startAnimation(animation2);
//        image.startAnimation(animation1);
        timerThread1.start();
//        image.startAnimation(animation1);
//        timerThread1.start();

    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
