package com.libframework.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.libframework.annotation.ViewUtils;

public abstract class BaseFragmentActivity extends FragmentActivity
		implements IBroadcastRegister, IActivity {

	public enum ActivityState { RUNNING, PAUSED, STOPED, DESTROYED }
	public enum LogState { NONE, I, D, W, E }
	
	private static final String TAG = BaseFragmentActivity.class.getName();
	private ActivityState mActivityState = ActivityState.DESTROYED;
	private LogState mLogState = LogState.D;
	private BaseFragment mCurrFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setLayoutView();  // 使用注解注入ContentView时，不用重写此方法
		ViewUtils.inject(this);
		initData();
		initWidget();
		initFragment(savedInstanceState);
		registerBroadcast();
		log("---------onCreate");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		log("---------onStart");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mActivityState = ActivityState.RUNNING;
		log("---------onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		mActivityState = ActivityState.PAUSED;
		log("---------onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		mActivityState = ActivityState.STOPED;
		log("---------onStop");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		log("---------onRestart");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mActivityState = ActivityState.DESTROYED;
		unRegisterBroadcast();
		log("---------onDestroy");
	}

	/**
	 * 设置Log状态，默认为Debug（若需修改必须在initData方法中调用）
	 */
	protected void setLogState(LogState logState) {
		mLogState = logState;
	}
	
	protected ActivityState getActivityState() {
		return mActivityState;
	}
	
	protected void addFragment(int containerViewId, BaseFragment fragment, String tag, Bundle argument) {
		if (null != argument) {
			fragment.setArguments(argument);
		}
		
		if (!fragment.isAdded()) {
			getSupportFragmentManager()
				.beginTransaction()
				.add(containerViewId, fragment, tag)
				.commit();
			
			mCurrFragment = fragment;
		}
	}
	
	protected void changeFragment(int containerViewId, BaseFragment fragment, String tag, Bundle argument) {
		if (fragment.equals(mCurrFragment)) {
			return;
		}
		if (null != argument) {
			fragment.setArguments(argument);
		}
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (!fragment.isAdded()) {
			transaction.add(containerViewId, fragment, tag);
		}
		if (fragment.isHidden()) {
			transaction.show(fragment);
		}
		if (null != mCurrFragment && mCurrFragment.isVisible()) {
			transaction.hide(mCurrFragment);
		}
		mCurrFragment = fragment;
		transaction.commit();
	}
	
	protected void replaceFragment(int containerViewId, BaseFragment fragment, String tag, Bundle argument) {
		if (fragment.equals(mCurrFragment)) {
			return;
		}
		if (null != argument) {
			fragment.setArguments(argument);
		}

		getSupportFragmentManager()
			.beginTransaction()
			.replace(containerViewId, fragment, tag)
			.commit();
	}
	
	protected void initFragment(Bundle savedInstanceState) {}
	
	@Override
	public void setLayoutView() {}
	
	@Override
	public void initData() {}

	@Override
	public void initWidget() {}

	@Override
	public void registerBroadcast() {}

	@Override
	public void unRegisterBroadcast() {}
	
	private void log(String message) {
		switch (mLogState) {
		case NONE:
			break;
		case I:
			Log.i(TAG, message);
			break;
		case D:
			Log.d(TAG, message);
			break;
		case W:
			Log.w(TAG, message);
			break;
		case E:
			Log.e(TAG, message);
			break;
		default:
			break;
		}
	}

}
