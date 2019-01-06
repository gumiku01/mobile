package com.example.zhanyuan.finalapp.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityManagerUtil {
    /** save activity on top */
    public static Activity mForegroundActivity = null;
    /** save all activies*/
    public static final List<Activity> mActivities = new LinkedList<>();

    /** check there are any activity */
    public static boolean hasActivity() {
        return mActivities.size() > 0;
    }

    /** get activity in foreground  */
    public static Activity getForegroundActivity() {
        return mForegroundActivity;
    }

    /** close all activity , except that passed as parameter */
    public static void finishAll(Class except) {
        List<Activity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<Activity>(mActivities);
        }
        for (Activity activity : copy) {
            if (activity.getClass() != except)
                activity.finish();
        }
    }

    /** close all activity */
    public static void finishAll() {
        List<Activity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<Activity>(mActivities);
        }
        for (Activity activity : copy) {
            activity.finish();
        }
    }
}
