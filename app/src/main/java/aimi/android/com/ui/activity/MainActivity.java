package aimi.android.com.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.growalong.util.util.GALogger;
import com.growalong.util.util.GsonUtil;
import com.growalong.util.util.bean.MessageEvent;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import butterknife.OnClick;
import aimi.android.com.BaseActivity;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.app.AppManager;
import aimi.android.com.app.Constants;
import aimi.android.com.downloads.UpdateAppBuilder;
import aimi.android.com.modle.UsdtPriceResponse;
import aimi.android.com.net.retrofit.ApiConstants;
import aimi.android.com.presenter.MainPresenter;
import aimi.android.com.presenter.contract.MainContract;
import aimi.android.com.presenter.modle.MainModle;
import aimi.android.com.ui.adapter.MainViewPagerAdapter;
import aimi.android.com.ui.fragment.CenterFragment;
import aimi.android.com.ui.fragment.OrderFragment;
import aimi.android.com.ui.widget.NoScrollViewPager;
import aimi.android.com.util.SharedPreferencesUtils;

public class MainActivity extends BaseActivity implements MainContract.View {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.noscrollViewPager)
    NoScrollViewPager noscrollViewPager;
    @BindView(R.id.rb_business)
    RadioButton rbBusiness;
    @BindView(R.id.rb_order)
    RadioButton rbOrder;
    @BindView(R.id.rb_property)
    RadioButton rbProperty;
    @BindView(R.id.rb_center)
    RadioButton rbCenter;
    @BindView(R.id.rg_controal)
    RadioGroup rgControal;

    private MainPresenter mainPresenter;
    private MainViewPagerAdapter mainViewPagerAdapter;

    public static void startThis(BaseActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public static void startThis(BaseActivity activity, int requestCode) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(View mRootView) {
        String hostAddress = MyApplication.getHostAddress();
        GALogger.d(TAG, "hostAddress   " + hostAddress);
    }

    @Override
    protected void initData() {
        updateApp(SharedPreferencesUtils.getString(Constants.VERSION));
        //初始化presenter
        new MainPresenter(this, new MainModle());
        mainPresenter.usdtPrice();
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        noscrollViewPager.setAdapter(mainViewPagerAdapter);
        noscrollViewPager.setOffscreenPageLimit(3);
    }

    @OnClick({R.id.rb_business,R.id.rb_order, R.id.rb_property, R.id.rb_center})
    public void onViewClicked(View view) {
        int currentItem = noscrollViewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.rb_business:
                if (currentItem != 0) {
                    noscrollViewPager.setCurrentItem(0, false);
                }
                break;
            case R.id.rb_order:
                if (currentItem != 1) {
                    noscrollViewPager.setCurrentItem(1, false);
                }
                break;
            case R.id.rb_property:
                if (currentItem != 2) {
                    noscrollViewPager.setCurrentItem(2, false);
                }
                break;
            case R.id.rb_center:
                if (currentItem != 3) {
                    noscrollViewPager.setCurrentItem(3, false);
                }
                break;
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mainPresenter = (MainPresenter) presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onStart() {
        GALogger.d(TAG,TAG+"    is    onStart");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        GALogger.d(TAG,TAG+"    is    onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        GALogger.d(TAG, "onNewIntent");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        GALogger.d(TAG, "onActivityResult");
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUESTCODE_10) {
                CenterFragment centerFragment = mainViewPagerAdapter.getCenterFragment();
                if (centerFragment != null) {
                    centerFragment.onActivityResultCenter(requestCode);
                }
            } else if (requestCode == Constants.REQUESTCODE_11) {
//                PropertyFragment propertyFragment = mainViewPagerAdapter.getPropertyFragment();
//                if (propertyFragment != null) {
//                    propertyFragment.onActivityResultProperty(requestCode);
//                }
            } else if (requestCode == Constants.REQUESTCODE_12 || requestCode == Constants.REQUESTCODE_13 || requestCode == Constants.REQUESTCODE_14 || requestCode == Constants.REQUESTCODE_18) {
                OrderFragment orderFragment = mainViewPagerAdapter.getOrderFragment();
                if (orderFragment != null) {
                    orderFragment.onActivityResultOrder(requestCode);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        int type = event.getType();
        GALogger.d(TAG, "type    is" + type);
        switch (type) {
            case 1:
                noscrollViewPager.setCurrentItem(1, false);
                changeRadioButton(1);
                onActivityResult(Constants.REQUESTCODE_13, RESULT_OK, null);
                break;
            case 2:
                noscrollViewPager.setCurrentItem(1, false);
                changeRadioButton(1);
                onActivityResult(Constants.REQUESTCODE_14, RESULT_OK, null);
                break;
            case 3:
                noscrollViewPager.setCurrentItem(1, false);
                changeRadioButton(1);
                onActivityResult(Constants.REQUESTCODE_18, RESULT_OK, null);
                break;
        }
    }

    @Override
    public void usdtPriceSuccess(UsdtPriceResponse usdtPriceResponse) {
        if (usdtPriceResponse != null) {
            SharedPreferencesUtils.putString(Constants.USDTPRICE, GsonUtil.getInstance().objTojson(usdtPriceResponse));
        }
    }

    @Override
    public void usdtPriceError() {
//        UsdtPriceResponse mUsdtPriceResponse = new UsdtPriceResponse(6.90, 6.90);
//        SharedPreferencesUtils.putString(Constants.USDTPRICE, GsonUtil.getInstance().objTojson(mUsdtPriceResponse));
    }

    private void updateApp(String version) {
        String h5_down_address = MyApplication.getDown_Address();
        if(TextUtils.isEmpty(h5_down_address)){
            return;
        }

        if(TextUtils.isEmpty(version)){
            return;
        }

        UpdateAppBuilder.with(MainActivity.this).
                apkPath(h5_down_address+ ApiConstants.DOWNLOADAPK).//apk 的下载路径
                isForce(true).//是否需要强制升级
                serverVersionName(version)//服务的版本(会与当前应用的版本号进行比较)
                .updateInfo("有新的版本发布啦！赶紧下载体验")//升级版本信息
                .isWifiRequired(true)
                .start();//开始下载
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            initExetTiShi();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initExetTiShi() {
        //带确认和取消按钮的弹窗
        new XPopup.Builder(this)
//                         .dismissOnTouchOutside(false)
                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                .setPopupCallback(new XPopupCallback() {
                    @Override
                    public void onShow() {
                        Log.e("tag", "onShow");
                    }

                    @Override
                    public void onDismiss() {
                        Log.e("tag", "onDismiss");
                    }
                }).asConfirm("提示", "确认退出吗?",
                "否", "是",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        //退出程序
                        AppManager.getInstance().appExit();
                    }
                }, null, false)
                .show();
    }

    private void changeRadioButton(int position){
        int childCount = rgControal.getChildCount();
        if(childCount > 0){
            for (int i = 0; i < childCount; i++) {
                RadioButton childAt = (RadioButton) rgControal.getChildAt(i);
                if(i == position){
                    childAt.setChecked(true);
                }else {
                    childAt.setChecked(false);
                }
            }
        }
    }
}
