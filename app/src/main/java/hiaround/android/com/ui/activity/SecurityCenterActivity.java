package hiaround.android.com.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.growalong.util.util.ActivityUtils;

import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.ui.fragment.SecurityCenterFragment;

public class SecurityCenterActivity extends BaseActivity {
    private static final String TAG = SecurityCenterActivity.class.getSimpleName();
    public static void startThis(BaseActivity activity) {
        activity.startActivity(new Intent(activity, SecurityCenterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        SecurityCenterFragment securityCenterFragment = (SecurityCenterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (securityCenterFragment == null) {
            securityCenterFragment = SecurityCenterFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    securityCenterFragment, R.id.contentFrame);
        }
    }
}
