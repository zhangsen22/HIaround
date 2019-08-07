package hiaround.android.com.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.ui.fragment.ChangePwdFragment;

public class ChangePwdActivity extends BaseActivity {
    private static final String TAG = ChangePwdActivity.class.getSimpleName();

    public static void startThis(BaseActivity activity) {
        activity.startActivity(new Intent(activity, ChangePwdActivity.class));
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
        ChangePwdFragment changePwdFragment = (ChangePwdFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (changePwdFragment == null) {
            changePwdFragment = ChangePwdFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    changePwdFragment, R.id.contentFrame);
        }
    }
}
