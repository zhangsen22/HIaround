package hiaround.android.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.growalong.util.util.ActivityUtils;

import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.ui.fragment.ChongBiFragment;

public class ChongBiActivity extends BaseActivity {
    private static final String TAG = ChongBiActivity.class.getSimpleName();

    public static void startThis(Context context) {
        context.startActivity(new Intent(context, ChongBiActivity.class));
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
        ChongBiFragment chongBiFragment = (ChongBiFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (chongBiFragment == null) {
            chongBiFragment = ChongBiFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    chongBiFragment, R.id.contentFrame);
        }
    }
}
