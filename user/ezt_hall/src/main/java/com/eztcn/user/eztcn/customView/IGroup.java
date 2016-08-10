package com.eztcn.user.eztcn.customView;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.eztcn.user.R;

/**
 * @title 点击下缩放效果
 * @describe 首页搜索、消息箱点击效果
 * @author ezt
 * @created 2015年2月4日
 */
public class IGroup extends RelativeLayout implements AnimationListener {

	final Animation mDownAnim;
	final Animation mUpAnim;
	private OnClickListener mClickListener;
	private OnClickListener l;
	private boolean r;
	private boolean mClick = true;
	private boolean isEnd = true;// 动画是否完成

	public IGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDownAnim = AnimationUtils.loadAnimation(context, R.anim.action_down);
		mDownAnim.setFillAfter(Boolean.TRUE);
		mUpAnim = AnimationUtils.loadAnimation(context, R.anim.action_up);
		mUpAnim.setAnimationListener(this);
	}

	public IGroup(Context context) {
		super(context);
		mDownAnim = AnimationUtils.loadAnimation(context, R.anim.action_down);
		mDownAnim.setFillAfter(Boolean.TRUE);
		mUpAnim = AnimationUtils.loadAnimation(context, R.anim.action_up);
		mUpAnim.setAnimationListener(this);
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (mClick) {
			if (mClickListener != null) {
				mClickListener.onClick(this);
			}
			mClickListener = this.l;
		} else {
			isEnd = Boolean.TRUE;
		}

	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isEnd) {
				startAnimation(mDownAnim);
				r = Boolean.TRUE;
				isEnd = Boolean.FALSE;
				handler.sendEmptyMessageDelayed(1, 500);
			} else {
				return false;
			}

			break;
		case MotionEvent.ACTION_MOVE:

			float moveX = event.getX();
			float moveY = event.getY();

			// 创建和位图一样位置的Rect
			Rect rect = new Rect(0, 0, this.getWidth(), this.getHeight());
			if (!rect.contains((int) moveX, (int) moveY)) {// 范围之外
				if (r) {
					mDownAnim.cancel();
					mClick = Boolean.FALSE;
					startAnimation(mUpAnim);
					r = Boolean.FALSE;
				}
			} else {
				mClick = Boolean.TRUE;
			}

			break;
		case MotionEvent.ACTION_UP:
			if (r) {
				playSoundEffect(SoundEffectConstants.CLICK);
				mDownAnim.cancel();
				startAnimation(mUpAnim);
				r = Boolean.TRUE;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			r = Boolean.TRUE;
			break;
		}
		return true;
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isEnd = Boolean.TRUE;

		}

	};

	@Override
	public void setOnClickListener(OnClickListener l) {
		mClickListener = l;
		this.l = l;
	}
}
