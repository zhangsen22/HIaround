package aimi.android.com.app;
import aimi.android.com.MyApplication;
import aimi.android.com.util.FileUtils;
/**
 * 静态变量类
 */
public class Constants {
    //二维码图片保存本地的路径
    public static final String FILTER_IMAGE_PATH = FileUtils.getFilterImageDir() + "filter_image.jpg";
    public static final String FILTERCHONGBI_IMAGE_PATH = FileUtils.getFilterImageDir() + "chongbi_filter_image.jpg";
    public static final String HOWGETALIPAYID = MyApplication.getH5_Address()+"aliid.htm";//如何获取支付宝id
    public static final String NOTIFYCLICK = MyApplication.getH5_Address()+"msg.html";
    public static final String USERXIEYI = MyApplication.getH5_Address()+"userpro.html";
    public static final String KEFUANDHELP = MyApplication.getH5_Address()+"help.html";
    public static final String WEBCHATTG = MyApplication.getH5_Address()+"service.html";//微信和tg按钮
    public static final String SESSIONID = "sessionid";
    public static final String USDTPRICE = "Usdt_Price";
    public static final String VERSION = "version";
    public static final String WXPAYLOCK = "wxPayLock";
    public static final String YUNSHANFUURL ="https://user.95516.com/pages/wap/login.html?sysIdStr=N7Tlq35lwXxFfzr&service=https://ctq.95516.com/web/kfmm_n/html/indexQuery.html?channelNo=02";//云闪付登录页面url
    public static final String YUNSHANFUURLLOGIN = "https://user.95516.com/pages/login/setcookie_frame.html";//云闪付登录的url
    public static final String YUNSHANFULOGINSUCCESS = "https://ctq.95516.com/web/kfmm_n/html/indexQuery.html";//云闪付登录成功的url
    /**
     * android 8.0以上通知栏渠道ID
     */
    public static final String NOTIFICATION_CHANNEL_DOWNLOAD = "bingo_channel_02";//下载安装包进度通知栏 渠道ID


    public static final int REQUESTCODE_10 = 10;
    public static final int REQUESTCODE_11 = 11;
    public static final int REQUESTCODE_12 = 12;
    public static final int REQUESTCODE_13 = 13;
    public static final int REQUESTCODE_14 = 14;
    public static final int REQUESTCODE_15 = 15;
    public static final int REQUESTCODE_16 = 16;
    public static final int REQUESTCODE_17 = 17;
    public static final int REQUESTCODE_18 = 18;
    public static final int REQUESTCODE_19 = 19;
    public static final int REQUESTCODE_20 = 20;
    public static final int REQUESTCODE_21 = 21;//拉卡拉
    public static final int RECYCLEVIEW_TOTALCOUNT = 10;
    public static final int Dialog_General = 0X0;
    public static final int Dialog_HorizontalProgress = 0X1;
    public static final int Dialog_VerticalProgress = 0X2;
}
