package gr.teilar.dionysos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LessonsScreen extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lessons_screen);
		
		Intent i = new Intent(
				getBaseContext(),
				gr.teilar.dionysos.Dionysos.class);
		Bundle b = new Bundle();
		b.putInt("id", 2);
		i.putExtras(b);
		startActivity(i);
	}
}
