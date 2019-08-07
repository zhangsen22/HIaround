package com.growalong.util.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yz on 2018/12/21 6:24 PM
 * Describe: 公用工具类
 */
public class CommonTools {

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

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后八位任意数(暂时不校验手机号)
     *
     * @param str 手机号
     * @return
     */
    public static boolean isChinaPhoneLegal(String str) {
        try {

            String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(14[5-9])|(166)|(198))\\d{8}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(str);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
