package com.greylock.wave;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

public class WaveFeedActivity extends Activity {
	WindowManager manager;
	ImageView imageView;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	   // Get window manager reference

	    manager = (WindowManager) getSystemService(WINDOW_SERVICE);

	    imageView= new ImageView(this);
	    imageView.setImageResource(R.drawable.ic_launcher);

	    // Setup layout parameter
	    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
	        WindowManager.LayoutParams.WRAP_CONTENT,
	        WindowManager.LayoutParams.WRAP_CONTENT,
	        WindowManager.LayoutParams.TYPE_PHONE,
	        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
	        PixelFormat.TRANSLUCENT);

	    params.gravity = Gravity.TOP | Gravity.LEFT; // Orientation
	    params.x = 100; // where you want to draw this, coordinates
	    params.y = 100;
	    // At it to window manager for display, it will be printed over any thing
	    manager.addView(imageView, params);


	   // Make sure to remove it when you are done, else it will stick there until you reboot
	   // Do keep track of same reference of view you added, don't mess with that
	}
	
	public void onDestroy() {
		super.onDestroy();
		manager.removeView(imageView);
	}
}
