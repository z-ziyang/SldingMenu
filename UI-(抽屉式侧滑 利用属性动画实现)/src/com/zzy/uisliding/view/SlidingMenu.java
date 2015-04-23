package com.zzy.uisliding.view;

import com.nineoldandroids.view.ViewHelper;
import com.zzy.uisliding.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {

	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private int mScreenWidth;
	private boolean once;

	private boolean isOpen;
	// dp
	private int mMenuRightPadding = 50;
	private int mMenuWidth;

	public SlidingMenu(Context context) {

		this(context, null);
	}

	public SlidingMenu(Context context, AttributeSet attrs) {

		this(context, attrs, -1);

	}

	// 自定义属性
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 获取我们定义的属性
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = array.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = array.getIndex(i);
			switch (attr) {
				case R.styleable.SlidingMenu_rightPadding :
					// 不存在自定义属性值 就是用，默认的值50
					mMenuRightPadding = array.getDimensionPixelSize(attr,
							(int) TypedValue
									.applyDimension(
											TypedValue.COMPLEX_UNIT_DIP, 50,
											context.getResources()
													.getDisplayMetrics()));
					break;
			}
		}
		array.recycle();
		WindowManager wManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wManager.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 获取子VIEW 并设置其宽度
		if (!once) {
			mWapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	/**
	 * 设置偏移量 设置左边Menu隐藏
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 设置隐藏
			this.scrollTo(mMenuWidth, 0);
		}
	}
	/**
	 * 侧滑实现
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_UP :
				// 隐藏在左边的宽度
				int scrollX = getScrollX();
				if (scrollX >= mMenuWidth / 2) {
					// 缓慢 动画效果隐藏
					this.smoothScrollTo(mMenuWidth, 0);
					isOpen = false;
				} else {
					this.smoothScrollTo(0, 0);
					isOpen = true;
				}
				return true;
		}
		return super.onTouchEvent(ev);
	}
	/**
	 * 打开菜单
	 */
	public void openMenu() {
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}
	/**
	 * 关闭菜单
	 */
	public void closeMenu() {
		if (!isOpen)
			return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;
	}

	/**
	 * 切换菜单
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}
	/**
	 * 滚动发生时
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		// 梯度的值 初始时候 l=mMenuWidth
		float scale = l * 1.0f / mMenuWidth; // 1 ~ 0
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale);
	}

}
