package com.libframework.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.libframework.annotation.ViewUtils;

public abstract class BaseActivity extends Activity implements IBroadcastRegister, IActivity {

	public enum ActivityState { RUNNING, PAUSED, STOPED, DESTROYED }
	public enum LogState { NONE, I, D, W, E }
	
	private static final String TAG = BaseActivity.class.getName();
	private ActivityState mActivityState = ActivityState.DESTROYED;
	private LogState mLogState = LogState.D;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setLayoutView();  // 使用注解注入ContentView时，不用重写此方法
		ViewUtils.inject(this);
		initData();
		initWidget();
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
		super.onResume();
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
		log("---------onDestory");
	}
	
	protected void setLogState(LogState logState) {
		mLogState = logState;
	}
	
	protected ActivityState getActivityState() {
		return mActivityState;
	}

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
