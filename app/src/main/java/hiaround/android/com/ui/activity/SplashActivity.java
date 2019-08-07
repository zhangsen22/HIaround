package hiaround.android.com.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.growalong.util.util.ActivityUtils;
import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.app.AppManager;
import hiaround.android.com.presenter.LoginPresenter;
import hiaround.android.com.presenter.modle.LoginModle;
import hiaround.android.com.ui.fragment.SplashFragment;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_content;
    }

    @Override
    protected void initView(View mRootView) {
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {

                        } else {
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void initData() {
        SplashFragment splashFragment = (SplashFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (splashFragment == null) {
            splashFragment = SplashFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    splashFragment, R.id.contentFrame);
        }
        //初始化presenter
        new LoginPresenter(splashFragment, new LoginModle());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //退出程序
            AppManager.getInstance().appExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
