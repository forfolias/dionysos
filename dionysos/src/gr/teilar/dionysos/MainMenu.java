package gr.teilar.dionysos;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenu extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		
		File myFile = new File("/sdcard/mysdfile.txt");
		myFile.delete();

		findViewById(R.id.preferences_button).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(
								getBaseContext(),
								gr.teilar.dionysos.PreferencesScreen.class);
						startActivity(i);
					}
				});
		findViewById(R.id.rates_button).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(
								getBaseContext(),
								gr.teilar.dionysos.RatesScreen.class);
						startActivity(i);
					}
				});
	}
}
