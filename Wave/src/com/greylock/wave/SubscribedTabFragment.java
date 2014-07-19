package com.greylock.wave;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SubscribedTabFragment extends Fragment {
	public static Fragment newInstance() {
		return new SubscribedTabFragment();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_wave_subscribed_tab, container, false);
	}
}
