package com.greylock.wave;

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
}
