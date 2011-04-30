package com.whitepages.cutebarista;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Main extends Activity implements OnClickListener, LocationListener
{
	static final int DIALOG_CLICKED_CUTE = 0;
	static final int DIALOG_CLICKED_NOT_CUTE = 1;
	
	Location mLastGoodLocation = null;
	Dialog mDialog = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ImageButton cuteButton = (ImageButton) findViewById(R.id.button_cute);
		cuteButton.setOnClickListener(this);

		ImageButton notCuteButton = (ImageButton) findViewById(R.id.button_not_cute);
		notCuteButton.setOnClickListener(this);	
        
		ImageButton findCuteButton = (ImageButton) findViewById(R.id.button_find_cute);
		findCuteButton.setOnClickListener(this);		
		
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    
	
    /**
     *  Custom onclick handler
     */
	public void onClick(View v) 
	{		
		switch (v.getId()) 
		{
			case R.id.button_cute:
				clickedCute();
				break;
			case R.id.button_not_cute:
				clickedNotCute();
				break;
			case R.id.button_find_cute:
				clickedFindCute();
				break;
		}
	}   

	/**
	 * This is where we create our different dialogs used on this screen/view.
	 */
	protected Dialog onCreateDialog(int id) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id)
		{
			case DIALOG_CLICKED_CUTE:
				builder.setMessage("You found a cute barista, nice!");
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
				    }
				});
				return builder.create();
		}
		return null;
	}
	
	
    /**
     *  Handle clicking the "Cute" button
     */
	public void clickedCute()
	{		
		if (mLastGoodLocation != null)
		{
			double lat = mLastGoodLocation.getLatitude();
			double lon = mLastGoodLocation.getLongitude();
			
			sendCuteBaristaMessage(lat, lon, 1);
			showDialog(DIALOG_CLICKED_CUTE);
		}
	}

	
	public void sendCuteBaristaMessage(double lat, double lon, int rating)
	{
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String deviceid = telephonyManager.getDeviceId();
		
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		String dateTimeString = f.format(new Date());
	
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://10.0.2.2:3000/ratings");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
	        nameValuePairs.add(new BasicNameValuePair("dt", dateTimeString));
	        nameValuePairs.add(new BasicNameValuePair("device_id", deviceid));
	        nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(lat)));
	        nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(lon)));
	        nameValuePairs.add(new BasicNameValuePair("rating", String.valueOf(rating)));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        Log.w("NET", response.toString());
	        
	    } catch (ClientProtocolException e) {
	        // Do something interesting here
	    	e.printStackTrace();
	    } catch (IOException e) {
	        // Do something interesting here
	    	e.printStackTrace();
	    }
	}

	
    /**
     *  Handle clicking the "Not Cute" button
     */
	public void clickedNotCute()
	{
	}


    /**
     *  Handle clicking the "Find Cute Baristas" button
     */
	public void clickedFindCute()
	{
		if (mLastGoodLocation != null)
		{
			double lat = mLastGoodLocation.getLatitude();
			double lon = mLastGoodLocation.getLongitude();
			
			sendFindBarisasRequest(lat, lon);
		}		
	}
 
	
	public void sendFindBarisasRequest(double lat, double lon)
	{
		// /ratings?device_id=foo&max=10&top=1&bottom=-1&left=9&right=11"
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String deviceid = telephonyManager.getDeviceId();		
	
	    // Create a new HttpClient and Get Request 
	    HttpClient httpclient = new DefaultHttpClient();
	    String uri = "http://10.0.2.2:3000/ratings";
	    uri += "?device_id=" + deviceid;
	    uri += "&max=25";
	    uri += "&top=" + (lat - 0.01);
	    uri += "&bottom=" + (lat + 0.01);
	    uri += "&left=" + (lon - 0.05);
	    uri += "&right=" + (lon + 0.05);
	    HttpGet httpget = new HttpGet(uri);

	    try {	    	
	        HttpResponse response = httpclient.execute(httpget);
	        Log.w("NET", response.toString());
	        
	    } catch (ClientProtocolException e) {
	        // Do something interesting here
	    	e.printStackTrace();
	    } catch (IOException e) {
	        // Do something interesting here
	    	e.printStackTrace();
	    }
	}
	
	/*******************************
	 *  LOCATION 
	 *******************************/
	
	
	public void onLocationChanged(Location location) 
	{	
		mLastGoodLocation = location;
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	
	public void onProviderEnabled(String provider) {}
	
	public void onProviderDisabled(String provider) {}
}