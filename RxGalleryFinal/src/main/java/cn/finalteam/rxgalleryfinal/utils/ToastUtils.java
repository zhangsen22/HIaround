package cn.finalteam.rxgalleryfinal.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by yz on 2018/12/6 9:39 PM
 * Describe: toast封装
 */
public class ToastUtils {
    public static long toastTime = 0;
    public static String lastString = "";
    public static Toast lastToast = null;

    /**
     * @Description: Toast一条消息
     */
    public static void toastMsg(Context context, String sMsg) {
        if (TextUtils.isEmpty(sMsg))
            return;
        Toast tmpToast = Toast.makeText(context, sMsg, Toast.LENGTH_SHORT);

        boolean isShowing = false;
        if (System.currentTimeMillis() - toastTime < 2000)
            isShowing = true;


        if (isShowing) {
            if (!lastString.equals(sMsg)) {
                if (lastToast != null)
                    lastToast.cancel();
                lastToast = tmpToast;
                toastTime = System.currentTimeMillis();
                lastString = sMsg;
                lastToast.show();
            }
        } else {
            if (lastToast != null) {
                lastToast.cancel();
            }
            lastToast = tmpToast;
            toastTime = System.currentTimeMillis();
            lastString = sMsg;
            lastToast.show();
        }
    }

    /**
     * @Description: Toast一条消息
     */
    public static void toastMsg(Context context, int rMsg) {
        toastMsg(context, context.getResources().getString(rMsg));
    }
}
