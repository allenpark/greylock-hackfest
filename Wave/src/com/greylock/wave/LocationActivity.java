package com.greylock.wave;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class LocationActivity extends Activity implements LocationListener,
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{

	private GoogleMap map;
	private Circle mapCircle;
	private static final float METERS_PER_FEET = 0.3048f;
	private static int progressLoc= 100;
	double lat, lon;
	private LocationClient locationClient;
	  private LocationRequest locationRequest;
	  
	  
	// Milliseconds per second
	  private static final int MILLISECONDS_PER_SECOND = 1000;

	  // The update interval
	  private static final int UPDATE_INTERVAL_IN_SECONDS = 5;

	  // A fast interval ceiling
	  private static final int FAST_CEILING_IN_SECONDS = 1;
	  
	// Update interval in milliseconds
	  private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
	      * UPDATE_INTERVAL_IN_SECONDS;

	  // A fast ceiling of update intervals, used when the app is visible
	  private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
	      * FAST_CEILING_IN_SECONDS;





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

		setContentView(R.layout.activity_location);
		Resources resources = getResources();
		final ActionBar actionBar = getActionBar();

		Intent prevIntent = getIntent();
		lat = prevIntent.getExtras().getDouble("lat");
		lon = prevIntent.getExtras().getDouble("lon");


		actionBar.setBackgroundDrawable(new ColorDrawable(resources
				.getColor(R.color.actionbar_tab_background_color)));
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(resources
				.getColor(R.color.actionbar_tab_background_color)));

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		centerMapOnMyLocation();

		final SeekBar sk=(SeekBar) findViewById(R.id.seekBar1);     
		sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       

			@Override       
			public void onStopTrackingTouch(SeekBar seekBar) {      
				// TODO Auto-generated method stub      
			}       

			@Override       
			public void onStartTrackingTouch(SeekBar seekBar) {     
				// TODO Auto-generated method stub      
			}       

			@Override       
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
				// TODO Auto-generated method stub      

				progressLoc = progress;
				updateCircle(new LatLng(lat, lon), progress);
			}       
		});  
		
	    locationClient = new LocationClient(this, this, this);
	 // Create a new global location parameters object
	    locationRequest = LocationRequest.create();

	    // Set the update interval
	    locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

	    // Use high accuracy
	    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	    // Set the interval ceiling to one minute
	    locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);


	}

	private void centerMapOnMyLocation() {

		map.setMyLocationEnabled(true);

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 12.0f));



	}

	/*
	 * Displays a circle on the map representing the search radius
	 */
	private void updateCircle(LatLng myLatLng, double radius) {
		if (mapCircle == null) {
			mapCircle =
					map.addCircle(
							new CircleOptions().center(myLatLng).radius(radius * METERS_PER_FEET));
			int baseColor = Color.DKGRAY;
			mapCircle.setStrokeColor(baseColor);
			mapCircle.setStrokeWidth(2);
			mapCircle.setFillColor(Color.argb(50, Color.red(baseColor), Color.green(baseColor),
					Color.blue(baseColor)));
		}
		mapCircle.setCenter(myLatLng);
		mapCircle.setRadius(radius * METERS_PER_FEET); // Convert radius in feet to meters.
	}

	@Override
	public void onLocationChanged(Location location) {

		LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());

		// Update map radius indicator
		updateCircle(myLatLng, progressLoc);


	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		startPeriodicUpdates();

	}

	private void startPeriodicUpdates() {
		locationClient.requestLocationUpdates(locationRequest, this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

}
