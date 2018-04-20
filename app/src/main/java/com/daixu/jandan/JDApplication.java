package com.daixu.jandan;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.daixu.jandan.di.AppComponent;
import com.daixu.jandan.di.DaggerAppComponent;
import com.daixu.jandan.utils.FakeCrashLibrary;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import timber.log.Timber;

public class JDApplication extends DaggerApplication {

    private AppComponent mAppComponent;
    private static JDApplication sApplication;

    private static Stack<WeakReference<AppCompatActivity>> activityStack;

    public static JDApplication getInstance() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        mAppComponent = DaggerAppComponent.builder().application(this).build();
        mAppComponent.inject(this);
        return mAppComponent;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }

    /*******************************************************
     * 说明：Activity入栈
     *
     * @author yangbo
     * @param activity
     ********************************************************/
    public void addActivity(AppCompatActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(new WeakReference<>(activity));
    }

    /**
     * 检查弱引用是否释放，若释放，则从栈中清理掉该元素
     */
    public void checkWeakReference() {
        if (activityStack != null) {
            // 使用迭代器进行安全删除
            for (Iterator<WeakReference<AppCompatActivity>> it = activityStack.iterator(); it.hasNext(); ) {
                WeakReference<AppCompatActivity> activityReference = it.next();
                AppCompatActivity temp = activityReference.get();
                if (temp == null) {
                    it.remove();
                }
            }
        }
    }

    /*******************************************************
     * 说明：取得最后入栈的Activity(当前Activity）
     *
     * @author yangbo
     * @return
     ********************************************************/
    public AppCompatActivity currentActivity() {
        checkWeakReference();
        if (activityStack != null && !activityStack.isEmpty()) {
            return activityStack.lastElement().get();
        }
        return null;
    }

    /*******************************************************
     * 说明：结束最后入栈的Activity（当前Activity）
     *
     * @author yangbo
     ********************************************************/
    public void finishActivity() {
        AppCompatActivity activity = currentActivity();
        if (null != activity) {
            finishActivity(activity);
        }
    }

    /*******************************************************
     * 说明：结束制定对象的Activity
     *
     * @author yangbo
     * @param activity
     ********************************************************/
    public void finishActivity(AppCompatActivity activity) {
        if (activity != null && activityStack != null) {
            for (Iterator<WeakReference<AppCompatActivity>> it = activityStack.iterator(); it.hasNext(); ) {
                WeakReference<AppCompatActivity> activityReference = it.next();
                AppCompatActivity temp = activityReference.get();
                // 清理掉已经释放的activity
                if (temp == null) {
                    it.remove();
                    continue;
                }
                if (temp == activity) {
                    it.remove();
                }
            }
            activity.finish();
        }
    }

    /*******************************************************
     * 说明：结束掉指定类的Activity
     *
     * @author yangbo
     * @param cls
     ********************************************************/
    public void finishActivity(Class<?> cls) {
        if (activityStack != null) {
            // 使用迭代器进行安全删除
            for (Iterator<WeakReference<AppCompatActivity>> it = activityStack.iterator(); it.hasNext(); ) {
                WeakReference<AppCompatActivity> activityReference = it.next();
                AppCompatActivity activity = activityReference.get();
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

    /*******************************************************
     * 说明：结束所有Activity
     *
     * @author yangbo
     ********************************************************/
    public void finishAllActivity() {
        if (activityStack != null) {
            for (WeakReference<AppCompatActivity> activityReference : activityStack) {
                AppCompatActivity activity = activityReference.get();
                if (activity != null) {
                    activity.finish();
                }
            }
            activityStack.clear();
        }
    }

    /*******************************************************
     * 说明：移除制定对象的Activity
     *
     * @author yangbo
     * @param activity
     ********************************************************/
    public void removeActivity(AppCompatActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /*******************************************************
     * 说明：退出应用程序
     *
     * @author yangbo
     ********************************************************/
    public void exitApp() {
        try {
            finishAllActivity();
            // 退出JVM,释放所占内存资源,0表示正常退出
            System.exit(0);
            // 从系统中kill掉应用程序
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            Timber.tag("Dagger2").e(String.format("e.message=%s", e.getMessage()));
        }
    }
}
