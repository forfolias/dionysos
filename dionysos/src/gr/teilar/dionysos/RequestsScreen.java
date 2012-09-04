package gr.teilar.dionysos;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class RequestsScreen extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.requests_screen);

		if (Dionysos.checkUpdate(getBaseContext(), "requests")) {
			Intent i = new Intent(getBaseContext(),
					gr.teilar.dionysos.Dionysos.class);
			Bundle b = new Bundle();
			
			/* inform the activity of the type of data we want to retrieve
			 * 1 : grades
			 * 2 : lessons
			 * 3 : requests 
			 */
			b.putInt("id", 3);
			i.putExtras(b);
			startActivityForResult(i, 1);
		}
		else {
			displayXml();
		}
	}

	protected void displayXml() {
		Log.d("DION", "Starting display");

		File file = new File("/sdcard/egrammatia/requests.xml");

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(file);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		doc.getDocumentElement().normalize();
		String date = ((Element) doc.getElementsByTagName("requests").item(0))
				.getAttribute("date");

		((TextView) findViewById(R.id.requests_data_date)).setText(date);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				displayXml();
			}

			if (resultCode == RESULT_CANCELED) {
				/* TODO 
				 * print a message that we display old xml data */
			}
		}
	}
}
