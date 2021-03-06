package gr.teilar.dionysos;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;


public class PreferencesScreen extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_screen);
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (prefs.getString("username", "").equals("") || prefs.getString("password", "").equals(""))
			((Preference) findPreference("updatePreference")).setEnabled(false);
		
		Preference aboutPref = (Preference) findPreference("aboutPreference");
		aboutPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(getBaseContext(),
						gr.teilar.dionysos.AboutAppScreen.class);
				startActivity(i);
				return true;
			}
		});
		
		Preference downloadPref = (Preference) findPreference("updatePreference");
		downloadPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(getBaseContext(),
						gr.teilar.dionysos.Dionysos.class);
				startActivity(i);
				return true;
			}
		});
		
		Preference dataInformationPreference = (Preference) findPreference("dataInformationPreference");
		dataInformationPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(getBaseContext(),
						gr.teilar.dionysos.AboutDataScreen.class);
				startActivity(i);
				return true;
			}
		});
	}
}
