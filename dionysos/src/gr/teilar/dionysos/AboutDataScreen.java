package gr.teilar.dionysos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AboutDataScreen extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_info_screen);
		
		findViewById(R.id.update_data_now_info_screen).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(
								getBaseContext(),
								gr.teilar.dionysos.Dionysos.class);
						startActivity(i);
					}
				});
		TextView gradesDate = (TextView) findViewById(R.id.data_info_grades_date_tv);
		TextView lessonsDate = (TextView) findViewById(R.id.data_info_lessons_date_tv);
		TextView requestsDate = (TextView) findViewById(R.id.data_info_requests_date_tv);
		
		
		/* TODO read the xml files and display the correct dates */
		
		gradesDate.setText("31/2/2012");
		lessonsDate.setText("31/2/2012");
		requestsDate.setText("31/2/2012");
	}
}
