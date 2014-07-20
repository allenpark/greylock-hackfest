package com.greylock.wave;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

public class SendWaveActivity extends Activity {

	ArrayList<String> channelNames;
	Spinner channelOptions;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_wave);
		channelNames = new ArrayList<String>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Channel");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> channelList, ParseException e) {
		        if (e == null) {
		            Log.d("score", "Retrieved " + channelList.size() + " scores");
		            	buildChannelNames(channelList);
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		});
	}

	public void sendWave(View view) {
		LinkedList<String> channels = new LinkedList<String>();
		channels.add(channelOptions.getSelectedItem().toString());
		EditText message = (EditText) findViewById(R.id.editText1);
		 
		ParsePush push = new ParsePush();
		JSONObject data = null;
		try {
			data = new JSONObject("{\"action\": \"com.greylock.wave.NEW_WAVE\", \"message\": \"" + message.getText().toString() + "\", \"channel\": \"" + channelOptions.getSelectedItem().toString() + "\"}");
			Log.i("SendWaveActivity sendWave", data.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		push.setChannels(channels); // Notice we use setChannels not setChannel
		push.setData(data);
		push.sendInBackground();

	}
	
	private void buildChannelNames(List<ParseObject> list)
	{
		channelOptions = (Spinner) findViewById(R.id.spinner1);

		for(int i=0; i<list.size(); i++)
		{
			channelNames.add(list.get(i).getString("name"));
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, channelNames);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		
		channelOptions.setAdapter(dataAdapter);
	}
}
