package com.growalong.util.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 作者 : Created by zhangsen on 2019/4/29
 * 邮箱 : zhangsen839705693@163.com
 * 软键盘工具类
 */
public class KeyboardUtil {

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param view
     */
    public static void hideInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    /**
     * 显示软键盘
     *
     * @param context
     * @param view
     */
    public static void showInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }
}
