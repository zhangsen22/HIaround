package hiaround.android.com.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.app.AccountManager;
import hiaround.android.com.app.AppManager;
import hiaround.android.com.app.Constants;
import hiaround.android.com.presenter.LoginPresenter;
import hiaround.android.com.presenter.modle.LoginModle;
import hiaround.android.com.ui.fragment.LoginFragment;
import hiaround.android.com.util.SharedPreferencesUtils;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    public static void startThis(BaseActivity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
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
        if(AccountManager.getInstance().isLogin()){
            AccountManager.getInstance().logout();
        }
        if(SharedPreferencesUtils.has(Constants.SESSIONID)){
            SharedPreferencesUtils.remove(Constants.SESSIONID);
        }
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    loginFragment, R.id.contentFrame);
        }
        //初始化presenter
        new LoginPresenter(loginFragment, new LoginModle());
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
