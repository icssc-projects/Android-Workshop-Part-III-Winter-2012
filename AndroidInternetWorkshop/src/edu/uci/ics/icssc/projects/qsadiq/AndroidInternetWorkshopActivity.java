package edu.uci.ics.icssc.projects.qsadiq;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AndroidInternetWorkshopActivity extends Activity {
	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb=null;
	String url = "http://workshop.appjam.roboteater.com/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button button = (Button) findViewById(R.id.ok);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				EditText et = (EditText) findViewById(R.id.entry);
				connect(et.getText().toString());
			}
		});  
	}

	void connect(String courseDept){
		url += "?depname=" + courseDept;
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		}catch(Exception e){
			Log.e("log_tag", "Error in http connection"+e.toString());
		}
		//convert response to string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line="0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
		}
		
		
		String[] courseNumbers = null;
		String[] profs = null;
		
		try{
			jArray = new JSONArray(result);
			courseNumbers = new String[jArray.length()];
			profs = new String[jArray.length()];
			JSONObject json_data=null;
			for(int i=0;i<jArray.length();i++){
				json_data = jArray.getJSONObject(i);
				courseNumbers[i] = json_data.getString("classno");
				profs[i] = json_data.getString("prof");
			}
		}
		catch(JSONException e1){
			Toast.makeText(getBaseContext(), "No Classes in Department" ,Toast.LENGTH_LONG).show();
			return;
		} catch (ParseException e1) {
			Toast.makeText(getBaseContext(), "No Classes in Department" ,Toast.LENGTH_LONG).show();
			e1.printStackTrace();
		}
		
		Intent myIntent = new Intent(this, ClassList.class);
		myIntent.putExtra("edu.uci.ics.icssc.projects.qsadiq.classList.courseNumbers", courseNumbers); 
		myIntent.putExtra("edu.uci.ics.icssc.projects.qsadiq.classList.profs", profs); 
		startActivity(myIntent);  
	}
}