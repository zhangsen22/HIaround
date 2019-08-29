package aimi.android.com.ui.activity;

import android.content.Intent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.ui.fragment.GuaDanFragment;

public class GuaDanActivity extends BaseActivity {
    private static final String TAG = GuaDanActivity.class.getSimpleName();

    public static void startThis(BaseActivity baseActivity) {
        baseActivity.startActivity(new Intent(baseActivity, GuaDanActivity.class));
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
        GuaDanFragment guaDanFragment = (GuaDanFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (guaDanFragment == null) {
            guaDanFragment = GuaDanFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    guaDanFragment, R.id.contentFrame);
        }
    }
}
