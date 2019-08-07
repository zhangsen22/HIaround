package com.growalong.util.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 作者 : Created by zhangsen on 2019/4/28
 * 邮箱 : zhangsen839705693@163.com
 */
public class TextWatcherUtils implements TextWatcher {
    private static final String TAG = TextWatcherUtils.class.getSimpleName();

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        GALogger.d(TAG,"beforeTextChanged      s       "+s+"       start       "+start+"       count       "+count+"       after      "+after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        GALogger.d(TAG,"onTextChanged      s       "+s+"       start       "+start+"       before       "+before+"       count      "+count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        GALogger.d(TAG,"afterTextChanged      s       "+s.toString());
    }
}
