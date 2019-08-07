package com.growalong.util.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;


/**
 * Android检查设备是否可以访问互联网，判断Internet连接，测试网络请求，解析域名
 * 安卓SDK提供了ConnectivityManager类，那么我们就可以轻松的获取设备的网络状态以及联网方式等信息。
 * 但是要想知道安卓设备连接的网络能不能访问到Internet，就要费一番周折了。
 * 本文为大家介绍三种方式来检查Internet连接状态。
 *
 * 1、使用Linux系统ping ip的命令方式检查设备的Internet连接状态。
 * 2、使用HttpURLConnection的get请求方式检查设备的Internet连接状态。（可以设置超时时长）
 * 3、使用java.net.InetAddress解析域名的方式检查设备的Internet连接状态。（可以设置超时时长）
 *
 * 本文只为测试网络连接状态使用，用到了三种常用的Internet状态检查方式，如果想在做某些网络操作之前检查Internet是否连通，
 * 建议使用后两种方式，可以自己定义等待响应的时间。我设置的是3秒。如果使用ping IP的方式的话，如果无法访问到Internet，
 * 则需要等待较长的时间。
 */
public class NetWorkUtil {
    private static final String TAG = NetWorkUtil.class.getSimpleName();


    /**
     * 检查WIFI是否连接
     */
    public static boolean isWifiConnected(Context context) {
        // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //判断NetworkInfo对象是否为空 并且类型是否为WIFI
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            return networkInfo.isAvailable();
        return false;

    }

    /**
     * 获取当前网络连接的类型
     *
     * @param context context
     * @return int
     */
    public static String getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (null == connManager) { // 为空则认为无网络
            return "NETWORK_NONE";
        }
        // 获取网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return "NETWORK_NONE";
        }
        // 判断是否为WIFI
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return "NETWORK_WIFI";
                }
            }
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            /*
             GPRS : 2G(2.5) General Packet Radia Service 114kbps
             EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
             UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
             CDMA : 2G 电信 Code Division Multiple Access 码分多址
             EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
             EVDO_A : 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
             1xRTT : 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
             HSDPA : 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
             HSUPA : 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
             HSPA : 3G (分HSDPA,HSUPA) High Speed Packet Access
             IDEN : 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
             EVDO_B : 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
             LTE : 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
             EHRPD : 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
             HSPAP : 3G HSPAP 比 HSDPA 快些
             */
            // 2G网络
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "NETWORK_2G";
            // 3G网络
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "NETWORK_3G";
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "NETWORK_4G";
            default:
                return "NETWORK_MOBILE";
        }
    }

    /**
     * 检查手机网络(4G/3G/2G)是否连接
     */
    public static boolean isMobileNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mobileNetworkInfo != null;
    }

    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /**
     * 保存文字到剪贴板
     *
     * @param context
     * @param text
     */
    public static void copyToClipBoard(Context context, String text) {
        ClipData clipData = ClipData.newPlainText("url", text);
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(clipData);
        Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }



    /** 第一种方式
     * ping IP方式
     *
     * 检查互联网地址是否可以访问
     *
     * @param address  要检查的域名或IP地址
     * @param callback 检查结果回调（是否可以ping通地址）{@see java.lang.Comparable<T>}
     */
    public static void isNetWorkAvailable(final String address, final Comparable<Boolean> callback) {
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (callback != null) {
                    callback.compareTo(msg.arg1 == 0);
                }
            }

        };
        new Thread(new Runnable() {

            @Override
            public void run() {
                Runtime runtime = Runtime.getRuntime();
                Message msg = new Message();
                try {
                    Process pingProcess = runtime.exec("/system/bin/ping -c 1 " + address);
                    InputStreamReader isr = new InputStreamReader(pingProcess.getInputStream());
                    BufferedReader buf = new BufferedReader(isr);
                    if (buf.readLine() == null) {
                        msg.arg1 = -1;
                    } else {
                        msg.arg1 = 0;
                    }
                    buf.close();
                    isr.close();
                } catch (Exception e) {
                    msg.arg1 = -1;
                    e.printStackTrace();
                } finally {
                    runtime.gc();
                    handler.sendMessage(msg);
                }
            }

        }).start();
    }

