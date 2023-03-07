package com.btproject.barberise;


import static com.btproject.barberise.database.UserDAO.getUser;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.btproject.barberise.navigation.MenuActivity;
import com.btproject.barberise.utils.CalendarUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Set;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Objects.requireNonNull(getSupportActionBar()).hide();   //hide action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideSystemUI();
        splashCountdown();

    }

    private void splashCountdown()
    {
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {    //What happens each second
                //no need to code
            }
            @Override
            public void onFinish() {    //What happens after time expires
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null){
                    Intent intent = new Intent(SplashActivity.this,MenuActivity.class);
                    startActivity(intent);
                }else{
                    startActivity(new Intent(SplashActivity.this, VerifyPhoneNoActivity.class));
                    finish();   //removes current activity from backstack
                }
            }
        }.start();  //starts countdown
    }


    private void hideSystemUI() {
        View decorView = this.getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(newUiOptions);
    }

}