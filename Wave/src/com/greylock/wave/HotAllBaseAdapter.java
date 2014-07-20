package com.greylock.wave;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.PushService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class HotAllBaseAdapter extends BaseAdapter {

	private final int mUnsubscribeDragDistance;
	private final Context mContext;
	private List<String> channelNames;
	private TextView channelName;
	private List<String> mSubscriptions;
	private List<String> mUsers;
	private TextView userNumber;

	private float mDragStart;

	public HotAllBaseAdapter(Context context, List<String> channelNames, List<String> subs, List<String> usersArea) {
		mContext = context;
		mUnsubscribeDragDistance = context.getResources().getInteger(
				R.integer.unsubscribe_drag_distance);
		this.channelNames = channelNames;
		mSubscriptions = subs;

		mUsers = usersArea;

		if (channelNames == null) {
			channelNames = new LinkedList<String>();
		}
		if (mSubscriptions == null) {
			mSubscriptions = new LinkedList<String>();
		}
		((WaveMainTabsActivity) context).bus.register(this);
		if (mUsers == null) {
			mUsers = new LinkedList<String>();
		}

		notifyDataSetChanged();
	}
	
	@Subscribe
	public void newData(Altercation a) {
		Log.i("Add/removing hot", a.s);
		if (a.b) {
			if (!mSubscriptions.contains(a.s)) {
				mSubscriptions.add(a.s);
			}
		} else {
			mSubscriptions.indexOf(a.s);
			mSubscriptions.remove(a.s);
		}
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelNames.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.wave_list_item, null);
		}

		userNumber = (TextView) convertView.findViewById(R.id.textView3);
		if(mUsers.size()>0 && position < mUsers.size()){
			userNumber.setText(mUsers.get(position));
		}
		channelName = (TextView) convertView.findViewById(R.id.channelName);
		channelName.setText(channelNames.get(position).replaceAll("_", " "));

		convertView.setClickable(true);
		final boolean rotate = !mSubscriptions.contains(channelNames.get(position));
		convertView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					v.getParent().requestDisallowInterceptTouchEvent(true);
					mDragStart = event.getX();
					break;
				case MotionEvent.ACTION_UP:
					v.findViewById(R.id.subscribe).animate().alpha(0).start();
					v.findViewById(R.id.channelName).animate().alpha(1f).start();
					v.findViewById(R.id.view2).animate().alpha(0).start();
					v.findViewById(R.id.imageView1).animate().alpha(0).start();
					v.findViewById(R.id.textView3).animate().alpha(1).start();
					if (rotate) v.findViewById(R.id.imageButton1).animate().alpha(1.0f).rotation(0.0f).start();
					if((mDragStart - event.getX()) / mUnsubscribeDragDistance > 1f) {
						
						PushService.subscribe(mContext, channelNames.get(position).replaceAll(" ", "_"), SendWaveActivity.class);
						//Toast.makeText(mContext, "You subscribed to " + channelNames.get(position), Toast.LENGTH_SHORT).show();
						((WaveMainTabsActivity) mContext).bus.post(new Altercation(true, channelNames.get(position), mUsers.get(position)));
					}
					v.getParent().requestDisallowInterceptTouchEvent(false);

					return true;
				case MotionEvent.ACTION_MOVE:
					if (mDragStart - event.getX() < 0.0) {
						v.getParent().requestDisallowInterceptTouchEvent(false);
					}
					if ((mDragStart - event.getX())/mUnsubscribeDragDistance > 1.0f) {
						v.findViewById(R.id.subscribe).setAlpha(1);
						v.findViewById(R.id.channelName).setAlpha(0);
						v.findViewById(R.id.view2).setAlpha(1);
						v.findViewById(R.id.imageView1).setAlpha(0);
						v.findViewById(R.id.textView3).setAlpha(0);
						if (rotate) v.findViewById(R.id.imageButton1).setRotation(90.0f);
						v.findViewById(R.id.imageButton1).setAlpha(1);
						return true;
					}
					v.findViewById(R.id.subscribe).setAlpha((mDragStart - event.getX()) / mUnsubscribeDragDistance);
					v.findViewById(R.id.view2).setAlpha((mDragStart - event.getX()) / mUnsubscribeDragDistance);
					v.findViewById(R.id.channelName).setAlpha(1-(mDragStart - event.getX()) / mUnsubscribeDragDistance);
					v.findViewById(R.id.imageView1).setAlpha(1-(mDragStart - event.getX()) / mUnsubscribeDragDistance);
					v.findViewById(R.id.textView3).setAlpha(1-(mDragStart - event.getX()) / mUnsubscribeDragDistance);
					if (rotate) {
						v.findViewById(R.id.imageButton1).setRotation((mDragStart - event.getX()) < 0.0 ? 0.0f : 90.0f*(mDragStart - event.getX()) / mUnsubscribeDragDistance);
						v.findViewById(R.id.imageButton1).setAlpha((mDragStart - event.getX()) < 0.0 ? 1.0f : 1.0f*(mDragStart - event.getX()) / mUnsubscribeDragDistance);
					}
				}
				return true;
			}
		});
		
		ImageButton button = (ImageButton) convertView
				.findViewById(R.id.imageButton1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				v.animate().rotationY(90.0f)
				.setListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {

					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						// TODO Auto-generated method stub
						((ImageButton) v)
						.setImageResource(R.drawable.check_icon);
						v.animate().rotationY(0.0f)
						.start();


					}

					@Override
					public void onAnimationCancel(Animator animation) {
					}
				}).start();


				PushService.subscribe(mContext, channelNames.get(position).replaceAll(" ", "_"), SendWaveActivity.class);
				//Toast.makeText(mContext, "You subscribed to " + channelNames.get(position), Toast.LENGTH_SHORT).show();
				((WaveMainTabsActivity) mContext).bus.post(new Altercation(true, channelNames.get(position), mUsers.get(position)));
			}

		});

		if(mSubscriptions.contains(channelNames.get(position))) {
			button.setImageResource(R.drawable.check_icon);
			button.setClickable(false);
			button.setFocusable(false);
		} else {
			button.setImageResource(R.drawable.plus_icon);
			button.setClickable(true);
			button.setFocusable(true);
		}

		return convertView;
	}



}
