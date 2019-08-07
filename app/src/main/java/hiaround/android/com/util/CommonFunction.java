package hiaround.android.com.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;

import com.growalong.util.util.DensityUtil;
import com.growalong.util.util.SharedPreferenceUtil;

import hiaround.android.com.MyApplication;
import hiaround.android.com.ui.tourguide.Overlay;
import hiaround.android.com.ui.tourguide.Pointer;
import hiaround.android.com.ui.tourguide.TourGuide;

/**
 * 作者: created by zhangsen on 2019/1/9
 * 邮箱: zhangsen839705693@163.com
 */
public class CommonFunction {

    private TourGuide mTutorialHandler;

    public static enum NetType {
        WIFI,//wifi
        TYPE_MOBILE,//手机网络
        //        CMNET,//手机网络
//        CMWAP,
        NONE// 无网络
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static NetType getAPNType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetType.NONE;//网络不可用
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {//
            return NetType.TYPE_MOBILE;
//            if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals("cmnet")) {
//                return NetType.CMNET;
//            } else {
//                return NetType.CMWAP;
//            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;//wifi 网络
        }
        return NetType.NONE;
    }

    /**
     * 判断字符串是否为空字符串、null或“null”字符串包括所有大小写情况
     *
     * @param str
     * @return 是否为空
     */
    public static boolean isEmptyOrNullStr(String str) {
        return TextUtils.isEmpty(str) || "".equals(str) || "null".equals(str);
    }

    public void showTip(final View tipCoverView, final Activity context, final String sharedPreStr, final String tipText, final int position) {
        MyApplication.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                boolean showTip = SharedPreferenceUtil.getInstance(context).getBoolean(sharedPreStr, true);

                if (showTip && tipCoverView.getVisibility() == View.VISIBLE) {
                    SharedPreferenceUtil.getInstance(context).setBoolean(sharedPreStr, false);
                    int w = DensityUtil.dip2px(context,200);
                    mTutorialHandler = TourGuide.init(context).with(TourGuide.Technique.Click)
                            .setPointer(new Pointer())
                            .setToolTipShadow(false)
                            .setToolTipGravity(position)
                            .setToolTipDescription(tipText)
                            .setToolTipsetOverlayWidth(w)
                            .overlayDisableClick(false)
                            .overlayDisableClickThroughHole(false)
                            .setOverlayStyle(Overlay.Style.Circle)
                            .setOverlayOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mTutorialHandler != null)
                                        mTutorialHandler.cleanUp();
                                    mTutorialHandler = null;
                                }
                            })
                            .playOn(tipCoverView);
                }
            }
        }, 200);
    }

}
