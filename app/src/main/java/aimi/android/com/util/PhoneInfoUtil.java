package aimi.android.com.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import com.growalong.util.util.DensityUtil;
import com.growalong.util.util.GALogger;

import java.lang.reflect.Field;
import java.util.Locale;

import aimi.android.com.MyApplication;

/**
 * Created by yz on 2018/12/19 11:49 AM
 * Describe:
 */
public class PhoneInfoUtil {
    private static final String TAG = PhoneInfoUtil.class.getSimpleName();

    /**
     * 手机类别
     */
    public static enum PhoneType {
        NONE,    //没找到手机品牌
        Xiaomi,   //小米手机
        Huawei,    //华为手机
        Opple     //opple手机
    }


    private static PhoneInfoUtil sInstance;

    /**
     * 手机型号
     */
    private static String model;

    /**
     * 手机厂商
     */
    private String deviceBrand;


    /**
     * 入网许可证
     */
    private String imei;
    /**
     * 分辨率X
     */
    private int resolutionX = 0;
    /**
     * 分辨率Y
     */
    private int resolutionY = 0;
    /**
     * 版本号
     */
    private String systemVersion;

    /**
     * 获取状态栏高度
     */
    private int statusBarHeight = 0;

    private TelephonyManager telephonyManager;

    private PhoneInfoUtil() {
        telephonyManager = (TelephonyManager) MyApplication.appContext.getSystemService(Activity.TELEPHONY_SERVICE);

    }

    public static PhoneInfoUtil getInstance() {
        if (sInstance == null) {
            synchronized (PhoneInfoUtil.class) {
                if (sInstance == null) {
                    sInstance = new PhoneInfoUtil();
                }
            }

        }
        return sInstance;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public String getSystemVersion() {
        if (TextUtils.isEmpty(systemVersion)) {
            systemVersion = Build.VERSION.RELEASE;
        }
        return systemVersion;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public String getSystemModel() {
        if (TextUtils.isEmpty(model)) {
            model = Build.MODEL;
        }
        return model;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public PhoneType getDeviceBrand() {
        if (TextUtils.isEmpty(deviceBrand)) {
            deviceBrand = Build.BRAND;
            GALogger.d(TAG,"deviceBrand   "+deviceBrand);
        }

        if(!TextUtils.isEmpty(deviceBrand)) {
            if (deviceBrand.equals("Xiaomi")) {
                return PhoneType.Xiaomi;
            } else if (deviceBrand.equals("HUAWEI")) {
                return PhoneType.Huawei;
            } else if (deviceBrand.equals("Opple")) {
                return PhoneType.Opple;
            }
        }

        return PhoneType.NONE;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    public String getIMEI() {
        if (telephonyManager != null) {
            try {
                if (TextUtils.isEmpty(imei)) {
                    imei = telephonyManager.getDeviceId();
                    //如果获取不到自动生成一个
                    if (TextUtils.isEmpty(imei)) {
                        StringBuffer buff = new StringBuffer();
                        buff.append("iaroundid_");
                        String constantChars = "1234567890abcdefghijklmnopqrstuvw";
                        for (int i = 0; i < 32; i++) {
                            int randomChar = (int) (Math.random() * 100);
                            int ch = randomChar % 30;
                            buff.append(constantChars.charAt(ch));
                        }
                        imei = buff.toString().toLowerCase();
                    }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return imei;
    }

    /**
     * 获取屏幕分辨率：宽
     *
     * @param context
     * @return
     */
    public int getScreenPixWidth(Context context) {
        if (resolutionX != 0) {
            return resolutionX;
        }
        DisplayMetrics dm = new DisplayMetrics();
        if (!(context instanceof Activity)) {
            dm = context.getResources().getDisplayMetrics();
            resolutionX = dm.widthPixels;
            return resolutionX;
        }

        WindowManager wm = ((Activity) context).getWindowManager();
        if (wm == null) {
            dm = context.getResources().getDisplayMetrics();
            resolutionX = dm.widthPixels;
            return resolutionX;
        }

        wm.getDefaultDisplay().getMetrics(dm);
        resolutionX = dm.widthPixels;
        return resolutionX;
    }

    /**
     * 获取屏幕分辨率：高
     *
     * @param context
     * @return
     */
    public int getScreenPixHeight(Context context) {
        if (resolutionY != 0) {
            return resolutionY;
        }
        DisplayMetrics dm = new DisplayMetrics();
        if (!(context instanceof Activity)) {
            dm = context.getResources().getDisplayMetrics();
            resolutionY = dm.heightPixels;
            return resolutionY;
        }

        WindowManager wm = ((Activity) context).getWindowManager();
        if (wm == null) {
            dm = context.getResources().getDisplayMetrics();
            resolutionY = dm.heightPixels;
            return resolutionY;
        }

        wm.getDefaultDisplay().getMetrics(dm);
        resolutionY = dm.heightPixels;
        return resolutionY;

    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field f = c.getField("status_bar_height");
                int x = (int) f.get(o);
                statusBarHeight = MyApplication.appContext.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(statusBarHeight <= 0){
            statusBarHeight = DensityUtil.dip2px(MyApplication.appContext,24);
        }
        return statusBarHeight;

    }

    /**
     * 获取底部导航栏的高度
     * @return
     */
    public int getNavigationBarHeight() {
        int resourceId = MyApplication.appContext.getResources().getIdentifier("navigation_bar_height","dimen", "android");
        int height = MyApplication.appContext.getResources().getDimensionPixelSize(resourceId);
        GALogger.d("getNavigationBarHeight", "Navi height:" + height);
        return height;
    }

    //获取是否存在NavigationBar
    public boolean isNavigationBarShow(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y!=size.y;
        }else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if(menu || back) {
                return false;
            }else {
                return true;
            }
        }
    }
    /**
     * (x,y)是否在view的区域内
     *
     * @param view
     * @param x
     * @param y
     * @return false-不在view区域
     */
    public boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();

        if (y >= top && y <= bottom && x >= left && x <= right) {
            return true;
        }
        return false;
    }
}
