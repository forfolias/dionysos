package gr.teilar.dionysos;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LessonsScreen extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lessons_screen);

		if (Dionysos.checkUpdate(getBaseContext(), "lessons")) {
			Intent i = new Intent(getBaseContext(),
					gr.teilar.dionysos.Dionysos.class);
			Bundle b = new Bundle();
			
			/* inform the activity of the type of data we want to retrieve
			 * 1 : grades
			 * 2 : lessons
			 * 3 : requests 
			 */
			b.putInt("id", 2);
			i.putExtras(b);
			startActivityForResult(i, 1);
		}
		else {
			((TextView) findViewById(R.id.lessons_data_date)).setText(getResources().getString(R.string.display_old_data));
			displayXml();
		}
	}

	protected void displayXml() {
		File file = new File(Environment.getExternalStorageDirectory().getPath()+"/egrammatia/lessons.xml");

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(file);
		} catch (SAXException e) {
			displayError();
			return;
		} catch (IOException e) {
			displayError();
			return;
		} catch (ParserConfigurationException e1) {
			displayError();
			return;
		}

		doc.getDocumentElement().normalize();
		String date = ((Element) doc.getElementsByTagName("lessons").item(0))
				.getAttribute("date");

		TextView dataDate = (TextView) findViewById(R.id.lessons_data_date);
		dataDate.setText(dataDate.getText() + " " + date);
		
		NodeList lessonssList = doc.getElementsByTagName("lesson");
		
		TableLayout table = (TableLayout) findViewById(R.id.tableLessons);
		
		
		for (int i = 0; i < lessonssList.getLength(); i++){
			TableRow row = new TableRow(this);
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			View hrLine = new View(this);
			hrLine.setBackgroundColor(Color.GRAY);
		
			if(i%2 == 0)
				row.setBackgroundColor(getResources().getColor(R.color.row_even));
			else if(i%2 == 1)
				row.setBackgroundColor(getResources().getColor(R.color.row_odd));
			
			TextView titleLine = new TextView(this);
			TextView subtitleLine = new TextView(this);
			String eksamino = ((Element) lessonssList.item(i)).getAttribute("eksamino");
			String dm = ((Element) lessonssList.item(i)).getAttribute("dm");
			String ores = ((Element) lessonssList.item(i)).getAttribute("ores");
			
			titleLine.setText(lessonssList.item(i).getChildNodes().item(0).getNodeValue());
			titleLine.setTypeface(null, Typeface.BOLD);
			titleLine.setTextSize(14);
			titleLine.setTextColor(getResources().getColor(R.color.text_color));
			
			subtitleLine.setText(
					getResources().getString(R.string.semester) + ": " + eksamino + " // " +
					getResources().getString(R.string.hours) + " : " + ores + " // " +
					getResources().getString(R.string.ects) + " : " + dm
					);
			subtitleLine.setTextSize(11);
			subtitleLine.setTextColor(getResources().getColor(R.color.text_color));
			
			layout.addView(titleLine, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			layout.addView(subtitleLine, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			 
			row.addView(layout);
			row.setPadding(7, 7, 7, 7);
			
			table.addView(hrLine, new LayoutParams(LayoutParams.FILL_PARENT, 1));
			table.addView(row, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}
	
	private void displayError() {
		((TextView) findViewById(R.id.lessons_data_date)).setText(getResources().getString(R.string.file_error));
	
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				((TextView) findViewById(R.id.lessons_data_date)).setText(getResources().getString(R.string.data_date));
				displayXml();
			}

			if (resultCode == RESULT_CANCELED) {
				((TextView) findViewById(R.id.lessons_data_date)).setText(getResources().getString(R.string.display_old_data));
				displayXml();
			}
		}
	}
}
