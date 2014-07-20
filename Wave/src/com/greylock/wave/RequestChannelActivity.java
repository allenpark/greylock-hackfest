package com.greylock.wave;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;

public class RequestChannelActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_channel);
	}
	
	public void submitChannelRequest(View view) {
		
		EditText text = (EditText) findViewById(R.id.channelText);
		
		ParseObject channel = new ParseObject("Channel");
		channel.put("name", (text.getText()+"").replaceAll(" ", "_").trim());
		channel.put("approved", true);
		channel.put("reviewed", true);
		channel.saveInBackground();
		
		Toast.makeText(this, "Channel Submitted", Toast.LENGTH_SHORT).show();
		
	}
}
