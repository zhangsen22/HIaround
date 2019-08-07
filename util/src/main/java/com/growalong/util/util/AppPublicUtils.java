package com.growalong.util.util;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;

public class AppPublicUtils {
    private static final String TAG = AppPublicUtils.class.getSimpleName();

    /**
     * 大家都知道，版本号一般由以下几部分组成：
     *
     * 1.  主版本号
     * 2.  次版本号
     * 3.  修正版本号
     * 4.  编译版本号
     * 例如：2.1.3 ，3.7.5，10.2.0
     *
     * 在比较版本号时，正确的做法应该是，主版本号和主版本号比较，次版本号和次版本号比较等等，也就是把版本号分割，对应的组成之间进行比较
     *
     * 版本号比较  version1:当前项目的versionName   version2:服务器返回的versionName
     * 结果说明：0代表相等，1代表version1大于version2，-1代表version1小于version2
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        GALogger.d(TAG, "version1Array=="+version1Array.length);
        GALogger.d(TAG, "version2Array=="+version2Array.length);
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        GALogger.d(TAG, "verTag2=2222="+version1Array[index]);
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    /**
     * 显示与隐藏状态栏的代码如下
     * getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN) //隐藏状态栏
     * getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN) //显示状态栏
     * @param enable
     */
    public static void fullscreen(Activity activity,boolean enable) {

    if (enable) { //隐藏状态栏

      WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

 lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;

        activity.getWindow().setAttributes(lp);

        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

 } else { //显示状态栏

 WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

 lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activity.getWindow().setAttributes(lp);

        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

 }

 }

    /**
     * 设置EditText是否可编辑
     * @param editText
     * @param mode true:可编辑   false:不可编辑
     */
 public static void setEditTextEnable(EditText editText,boolean mode){
        editText.setFocusable(mode);
        editText.setFocusableInTouchMode(mode);
        editText.setLongClickable(mode);
        editText.setInputType(mode ? InputType.TYPE_CLASS_TEXT:InputType.TYPE_NULL);
 }
}
