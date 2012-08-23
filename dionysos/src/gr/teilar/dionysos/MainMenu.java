package gr.teilar.dionysos;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenu extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		
		File myFile = new File("/sdcard/egrammatia/grades.xml");
		myFile.delete();
		myFile = new File("/sdcard/egrammatia/lessons.xml");
		myFile.delete();
		myFile = new File("/sdcard/egrammatia/requests.xml");
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
								gr.teilar.dionysos.Dionysos.class);
						startActivity(i);
					}
				});
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, R.string.downloading_data).setIcon(
				R.drawable.ic_menu_save);
		menu.add(0, 2, 0, R.string.preferences).setIcon(
				R.drawable.ic_menu_preferences);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case 1:
			i = new Intent(
					getBaseContext(),
					gr.teilar.dionysos.Dionysos.class);
			startActivity(i);
			return true;
		case 2:
			i = new Intent(
					getBaseContext(),
					gr.teilar.dionysos.PreferencesScreen.class);
			startActivity(i);
			return true;
		}
		return false;
	}
}
