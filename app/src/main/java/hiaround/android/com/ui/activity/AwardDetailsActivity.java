package hiaround.android.com.ui.activity;

import android.content.Intent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.presenter.AwardDetailsPresenter;
import hiaround.android.com.presenter.modle.AwardDetailsModle;
import hiaround.android.com.ui.fragment.AwardDetailsFragment;

public class AwardDetailsActivity extends BaseActivity {
    private static final String TAG = AwardDetailsActivity.class.getSimpleName();

    public static void startThis(BaseActivity baseActivity) {
        baseActivity.startActivity(new Intent(baseActivity, AwardDetailsActivity.class));
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
        AwardDetailsFragment awardDetailsFragment = (AwardDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (awardDetailsFragment == null) {
            awardDetailsFragment = AwardDetailsFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    awardDetailsFragment, R.id.contentFrame);
        }
        //初始化presenter
        new AwardDetailsPresenter(awardDetailsFragment, new AwardDetailsModle());
    }
}
