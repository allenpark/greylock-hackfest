package com.greylock.wave;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class HotAllLTabListFragment extends ListFragment {
	public static Fragment newInstance() {
		return new HotAllLTabListFragment();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Channel");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> channelList, ParseException e) {
				if (e == null) {
					Log.d("score", "Retrieved " + channelList.size()
							+ " scores");
					buildChannelNames(channelList);
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
		
		

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void buildChannelNames(List<ParseObject> list) {
		List<String> channelNames = new ArrayList<String>();

		for (int i = 0; i < list.size(); i++) {
			channelNames.add(list.get(i).getString("name"));
		}

		setListAdapter(new HotAllBaseAdapter(getActivity(), channelNames));

	}
}
