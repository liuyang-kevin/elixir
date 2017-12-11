package com.echoesnet.crowdfunding;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.List;


public class ElixirApplication extends Application {
    private static ElixirApplication mApplication;

    public static ElixirApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

//        try {
//            new InputStream[] {getAssets().open("file.crt")};
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String processName = getProcessName(this, android.os.Process.myPid());
//        if (processName != null) {
//            boolean defaultProcess = processName.equals("");
//            if (defaultProcess) {
//
//            }else {
//
//            }
//
//        }


    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }







    /**
     * @return null may be returned if the specified process not found
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

}
