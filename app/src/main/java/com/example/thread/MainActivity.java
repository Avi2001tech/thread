package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView AppName;
    LottieAnimationView lottie;
   // FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AppName = findViewById(R.id.AppName);
        lottie = findViewById(R.id.lottie);
        //mAuth = FirebaseAuth.getInstance();

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.text_animation);
        AppName.setAnimation(anim);


        lottie.animate().setDuration(2700).setStartDelay(0);
        lottie.animate().getInterpolator();
        lottie.playAnimation();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
//                if(mAuth.getCurrentUser()!=null){
//                    Intent i = new Intent(splashScreen.this,MainActivity.class);
//                    startActivity(i);
//                    finish();
//                }
//                else{
//                    Intent i = new Intent(splashScreen.this,login_page.class);
//                    startActivity(i);
//                    finish();
//                }
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(i);
                   finish();

            }
        },3500);
    }
}