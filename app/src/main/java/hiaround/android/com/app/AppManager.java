package hiaround.android.com.app;

import android.app.Activity;

import java.util.Stack;

public class AppManager {
    /**
     * Activity记录栈
     */
    private static Stack<Activity> activityStack;
    /**
     * AppManager单例
     */
    private static AppManager instance;

    /**
     * 单例
     */
    private AppManager() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
    }

    /**
     * 获取AppManager单一实例
     */
    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 返回activity的数量
     *
     * @return
     */
    public static int getActivitySize() {
        if (activityStack != null) {
            return activityStack.size();
        }
        return 0;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack.size() == 0)
            return null;
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 获取Activity（堆栈中第一个压入的）
     */
    public Activity getFirstActivity() {
        return activityStack.firstElement();
    }

    /**
     * 获取指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    public boolean isExistActivity(Class<?> cls) {
        return getActivity(cls) != null;
    }

    public boolean isExistActivity(Activity currentActivity) {
        if (activityStack.search(currentActivity) != -1) {
            return true;
        }
        return false;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity,除了excludeCls
     */
    public void finishAllActivity(Class<?> excludeCls) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                if (excludeCls != null) {
                    if (!activityStack.get(i).getClass().equals(excludeCls)) {
                        activityStack.get(i).finish();
                    }
                } else {
                    activityStack.get(i).finish();
                }
            }
        }
        activityStack.clear();
    }

    /**
     * 返回当前activity的root
     *
     * @param activity
     * @return
     */
    public Activity getRootActivity(Activity activity) {
        Activity parent;
        while ((parent = activity.getParent()) != null) {
            activity = parent;
        }
        return activity;
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity(null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }


}
