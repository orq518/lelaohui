//
//  Created by XuGuobiao  2012-8-25
//  Copyright 2012�? ever. All rights reserved
//
package com.yh.materialdesign.anination;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationController {
	public static final int _UP = 0;
	public static final int _DOWN = 1;
	public static final int _LEFT = 2;
	public static final int _RIGHT = 3;

	public final int rela1 = Animation.RELATIVE_TO_SELF;
	public final int rela2 = Animation.RELATIVE_TO_PARENT;

	public final int Default = -1;
	public final int Linear = 0;
	public final int Accelerate = 1;
	public final int Decelerate = 2;
	public final int AccelerateDecelerate = 3;
	public final int Bounce = 4;
	public final int Overshoot = 5;
	public final int Anticipate = 6;
	public final int AnticipateOvershoot = 7;

	// LinearInterpolator,AccelerateInterpolator,DecelerateInterpolator,AccelerateDecelerateInterpolator,
	// BounceInterpolator,OvershootInterpolator,AnticipateInterpolator,AnticipateOvershootInterpolator

	public AnimationController() {
	}

	private class MyAnimationListener implements AnimationListener {
		private View view;

		public MyAnimationListener(View view) {
			this.view = view;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// this.view.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			this.view.setVisibility(View.GONE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

	}

	// 设置动画方式
	private void setEffect(Animation animation, int interpolatorType,
			long durationMillis, long delayMillis) {
		switch (interpolatorType) {
		case 0:
			animation.setInterpolator(new LinearInterpolator());
			break;
		case 1:
			animation.setInterpolator(new AccelerateInterpolator());
			break;
		case 2:
			animation.setInterpolator(new DecelerateInterpolator());
			break;
		case 3:
			animation.setInterpolator(new AccelerateDecelerateInterpolator());
			break;
		case 4:
			animation.setInterpolator(new BounceInterpolator());
			break;
		case 5:
			animation.setInterpolator(new OvershootInterpolator());
			break;
		case 6:
			animation.setInterpolator(new AnticipateInterpolator());
			break;
		case 7:
			animation.setInterpolator(new AnticipateOvershootInterpolator());
			break;
		default:
			break;
		}
		animation.setDuration(durationMillis);
		animation.setStartOffset(delayMillis);
	}

	// 进入动画
	private void baseIn(View view, Animation animation, long durationMillis,
			long delayMillis) {
		setEffect(animation, Overshoot, durationMillis, delayMillis);
		view.setVisibility(View.VISIBLE);
		view.startAnimation(animation);
	}

	// �?出动�?
	private void baseOut(View view, Animation animation, long durationMillis,
			long delayMillis) {
		setEffect(animation, AccelerateDecelerate, durationMillis, delayMillis);
		animation.setAnimationListener(new MyAnimationListener(view));
		view.startAnimation(animation);
	}

	public void show(View view) {
		view.setVisibility(View.VISIBLE);
	}

	public void hide(View view) {
		view.setVisibility(View.GONE);
	}

	public void transparent(View view) {
		view.setVisibility(View.INVISIBLE);
	}

	/**
	 * 渐入
	 *
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void fadeIn(View view, long durationMillis, long delayMillis) {

		AlphaAnimation animation = new AlphaAnimation(0, 1);
		baseIn(view, animation, durationMillis, delayMillis);

	}

	/**
	 * 渐出
	 *
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void fadeOut(View view, long durationMillis, long delayMillis) {

		AlphaAnimation animation = new AlphaAnimation(1, 0);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 滑入
	 *
	 * @param view
	 * @param direction
	 *            方向
	 * @param durationMillis
	 *            时长
	 * @param delayMillis
	 *            延时
	 */
	public void slideIn(View view, int direction, long durationMillis,
			long delayMillis) {

		TranslateAnimation animation = null;
		switch (direction) {
		case _UP:
			animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 1,
					rela2, 0);
			break;
		case _DOWN:
			animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, -1,
					rela2, 0);
			break;
		case _LEFT:
			animation = new TranslateAnimation(rela2, 1, rela2, 0, rela2, 0,
					rela2, 0);
			break;
		case _RIGHT:
			animation = new TranslateAnimation(rela2, -1, rela2, 0, rela2, 0,
					rela2, 0);
			break;
		default:
			animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 1,
					rela2, 0);
			break;
		}

		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 滑出
	 *
	 * @param view
	 * @param direction
	 *            方向
	 * @param durationMillis
	 *            时长
	 * @param delayMillis
	 *            延时
	 */
	public void slideOut(View view, int direction, long durationMillis,
			long delayMillis) {
		TranslateAnimation animation = null;

		switch (direction) {
		case _UP:
			animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 0,
					rela2, -1);
			break;
		case _DOWN:
			animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 0,
					rela2, 1);
			break;
		case _LEFT:
			animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 0,
					rela2, 0);
			break;
		case _RIGHT:
			animation = new TranslateAnimation(rela2, 0, rela2, 1, rela2, 0,
					rela2, -1);
			break;
		default:
			animation = new TranslateAnimation(rela2, 0, rela2, -1, rela2, 0,
					rela2, -1);
			break;
		}

		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 放大进入
	 *
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void scaleIn(View view, long durationMillis, long delayMillis) {
		ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, rela2, 0.5f,
				rela2, 0.5f);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 缩小�?�?
	 *
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void scaleOut(View view, long durationMillis, long delayMillis) {
		ScaleAnimation animation = new ScaleAnimation(1, 0, 1, 0, rela2, 0.5f,
				rela2, 0.5f);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 扇形进入
	 *
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void rotateIn(View view, long durationMillis, long delayMillis) {
		RotateAnimation animation = new RotateAnimation(-90, 0, rela1, 0,
				rela1, 1);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 扇形�?�?
	 *
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void rotateOut(View view, long durationMillis, long delayMillis) {
		RotateAnimation animation = new RotateAnimation(0, 90, rela1, 0, rela1,
				1);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 旋转进入
	 *
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void scaleRotateIn(View view, long durationMillis, long delayMillis) {
		ScaleAnimation animation1 = new ScaleAnimation(0, 1, 0, 1, rela1, 0.5f,
				rela1, 0.5f);
		RotateAnimation animation2 = new RotateAnimation(0, 360, rela1, 0.5f,
				rela1, 0.5f);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 旋转�?�?
	 *
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void scaleRotateOut(View view, long durationMillis, long delayMillis) {
		ScaleAnimation animation1 = new ScaleAnimation(1, 0, 1, 0, rela1, 0.5f,
				rela1, 0.5f);
		RotateAnimation animation2 = new RotateAnimation(0, 360, rela1, 0.5f,
				rela1, 0.5f);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 滑动渐现
	 *
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void slideFadeIn(View view, int direction, long durationMillis,
			long delayMillis) {

		TranslateAnimation animation1 = null;
		switch (direction) {
		case _UP:
			animation1 = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 1,
					rela2, 0);
			break;
		case _DOWN:
			animation1 = new TranslateAnimation(rela2, 0, rela2, 0, rela2, -1,
					rela2, 0);
			break;
		case _LEFT:
			animation1 = new TranslateAnimation(rela2, 1, rela2, 0, rela2, 0,
					rela2, 0);
			break;
		case _RIGHT:
			animation1 = new TranslateAnimation(rela2, -1, rela2, 0, rela2, 0,
					rela2, 0);
			break;
		default:
			animation1 = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 1,
					rela2, 0);
			break;
		}

		AlphaAnimation animation2 = new AlphaAnimation(0, 1);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 滑动渐隐
	 *
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void slideFadeOut(View view, int direction, long durationMillis,
			long delayMillis) {

		TranslateAnimation animation1 = null;
		switch (direction) {
		case _UP:
			animation1 = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 1,
					rela2, 0);
			break;
		case _DOWN:
			animation1 = new TranslateAnimation(rela2, 0, rela2, 0, rela2, -1,
					rela2, 0);
			break;
		case _LEFT:
			animation1 = new TranslateAnimation(rela2, 1, rela2, 0, rela2, 0,
					rela2, 0);
			break;
		case _RIGHT:
			animation1 = new TranslateAnimation(rela2, -1, rela2, 0, rela2, 0,
					rela2, 0);
			break;
		default:
			animation1 = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 1,
					rela2, 0);
			break;
		}

		AlphaAnimation animation2 = new AlphaAnimation(1, 0);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseOut(view, animation, durationMillis, delayMillis);
	}
}
