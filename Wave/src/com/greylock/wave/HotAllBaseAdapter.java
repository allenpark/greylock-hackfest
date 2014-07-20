package com.greylock.wave;

import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.PushService;

public class HotAllBaseAdapter extends BaseAdapter {

	private final int mAnimationDuration;
	private final int mUnsubscribeDragDistance;
	private final Context mContext;
	private List<String> channelNames;
	private TextView channelName;
	private List<String> mSubscriptions;

	private float mDragStart;

	public HotAllBaseAdapter(Context context, List<String> channelNames, List<String> subs) {
		mContext = context;
		mAnimationDuration = context.getResources().getInteger(
				R.integer.plus_icon_animation_duration);
		mUnsubscribeDragDistance = context.getResources().getInteger(
				R.integer.unsubscribe_drag_distance);
		this.channelNames = channelNames;
		mSubscriptions = subs;
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
		if (convertView != null) {
			return convertView;
		} else {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.wave_list_item, null);
		}

		channelName = (TextView) convertView.findViewById(R.id.channelName);
		channelName.setText(channelNames.get(position).replaceAll("_", " "));
		convertView.setClickable(true);

		ImageButton button = (ImageButton) convertView
				.findViewById(R.id.imageButton1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				v.animate().rotationY(90.0f).setDuration(mAnimationDuration)
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
						.setDuration(mAnimationDuration)
						.start();


					}

					@Override
					public void onAnimationCancel(Animator animation) {
					}
				}).start();
				

				PushService.subscribe(mContext, channelNames.get(position).replaceAll(" ", "_"), SendWaveActivity.class);
				Toast.makeText(mContext, "You subscribed to " + channelNames.get(position), Toast.LENGTH_SHORT).show();
			}
			
		});
		
		if(mSubscriptions.contains(channelNames.get(position))) {
			button.setImageResource(R.drawable.check_icon);
			button.setClickable(false);
			button.setFocusable(false);
		}
		
		return convertView;
	}

}