//    可以使用ping www.baidu.com，进行测试。当然，你也可以ping你们的服务器地址。如下：
//
//    复制代码
//      NetWorkUtils.isNetWorkAvailable("www.baidu.com", new Comparable<Boolean>() {
//
//        @Override
//        public int compareTo(Boolean available) {
//            if (available) {
//                // TODO 设备访问Internet正常
//            } else {
//                // TODO 设备无法访问Internet
//            }
//            return 0;
//        }
//
//    });


    /**
     * 第二种方式
     * get请求方式
     *
     * 检查互联网地址是否可以访问-使用get请求
     *
     * @param urlStr   要检查的url
     * @param callback 检查结果回调（是否可以get请求成功）{@see java.lang.Comparable<T>}
     */
    public static void isNetWorkAvailableOfGet(final String urlStr, final Comparable<Boolean> callback) {
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (callback != null) {
                    callback.compareTo(msg.arg1 == 0);
                }
            }

        };
        new Thread(new Runnable() {

            @Override
            public void run() {
                Message msg = new Message();
                try {
                    Connection conn = new Connection(urlStr);
                    Thread thread = new Thread(conn);
                    thread.start();
                    thread.join(3 * 1000); // 设置等待DNS解析线程响应时间为3秒
                    int resCode = conn.get(); // 获取get请求responseCode
                    msg.arg1 = resCode == 200 ? 0 : -1;
                } catch (Exception e) {
                    msg.arg1 = -1;
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }

        }).start();
    }

    /**
     * HttpURLConnection请求线程
     */
    private static class Connection implements Runnable {
        private String urlStr;
        private int responseCode;

        public Connection(String urlStr) {
            this.urlStr = urlStr;
        }

        public void run() {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                set(conn.getResponseCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void set(int responseCode) {
            this.responseCode = responseCode;
        }

        public synchronized int get() {
            return responseCode;
        }
    }

//    可以请求http://www.baidu.com，进行测试。当然，你也可以写你们的服务器地址。如下：
//
//    复制代码
//NetWorkUtils.isNetWorkAvailableOfGet("http://www.baidu.com", new Comparable<Boolean>() {
//
//        @Override
//        public int compareTo(Boolean available) {
//            if (available) {
//                // TODO 设备访问Internet正常
//            } else {
//                // TODO 设备无法访问Internet
//            }
//            return 0;
//        }
//
//    });



    /**
     * 第三种方式
     * DNS解析方式
     *
     * 检查互联网地址是否可以访问-使用DNS解析
     *
     * @param hostname   要检查的域名或IP
     * @param callback 检查结果回调（是否可以解析成功）{@see java.lang.Comparable<T>}
     */
    public static void isNetWorkAvailableOfDNS(final String hostname, final Comparable<Boolean> callback) {
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (callback != null) {
                    GALogger.d(TAG,"msg.arg1    "+msg.arg1);
                    callback.compareTo(msg.arg1 == 0);
                }
            }

        };
        new Thread(new Runnable() {

            @Override
            public void run() {
                Message msg = new Message();
                try {
                    DNSParse parse = new DNSParse(hostname);
                    Thread thread = new Thread(parse);
                    thread.start();
                    thread.join(3 * 1000); // 设置等待DNS解析线程响应时间为3秒
                    InetAddress resCode = parse.get(); // 获取解析到的IP地址
                    msg.arg1 = resCode == null ? -1 : 0;
                } catch (Exception e) {
                    msg.arg1 = -1;
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    /**
     * DNS解析线程
     */
    private static class DNSParse implements Runnable {
        private String hostname;
        private InetAddress address;

        public DNSParse(String hostname) {
            this.hostname = hostname;
        }

        public void run() {
            try {
                set(InetAddress.getByName(hostname));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void set(InetAddress address) {
            this.address = address;
        }

        public synchronized InetAddress get() {
            return address;
        }
    }


//    可以解析百度www.baidu.com，进行测试。当然，你也可以解析自己的域名。如下：
//
//    复制代码
//NetWorkUtils.isNetWorkAvailableOfDNS("www.baidu.com", new Comparable<Boolean>() {
//
//        @Override
//        public int compareTo(Boolean available) {
//            if (available) {
//                // TODO 设备访问Internet正常
//            } else {
//                // TODO 设备无法访问Internet
//            }
//            return 0;
//        }
//
//    });

}
