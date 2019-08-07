package hiaround.android.com.ui.activity;

import android.content.Intent;
import android.view.View;

import com.growalong.util.util.ActivityUtils;

import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.presenter.ForgetPwdPresenter;
import hiaround.android.com.presenter.modle.ForgetPwdModle;
import hiaround.android.com.ui.fragment.ForgetPwdFragment;

public class ForgetPwdActivity extends BaseActivity {
    private static final String TAG = ForgetPwdActivity.class.getSimpleName();

    public static void startThis(BaseActivity activity) {
        activity.startActivity(new Intent(activity, ForgetPwdActivity.class));
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_content;
    }

    @Override
    protected void initView(View mRootView) {
    }

    @Override
    protected void initData() {
        ForgetPwdFragment forgetPwdFragment = (ForgetPwdFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (forgetPwdFragment == null) {
            forgetPwdFragment = ForgetPwdFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    forgetPwdFragment, R.id.contentFrame);
        }
        //初始化presenter
        new ForgetPwdPresenter(forgetPwdFragment, new ForgetPwdModle());
    }
}
