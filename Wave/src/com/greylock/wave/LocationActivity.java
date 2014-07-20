package com.greylock.wave;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

public class LocationActivity extends Activity {

	private GoogleMap map;
	private Circle mapCircle;
	double lat, lon;

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

				Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_LONG).show();

			}       
		});             

	}

	private void centerMapOnMyLocation() {
		
		map.setMyLocationEnabled(true);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 12.0f));


		
	}

	//	/*
	//	   * Displays a circle on the map representing the search radius
	//	   */
	//	  private void updateCircle(LatLng myLatLng) {
	//	    if (mapCircle == null) {
	//	      mapCircle =
	//	          map.addCircle(
	//	              new CircleOptions().center(myLatLng).radius(radius * METERS_PER_FEET));
	//	      int baseColor = Color.DKGRAY;
	//	      mapCircle.setStrokeColor(baseColor);
	//	      mapCircle.setStrokeWidth(2);
	//	      mapCircle.setFillColor(Color.argb(50, Color.red(baseColor), Color.green(baseColor),
	//	          Color.blue(baseColor)));
	//	    }
	//	    mapCircle.setCenter(myLatLng);
	//	    mapCircle.setRadius(radius * METERS_PER_FEET); // Convert radius in feet to meters.
	//	  }

}
