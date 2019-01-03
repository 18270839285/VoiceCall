package huidu.com.voicecall.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * 作者:zzj
 * 时间:2017/11/10
 * 描述:AtyContainer 管理Activity
 */

public class AtyContainer {
    private AtyContainer() {
    }

    private static AtyContainer instance = new AtyContainer();
    private static ArrayDeque<Activity> activityStack = new ArrayDeque<>();

    public static AtyContainer getInstance() {
        return instance;
    }

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            iterator.remove();
            activity.finish();
        }
        activityStack.clear();
    }

    public void finishActivity(Class<?> cls) {
        if (activityStack != null) {
            // 使用迭代器安全删除
            for (Iterator<Activity> it = activityStack.iterator(); it.hasNext(); ) {
                Activity activity = it.next();
                // 清理掉已经释放的activity
                if (activity == null) {
                    it.remove();
                    continue;
                }
                if (activity.getClass().equals(cls)) {
                    it.remove();
                    activity.finish();
                }
            }
        }
    }

    public Activity currentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.peek();
        return activity;
    }

}
