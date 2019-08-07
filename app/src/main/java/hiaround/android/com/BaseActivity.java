package hiaround.android.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import com.growalong.util.util.DensityUtil;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.RomUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import butterknife.ButterKnife;
import hiaround.android.com.app.AppManager;
import hiaround.android.com.observer.NetChangeObserver;
import hiaround.android.com.observer.NetworkChangedReceiver;
import hiaround.android.com.ui.widget.LoadingDialog;
import hiaround.android.com.util.CommonFunction;
import hiaround.android.com.util.PhoneInfoUtil;


public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "BaseActivity";
    protected FrameLayout mRootContainer;
    protected View mRootView;
    protected Context mContext;
//    private TextView tv_no_net;//全局的无网络提示
    protected int mStatusBarHeight;//状态栏高度
    public RxPermissions mRxPermissions;

    private volatile boolean isDestroyed = false;
    protected Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (RomUtils.getLightStatusBarAvailableRomType() == RomUtils.AvailableRomType.MIUI) {
            RomUtils.MIUISetStatusBarLightMode(this, true);
        } else if (RomUtils.getLightStatusBarAvailableRomType() == RomUtils.AvailableRomType.FLYME) {
            RomUtils.setFlymeLightStatusBar(this, true);
        } else {
            RomUtils.setAndroidNativeLightStatusBar(this, true);
        }
        mContext = this;
        mRxPermissions = new RxPermissions(this);
        //被系统回收后重启恢复
        if (savedInstanceState != null) {
            GALogger.d(TAG,"savedInstanceState is not null");
            onStateRestore(savedInstanceState);
        }
        //设置根视图
        setContentView(R.layout.activity_base);
        mRootContainer = findViewById(R.id.contentLayout);
//        tv_no_net = (TextView)findViewById(R.id.tv_no_net);
//        tv_no_net.setOnClickListener(this);
        //开启广播去监听 网络 改变事件
        NetworkChangedReceiver.getReceiverInstance().addObserver(mNetChangeObserver);
        //添加业务根视图
        int rootView = getRootView();
        if (rootView > 0) {
            mRootView = getLayoutInflater().inflate(rootView, mRootContainer, false);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            mStatusBarHeight = PhoneInfoUtil.getInstance().getStatusBarHeight();
            GALogger.d(TAG, "statusBarHeight: " + mStatusBarHeight + "  DensityUtil.dip2px(24):" + DensityUtil.dip2px(MyApplication.appContext,24));
            if (mStatusBarHeight <= 0) {//如果没有获取到高度，则默认给 24dp
                mStatusBarHeight = DensityUtil.dip2px(MyApplication.appContext,24);
            }
            params.topMargin = -mStatusBarHeight;
//            root.setPadding(0, statusBarHeight, 0, 0);
            mRootContainer.addView(mRootView);
        }

        //自动绑定视图
        ButterKnife.bind(this);

        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }

        //初始化视图
        initView(mRootView);

        //初始化数据
        initData();

        AppManager.getInstance().addActivity(this);
        GALogger.d(TAG, "onCreate() out");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (isFinishing()) {
            on_Destroy();
        }
        super.onPause();
        GALogger.d(TAG, "onPause() out");
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideLoadingDialog();
        GALogger.d(TAG, "onStop() out");
    }

    @Override
    protected void onDestroy() {
        on_Destroy();//需要在onDestroy方法中进一步检测是否回收资源等。
        super.onDestroy();
    }

    /**
     * 阿里开发规范也要求：
     * 【推荐】不要在 Activity#onDestroy()内执行释放资源的工作，例如一些工作线程的
     * 销毁和停止，因为 onDestroy()执行的时机可能较晚。可根据实际需要，在
     * Activity#onPause()/onStop()中结合 isFinishing()的判断来执行。
     */
    private void on_Destroy() {
        if (isDestroyed) {
            return;
        }

        // 回收资源
        if (mNetChangeObserver != null) {
            NetworkChangedReceiver.getReceiverInstance().removeRegisterObserver(mNetChangeObserver);
        }
        AppManager.getInstance().finishActivity(this);
        hideLoadingDialog();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        GALogger.d(TAG, "onDestroy() out");
        isDestroyed = true;
    }


    /**
     * 返回 Activity 根 View 的 resource ID
     *
     * @return resource ID
     */
    protected abstract int getRootView();

    /**
     * 初始化 Activity 的 View
     * @param mRootView
     */
    protected abstract void initView(View mRootView);

    /**
     * 初始化数据 比如：Intent 传来的数据
     */
    protected abstract void initData();

    /**
     * 布局整体下移 状态栏高度，保持背景不下移
     * @param
     */
    protected void setRootViewPaddingTop() {
        mRootView.setPadding(0, mStatusBarHeight, 0, 0);
    }

    protected void setRootViewPaddingTop(View view) {
        view.setPadding(0, mStatusBarHeight, 0, 0);
    }

    /**
     * 返回桌面
     */
    protected void backToDesktop() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        GALogger.d(TAG, "返回桌面");
    }

    private LoadingDialog customDialog;

    public void showLoadingDialog(){
        if(mHandler!=null && showDialogRunnable!=null){
            mHandler.postDelayed(showDialogRunnable,100);
        }
    }

    private Runnable showDialogRunnable = new Runnable() {
        @Override
        public void run() {
            showDialogNow();
        }
    };

    private void showDialogNow(){
        if(customDialog == null){
            customDialog = new LoadingDialog(this);
        }

        if(customDialog.isShow()){
            return;
        }
        customDialog.show();//显示,显示时页面不可点击,只能点击返回
    }

    public void hideLoadingDialog() {
        if (mHandler != null && showDialogRunnable != null) {
            mHandler.removeCallbacks(showDialogRunnable);
        }
        if (customDialog != null) {
            if (customDialog.isShow()) {
                customDialog.dismiss();
            }
            customDialog = null;
        }
    }

    public LoadingDialog getCustomDialog() {
        if(customDialog == null){
            customDialog = new LoadingDialog(this);
        }
        return customDialog;
    }

    /**
     * 网络观察者
     */
    protected final NetChangeObserver mNetChangeObserver = new NetChangeObserver() {
        @Override
        public void onNetConnected(CommonFunction.NetType type) {
            if (type == CommonFunction.NetType.WIFI) {
                GALogger.d(TAG, "wifi网络已连接");
                onChildNetConnected(type);
            } else if (type == CommonFunction.NetType.TYPE_MOBILE) {
                GALogger.d(TAG, "移动网络已连接");
                onChildNetConnected(type);
            }
//            tv_no_net.setVisibility(View.GONE);
        }

        @Override
        public void onNetDisConnect() {
            GALogger.d(TAG, "网络已经断开");
            onChildNetDisConnect();
//            tv_no_net.setVisibility(View.VISIBLE);
        }
    };

    public void onChildNetConnected(CommonFunction.NetType type) {

    }

    public void onChildNetDisConnect() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }

    protected void onStateRestore(@NonNull Bundle savedInstanceState) {

    }

    @CallSuper
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        GALogger.d(TAG, " onSaveInstanceState");
    }

    @CallSuper
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        GALogger.d(TAG, " onRestoreInstanceState");
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        GALogger.d(TAG, " onSaveInstanceState");
    }
}
