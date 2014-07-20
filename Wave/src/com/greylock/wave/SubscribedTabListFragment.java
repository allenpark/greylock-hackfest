package com.greylock.wave;

import java.util.List;

import com.parse.ParseInstallation;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SubscribedTabListFragment extends ListFragment {
	public static Fragment newInstance() {
		return new SubscribedTabListFragment();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {




		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void onResume() {
		super.onResume();

		ParseInstallation currentInstall = ParseInstallation.getCurrentInstallation();
		List<String> channelNames = currentInstall.getList("channels");
		setListAdapter(new SubscribedBaseAdapter(getActivity(), channelNames));



	}




}
