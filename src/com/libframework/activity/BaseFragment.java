package com.libframework.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.libframework.annotation.ViewUtils;

public abstract class BaseFragment extends Fragment {

	public enum FragmentState { RUNNING, PAUSED, STOPED, VIEW_DESTORYED, DESTROYED }
	public enum LogState { NONE, I, D, W, E }
	
	private static final String TAG = BaseFragment.class.getName();
	private FragmentState mFragmentState = FragmentState.DESTROYED;
	private LogState mLogState = LogState.D;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		log("---------onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		log("---------onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflaterView(inflater, container, savedInstanceState);
		ViewUtils.inject(this, view);
		initWidget(view);
		log("---------onCreateView");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		log("---------onActivityCreated");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		log("---------onStart");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mFragmentState = FragmentState.RUNNING;
		log("---------onResume");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mFragmentState = FragmentState.PAUSED;
		log("---------onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		mFragmentState = FragmentState.STOPED;
		log("---------onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mFragmentState = FragmentState.VIEW_DESTORYED;
		log("---------onDestroyView");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mFragmentState = FragmentState.DESTROYED;
		log("---------onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		log("---------onDetach");
	}

	protected abstract View inflaterView(LayoutInflater inflater,
			ViewGroup container, Bundle bundle);
	
	protected void initWidget(View view) {}
	
	/**
	 * 设置Log状态，默认为Debug（若修改必须在onAttach方法中调用）
	 */
	protected void setLogState(LogState logState) {
		mLogState = logState;
	}
	
	protected FragmentState getFragmentState() {
		return mFragmentState;
	}
	
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
