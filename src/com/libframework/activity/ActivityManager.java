package com.libframework.activity;

import java.util.Stack;


import android.app.Activity;
import android.content.Context;

final public class ActivityManager {
	
    private static Stack<IActivity> activityStack = new Stack<IActivity>();

    private ActivityManager() {}

    private static class ManagerHolder {
        private static final ActivityManager instance = new ActivityManager();
    }

    public static ActivityManager getInstance() {
        return ManagerHolder.instance;
    }

    public void addActivity(IActivity activity) {
        activityStack.add(activity);
    }
    
    public Stack<IActivity> getActivityStack() {
    	if (activityStack == null) {
    		activityStack = new Stack<IActivity>();
    	}
    	return activityStack;
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                ((Activity) activityStack.get(i)).finish();
            }
        }
        activityStack.clear();
    }

    public void AppExit(Context context) {
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
        }
    }
    
}