package gr.teilar.dionysos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

public class RatesScreen extends Activity {

	public WebView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rates_screen);

		tv = (WebView) findViewById(R.id.webView1);
		dorequest();

	}

	public void dorequest() {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		try {
//            HttpGet httpget = new HttpGet("https://dionysos.teilar.gr/unistudent/login.asp");
//
//            HttpResponse ratesresponse = httpclient.execute(httpget);
//
//            HttpPost httpost = new HttpPost("https://dionysos.teilar.gr/unistudent/login.asp");
//
//            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
//            nvps.add(new BasicNameValuePair("userName", "ouramaro"));
//            nvps.add(new BasicNameValuePair("pwd", "koukouroukoukou"));
//            nvps.add(new BasicNameValuePair("loginTrue", "login"));
//
//            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
//
//            ratesresponse = httpclient.execute(httpost);
//            httpget = new HttpGet("https://dionysos.teilar.gr/unistudent/stud_CResults.asp?studPg=1&mnuid=mnu3");
//            ratesresponse = httpclient.execute(httpget);
//            String html = inputStreamToString(ratesresponse.getEntity().getContent()).toString();
//            writeToFile(html);
            String html = getHtmlFromFile();
            String resp = "";
			
            Document doc = Jsoup.parse(html);
			
			Elements gradestable = doc.select("table[cellpadding=4]");
			Elements grades = gradestable.select("td[colspan=2]");
			
			
			/* DEBUG */
			
				Log.d("DEBUG", grades.html());
			
			/* END DEBUG */
			
			resp = grades.html();
			
			tv.loadDataWithBaseURL(null, resp, "text/html", "utf-8", null);
            
//        } catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
	}
	
	private String getHtmlFromFile() {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard,"html.txt");
		StringBuilder text = new StringBuilder();

		try {
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;

		    while ((line = br.readLine()) != null) {
		        text.append(line);
		        text.append('\n');
		    }
		}
		catch (IOException e) {
		    //You'll need to add proper error handling here
		}
		
		return text.toString();
	
	}

	private void writeToFile(String resp) {
		try {

			File myFile = new File("/sdcard/mysdfile.txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(resp);
			myOutWriter.close();
			fOut.close();
		} catch (Exception e) {
			// todo
		}
	}

	private StringBuilder inputStreamToString(InputStream is) {
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
}
