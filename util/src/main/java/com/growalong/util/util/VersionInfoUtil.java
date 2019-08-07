package com.growalong.util.util;

import android.content.Context;

/**
 * Created by gangqing on 2016/7/25.
 */
public class VersionInfoUtil {

    public static final String getVersionName(Context context){
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }
    
    public static final int getVersionCode(Context context){
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}
