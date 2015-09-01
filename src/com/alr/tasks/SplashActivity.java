package com.alr.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            Logger.d(Consts.LOG, e.getMessage());
        }
        setContentView(R.layout.splash);
        TextView v = (TextView)findViewById(R.id.versionText);
        v.setText(getString(R.string.version)+" "+versionName);
        startAnimating();
    }
    // Helper method to start the animation on the splash screen
    private void startAnimating() {
        // Fade in top title
        ImageView logo1 = (ImageView) findViewById(R.id.myLogo);
        Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.splash);
        logo1.startAnimation(fade1);
        // Transition to Main Menu when bottom title finishes animating
        fade1.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationEnd(Animation animation) {
                // The animation has ended, transition to the Main Menu screen
                //Phone phone = new Phone(getApplicationContext());
                //phone.setNotifyVolume(2);
                //int n = phone.getNotifyVolume();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Stop the animation
        ImageView logo1 = (ImageView) findViewById(R.id.myLogo);
        logo1.clearAnimation();
    }
    @Override
    protected void onResume() {
        super.onResume();
        startAnimating();
    }
}
