package com.greylock.wave;

import java.util.List;

import com.parse.PushService;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class SubscribedBaseAdapter extends BaseAdapter {
	private final int mAnimationDuration;
	private final int mUnsubscribeDragDistance;
	private final Context mContext;
	private List<String> channelNames;
	private TextView mChannelName;


	private float mDragStart;

	public SubscribedBaseAdapter(Context context, List<String> names) {
		mContext = context;
		mAnimationDuration = context.getResources().getInteger(
				R.integer.plus_icon_animation_duration);
		mUnsubscribeDragDistance = context.getResources().getInteger(
				R.integer.unsubscribe_drag_distance);
		channelNames = names;
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

		mChannelName = (TextView) convertView.findViewById(R.id.channelName);
		mChannelName.setText(channelNames.get(position).replaceAll("_", " "));

		convertView.findViewById(R.id.textView3).setVisibility(View.GONE);
		convertView.findViewById(R.id.imageView1).setVisibility(View.GONE);
		convertView.findViewById(R.id.imageButton1).setAlpha(0);

		convertView.setClickable(true);
		convertView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mDragStart = event.getX();
					break;
				case MotionEvent.ACTION_UP:
					v.findViewById(R.id.textView2).animate().alpha(0).start();
					v.findViewById(R.id.channelName).animate().alpha(1f).start();
					v.findViewById(R.id.imageButton1).animate().alpha(0.0f).rotation(0.0f).start();
					if((event.getX() - mDragStart) / mUnsubscribeDragDistance > 1f) {
						PushService.unsubscribe(mContext, channelNames.get(position).replaceAll(" ", "_"));
						channelNames.remove(position);
						notifyDataSetInvalidated();

						notifyDataSetChanged();
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (event.getX() - mDragStart < 5.0) v.getParent().requestDisallowInterceptTouchEvent(false);
					if ((event.getX() - mDragStart)/mUnsubscribeDragDistance > 1.0f) {
						v.findViewById(R.id.textView2).setAlpha(1);
						v.findViewById(R.id.channelName).setAlpha(0);
						v.findViewById(R.id.imageButton1).setRotation(135.0f);
						v.findViewById(R.id.imageButton1).setAlpha(1);
						return true;
					}
					v.findViewById(R.id.textView2).setAlpha((event.getX() - mDragStart) / mUnsubscribeDragDistance);
					v.findViewById(R.id.channelName).setAlpha(1-(event.getX() - mDragStart) / mUnsubscribeDragDistance);
					v.findViewById(R.id.imageButton1).setRotation((event.getX() - mDragStart) < 0.0 ? 0.0f : 135.0f*(event.getX() - mDragStart) / mUnsubscribeDragDistance);
					v.findViewById(R.id.imageButton1).setAlpha((event.getX() - mDragStart) < 0.0 ? 0.0f : 1.0f*(event.getX() - mDragStart) / mUnsubscribeDragDistance);
				}
				return true;
			}
		});
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
			}
		});
		return convertView;
	}

}
