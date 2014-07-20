package com.greylock.wave;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HotAllLTabListFragment extends ListFragment {
	public static Fragment newInstance() {
		return new HotAllLTabListFragment();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setListAdapter(new TestAdapter(getActivity()));
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
