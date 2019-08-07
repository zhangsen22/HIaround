package hiaround.android.com.app;
import hiaround.android.com.MyApplication;
import hiaround.android.com.util.FileUtils;
/**
 * 静态变量类
 */
public class Constants {
    //二维码图片保存本地的路径
    public static final String FILTER_IMAGE_PATH = FileUtils.getFilterImageDir() + "filter_image.jpg";
    public static final String FILTERCHONGBI_IMAGE_PATH = FileUtils.getFilterImageDir() + "chongbi_filter_image.jpg";
    public static final String HOWGETALIPAYID = MyApplication.getH5_down_Address()+"aliid.htm";//如何获取支付宝id
    public static final String NOTIFYCLICK = MyApplication.getH5_down_Address()+"msg.html";
    public static final String USERXIEYI = MyApplication.getH5_down_Address()+"userpro.html";
    public static final String KEFUANDHELP = MyApplication.getH5_down_Address()+"help.html";
    public static final String WEBCHATTG = MyApplication.getH5_down_Address()+"service.html";//微信和tg按钮
    public static final String SESSIONID = "sessionid";
    public static final String USDTPRICE = "Usdt_Price";
    public static final String VERSION = "version";
    public static final String WXPAYLOCK = "wxPayLock";
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
    public static final int RECYCLEVIEW_TOTALCOUNT = 10;
}
