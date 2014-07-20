package com.greylock.wave;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.PushService;

public class WaveFeedActivity extends Activity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	private ArrayList<String> messages;
	String chan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wave_feed);
		//getActionBar().hide();

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
		messages = new ArrayList<String>();
		String j = getIntent().getStringExtra("data");
		JSONObject ob = null;
		try {
			ob = new JSONObject(j);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("WaveFeedActivity json", ob.toString());
		
		chan = null;
		try {
			chan = ob.getString("channel");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		chan = getIntent().getStringExtra("channel");
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String ar = sharedPreferences.getString(chan, null);
		sharedPreferences.edit().remove(chan).commit();
	
		JSONArray jar = null;
		try {
			if (ar != null) {
			jar = new JSONArray(ar);
			} else {
				jar = new JSONArray();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < jar.length(); i++) {
			try {
				messages.add((String) jar.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}
	
	public void snoozeChannel(View view) {
		
		SharedPreferences prefs; 
	    prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    SharedPreferences.Editor editor = prefs.edit();
		editor.putString("singleChannelSnooze", chan);
		editor.commit();

		PushService.unsubscribe(getApplicationContext(), chan);
		
		Toast.makeText(getApplicationContext(), "Channel Snoozed", Toast.LENGTH_SHORT).show();
		
	}
	
	public void blockUser(View view) {
		Toast.makeText(getApplicationContext(), "User Blocked", Toast.LENGTH_SHORT).show();
		startActivity(new Intent(this, SendWaveActivity.class));

	}
	
public void snoozeAll(View view) {
		
		SharedPreferences prefs; 
		Context mContext = getApplicationContext();
	    prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    SharedPreferences.Editor editor = prefs.edit();
		ParseInstallation currentInstall = ParseInstallation.getCurrentInstallation();
		List<String> channelNames = currentInstall.getList("channels");
		//Set<String> myChannels = new HashSet<String>(channelNames);
		Set<String> setOfAllSubscriptions = PushService.getSubscriptions(getApplicationContext());		
		editor.putStringSet("snoozeChannel", setOfAllSubscriptions);
		editor.commit();
		
		for(int i=0; i< channelNames.size(); i++) {
			PushService.unsubscribe(getApplicationContext(), channelNames.get(i));
		}
		
		Toast.makeText(getApplicationContext(), "Snooze enabled", Toast.LENGTH_SHORT).show();
		finish();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wave_feed, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(messages.get(position));
		}

		@Override
		public int getCount() {
			return messages.size();
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(String sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putString(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_wave_feed,
					container, false);
			((TextView) rootView.findViewById(R.id.textView1)).setText(getArguments().getString(ARG_SECTION_NUMBER));
			return rootView;
		}
	}

}
