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
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

		((TextView) findViewById(R.id.requests_data_date)).setText(getResources().getString(R.string.data_date) + " " + date);
		
		NodeList requestsList = doc.getElementsByTagName("request");
		
		TableLayout table = (TableLayout) findViewById(R.id.tableRequests);
		
		
		for (int i = 0; i < requestsList.getLength(); i++){
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
			String reqDateString = ((Element) requestsList.item(i)).getAttribute("date");
			
			titleLine.setText(requestsList.item(i).getChildNodes().item(0).getNodeValue());
			titleLine.setTypeface(null, Typeface.BOLD);
			titleLine.setTextSize(14);
			
			subtitleLine.setText(getResources().getString(R.string.date) + " : " + reqDateString.substring(0, reqDateString.indexOf(" ")));
			subtitleLine.setTextSize(11);
			
			layout.addView(titleLine, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			layout.addView(subtitleLine, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			row.addView(layout);
			row.setPadding(7, 7, 7, 7);
			
			table.addView(hrLine, new LayoutParams(LayoutParams.FILL_PARENT, 1));
			table.addView(row, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
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
