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
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class GradesScreen extends Activity {
	
	private int eksamina = 7;
	private int maxEksamina = 7;
	private LinearLayout[] eksaminaOuterLayout = new LinearLayout[maxEksamina];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grades_screen);
		
		if (Dionysos.checkUpdate(getBaseContext(), "grades")) {
			Intent i = new Intent(getBaseContext(),
					gr.teilar.dionysos.Dionysos.class);
			Bundle b = new Bundle();
			
			/* inform the activity of the type of data we want to retrieve
			 * 1 : grades
			 * 2 : lessons
			 * 3 : requests 
			 */
			b.putInt("id", 1);
			i.putExtras(b);
			startActivityForResult(i, 1);
		}
		else {
			((TextView) findViewById(R.id.grades_data_date)).setText(getResources().getString(R.string.display_old_data));
			displayXml();
		}
	}

	protected void displayXml() {
		TextView dataDate = (TextView) findViewById(R.id.grades_data_date);
				
		File file = new File(Environment.getExternalStorageDirectory().getPath()+"/egrammatia/grades.xml");

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
		String date = ((Element) doc.getElementsByTagName("grades").item(0))
				.getAttribute("date");

		dataDate.setText(dataDate.getText() + " " + date);
		
		NodeList eksaminaList = doc.getElementsByTagName("eksamino");
		eksamina = eksaminaList.getLength();
		
		for (int j = 0; j < eksamina; j++){
			eksaminaOuterLayout[j] = new LinearLayout(this);
			eksaminaOuterLayout[j].setOrientation(LinearLayout.VERTICAL);
			
			ScrollView eksaminaScroll = new ScrollView(this);
			LinearLayout mathimataLayout = new LinearLayout(this);
			RelativeLayout mathimaLine = new RelativeLayout(this);
			TextView eksaminoTitle = new TextView(this);
			TextView prevnext = null;
			
			eksaminoTitle.setText(getResources().getString(R.string.semester)+" "+(j+1)+"o");
			eksaminoTitle.setTextColor(getResources().getColor(R.color.text_color));
			eksaminoTitle.setTextSize(15);
			eksaminoTitle.setTypeface(null, Typeface.BOLD);
			
			RelativeLayout.LayoutParams align = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			align.addRule(RelativeLayout.CENTER_HORIZONTAL);
			mathimaLine.addView(eksaminoTitle, align);
			mathimaLine.setPadding(7, 7, 7, 7);
			
			if(j != eksamina-1){
				prevnext = new TextView(this);
				prevnext.setText("»»");
				prevnext.setPadding(0, 0, 10, 0);
				prevnext.setTypeface(null, Typeface.BOLD);
				prevnext.setTextSize(15);
				prevnext.setTextColor(getResources().getColor(R.color.text_color));
				align = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				align.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				mathimaLine.addView(prevnext, align);
			}
			if(j != 0){
				prevnext = new TextView(this);
				prevnext.setText("««");
				prevnext.setPadding(10, 0, 0, 0);
				prevnext.setTypeface(null, Typeface.BOLD);
				prevnext.setTextSize(15);
				prevnext.setTextColor(getResources().getColor(R.color.text_color));
				align = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				align.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				mathimaLine.addView(prevnext, align);
			}
			
			
			mathimataLayout.setOrientation(LinearLayout.VERTICAL);
			eksaminaOuterLayout[j].addView(mathimaLine, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			
			NodeList mathimata = ((Element) eksaminaList.item(j)).getElementsByTagName("lesson");
						
			for (int i = 0; i < mathimata.getLength(); i++){
				
				mathimaLine = new RelativeLayout(this);
				View hrLine = new View(this);
				hrLine.setBackgroundColor(Color.GRAY);
			
				if(i%2 == 0)
					mathimaLine.setBackgroundColor(getResources().getColor(R.color.row_even));
				else if(i%2 == 1)
					mathimaLine.setBackgroundColor(getResources().getColor(R.color.row_odd));
				
				TextView title = new TextView(this);
				TextView subtitleLine = new TextView(this);
				TextView vathmos = new TextView(this);
				String dm = ((Element) mathimata.item(i)).getAttribute("dm");
				String ores = ((Element) mathimata.item(i)).getAttribute("ores");
				
				vathmos.setText(((Element) mathimata.item(i)).getAttribute("vathmos"));
				vathmos.setTypeface(null, Typeface.BOLD);
				vathmos.setTextSize(17);
				vathmos.setPadding(5, 5, 10, 0);
				vathmos.setTextColor(getResources().getColor(R.color.text_color));
				vathmos.setId((30*(j+1))+i);
				
				title.setText(mathimata.item(i).getChildNodes().item(0).getNodeValue());
				title.setTypeface(null, Typeface.BOLD);
				title.setTextSize(14);
				title.setTextColor(getResources().getColor(R.color.text_color));
				title.setId((10*(j+1))+i);
				
				subtitleLine.setText(
						getResources().getString(R.string.hours) + " : " + ores + " // " +
						getResources().getString(R.string.ects) + " : " + dm
						);
				subtitleLine.setTextSize(11);
				subtitleLine.setTextColor(getResources().getColor(R.color.text_color));
				
				align = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				align.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				align.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				align.addRule(RelativeLayout.CENTER_VERTICAL);
				mathimaLine.addView(vathmos, align);
				
				
				align = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				align.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				align.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
				align.addRule(RelativeLayout.LEFT_OF, vathmos.getId());
				mathimaLine.addView(title, align);
				
				align = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				align.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				align.addRule(RelativeLayout.BELOW, title.getId());
				mathimaLine.addView(subtitleLine, align);
				
				
				mathimaLine.setPadding(7, 7, 7, 7);
				
				mathimataLayout.addView(mathimaLine, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			}
			eksaminaScroll.addView(mathimataLayout);
			eksaminaOuterLayout[j].addView(eksaminaScroll);
		}
		
		myPagerAdapter awesomeAdapter = new myPagerAdapter();
		ViewPager awesomePager = (ViewPager) findViewById(R.id.eksaminapager);
		awesomePager.setAdapter(awesomeAdapter);
	}
	
	private void displayError() {
		((TextView) findViewById(R.id.grades_data_date)).setText(getResources().getString(R.string.file_error));
	
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				((TextView) findViewById(R.id.grades_data_date)).setText(getResources().getString(R.string.data_date));
				displayXml();
			}

			if (resultCode == RESULT_CANCELED) {
				((TextView) findViewById(R.id.grades_data_date)).setText(getResources().getString(R.string.display_old_data));
				displayXml();
			}
		}
	}
	

    private class myPagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			return eksamina;
		}

		@Override
		public Object instantiateItem(View collection, int position) {
			((ViewPager) collection).addView(eksaminaOuterLayout[position], 0);
			return eksaminaOuterLayout[position];
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((LinearLayout) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==((LinearLayout)object);
		}

		@Override
		public void finishUpdate(View arg0) {}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {}
    }
}
