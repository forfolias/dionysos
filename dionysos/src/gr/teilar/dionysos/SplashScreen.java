package gr.teilar.dionysos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);

		ImageView logo = (ImageView) findViewById(R.id.splashScreen);
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.appear);
		logo.startAnimation(anim);

        new Handler().postDelayed(new Runnable(){
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, MainMenu.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, 1200);
    }
}
