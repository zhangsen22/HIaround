package aimi.android.com.ui.activity;

import android.content.Intent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.presenter.IdentityPresenter;
import aimi.android.com.presenter.modle.IdentityModle;
import aimi.android.com.ui.fragment.IdentityFragment;

public class IdentityActivity extends BaseActivity {
    private static final String TAG = IdentityActivity.class.getSimpleName();

    public static void startThis(BaseActivity activity,int requestCode) {
        activity.startActivityForResult(new Intent(activity, IdentityActivity.class),requestCode);
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
        IdentityFragment identityFragment = (IdentityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (identityFragment == null) {
            identityFragment = IdentityFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    identityFragment, R.id.contentFrame);
        }
        //初始化presenter
        new IdentityPresenter(identityFragment, new IdentityModle());
    }
}
