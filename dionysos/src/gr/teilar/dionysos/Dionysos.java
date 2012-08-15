package gr.teilar.dionysos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Dionysos extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.downloading);
		
		final ProgressBar progress = (ProgressBar)findViewById(R.id.downloadingProgressBar);
        final TextView    textview = (TextView)findViewById(R.id.downloadingProgressText);
        
        new DownloadData(this, progress, textview).execute();
	}

	private static void connectToDionysos(String username, String password) {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpGet httpget = new HttpGet(
				"https://dionysos.teilar.gr/unistudent/login.asp");

		try {
			@SuppressWarnings("unused")
			HttpResponse ratesresponse;
			ratesresponse = httpclient.execute(httpget);

			HttpPost httpost = new HttpPost(
					"https://dionysos.teilar.gr/unistudent/login.asp");

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("userName", username));
			nvps.add(new BasicNameValuePair("pwd", password));
			nvps.add(new BasicNameValuePair("loginTrue", "login"));

			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			ratesresponse = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String downloadGrades() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String html = "";
		try {
			
			HttpGet httpget = new HttpGet(
					"https://dionysos.teilar.gr/unistudent/stud_CResults.asp?studPg=1&mnuid=mnu3");
			HttpResponse ratesresponse = httpclient.execute(httpget);
			html = inputStreamToString(
					ratesresponse.getEntity().getContent()).toString();
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
		return html;
	}

	private static void parseAndCreateGradesXML(String html) {
		Document doc = Jsoup.parse(html);

		Elements gradestable = doc.select("table[cellpadding=4]");
		Elements grades = gradestable.select("td[colspan=2]");

		Elements trs = grades.select("table > tbody > tr");
		Elements tds;

		int eksamino = 0;

		XmlSerializer xmlSerializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();

		try {
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument("UTF-8", true);
			xmlSerializer.startTag("", "lessons");

			for (Element tr : trs) {
				if (tr.attr("height").equals("15")
						|| tr.className().equals("italicHeader")
						|| tr.className().equals("subHeaderBack"))
					continue;
				else {
					tds = tr.select("td");

					if (tds.size() == 1) {
						eksamino++;
					} else if (tds.size() == 8) {
						xmlSerializer.startTag("", "lesson");
						xmlSerializer.attribute("", "eksamino",
								Integer.toString(eksamino));
						xmlSerializer.attribute("", "ores", tds.get(4).text());
						xmlSerializer.attribute("", "vathmos", tds.get(6)
								.text());
						xmlSerializer.text(tds.get(1).text()
								.replaceAll("\\(.*?\\)  ", ""));
						xmlSerializer.endTag("", "lesson");
					}
				}
			}

			xmlSerializer.endTag("", "lessons");
			xmlSerializer.endDocument();

			File direct = new File(Environment.getExternalStorageDirectory()
					+ "/egrammatia");
			if (!direct.exists()) {
				direct.mkdir();
			}

			File myFile = new File("/sdcard/egrammatia/grades.xml");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(writer.toString());
			myOutWriter.close();
			fOut.close();

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

	}

	private static String getHtmlFromFile() {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard, "html.txt");
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
		} catch (IOException e) {
			// You'll need to add proper error handling here
		}

		return text.toString();

	}

	private static StringBuilder inputStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is,
				Charset.forName("cp1253")));

		// Read response until the end
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Return full string
		return total;
	}
	
	private Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;

		return false;
	}
	
	final class DownloadData extends AsyncTask<Void, Integer, Void> {
	    private final int titles[] = {R.string.connecting,
	    							  R.string.downloading_grades,
	                                  R.string.writing_grades};
	    private final int progr[]  = {10, 15, 15};

	    private int index;

	    private final Activity parent;
	    private final ProgressBar progress;
	    private final TextView textview;

	    public DownloadData(final Activity parent, final ProgressBar progress, final TextView textview) {
	        this.parent = parent;
	        this.progress = progress;
	        this.textview = textview;
	    }

	    @Override
	    protected void onPreExecute() {
	        int max = 0;
	        for (final int p : progr) {
	            max += p;
	        }
	        progress.setMax(max);
	        index = 0;
	        progress.setVisibility(View.VISIBLE);
	    }

	    @Override
	    protected Void doInBackground(final Void... params) {
	    	publishProgress();
	    	
	    	if(!isOnline())
	    		Toast.makeText(parent, R.string.connection_error,
						Toast.LENGTH_LONG).show();
	    	
	    	SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this.parent);
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");
			Boolean error = false;
			String errorMsg = "";

			if (username.equals("")) {
				error = true;
				errorMsg = getResources().getString(R.string.missingUsername);
			}
			if (password.equals("")) {
				error = true;
				errorMsg = getResources().getString(R.string.missingPassword);
			}

			if (error) {
				Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG)
						.show();
				Intent i = new Intent(getBaseContext(),
						gr.teilar.dionysos.PreferencesScreen.class);
				startActivity(i);
			}
			
			connectToDionysos(username, password);
			publishProgress();

			parseAndCreateGradesXML(downloadGrades());
			publishProgress();
			
	        return null;
	    }

	    @Override
	    protected void onProgressUpdate(final Integer... values) {
	        textview.setText(titles[index]);
	        progress.incrementProgressBy(progr[index]);
	        ++index;
	    }

	    @Override
	    protected void onPostExecute(final Void result) {
	    	progress.setVisibility(View.GONE);
	    	if (index == 1)
	    		Toast.makeText(parent, R.string.download_fail,
						Toast.LENGTH_LONG).show();
	    	else
	    		Toast.makeText(parent, R.string.download_success,
					Toast.LENGTH_LONG).show();
	        parent.finish();
	    }
	}
	
	
}