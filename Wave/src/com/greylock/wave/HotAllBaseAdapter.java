package com.greylock.wave;

import java.util.List;

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
import android.widget.Toast;

import com.parse.PushService;

public class HotAllBaseAdapter extends BaseAdapter {

	private final int mAnimationDuration;
	private final int mUnsubscribeDragDistance;
	private final Context mContext;
	private List<String> channelNames;
	private TextView channelName;

	private float mDragStart;

	public HotAllBaseAdapter(Context context, List<String> channelNames) {
		mContext = context;
		mAnimationDuration = context.getResources().getInteger(
				R.integer.plus_icon_animation_duration);
		mUnsubscribeDragDistance = context.getResources().getInteger(
				R.integer.unsubscribe_drag_distance);
		this.channelNames = channelNames;
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

		channelName = (TextView) convertView.findViewById(R.id.textView1);
		channelName.setText(channelNames.get(position).replaceAll("_", " "));
		convertView.setClickable(true);

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
					v.findViewById(R.id.view2).animate().alpha(0).start();
					v.findViewById(R.id.imageButton1).animate().rotation(0.0f).start();
					break;
				case MotionEvent.ACTION_MOVE:
					if (mDragStart - event.getX() < 5.0) v.getParent().requestDisallowInterceptTouchEvent(false);
					if ((mDragStart - event.getX())/mUnsubscribeDragDistance > 1.0f) {
						v.findViewById(R.id.view2).setAlpha(1);
						v.findViewById(R.id.imageButton1).setRotation(180.0f);
						return true;
					}
					v.findViewById(R.id.view2).setAlpha((mDragStart - event.getX()) / mUnsubscribeDragDistance);
					v.findViewById(R.id.imageButton1).setRotation((mDragStart - event.getX()) < 0.0 ? 0.0f : 180.0f*(mDragStart - event.getX()) / mUnsubscribeDragDistance);
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
				

				PushService.subscribe(mContext, channelNames.get(position).replaceAll(" ", ""), SendWaveActivity.class);
				Toast.makeText(mContext, "You subscribed to " + channelNames.get(position), Toast.LENGTH_SHORT).show();
			}
			
		});
		
		return convertView;
	}

}
