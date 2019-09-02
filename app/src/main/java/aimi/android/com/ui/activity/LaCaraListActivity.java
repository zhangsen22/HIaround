package aimi.android.com.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import com.growalong.util.util.ActivityUtils;

import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.app.Constants;
import aimi.android.com.presenter.LaCaraListPresenter;
import aimi.android.com.presenter.modle.LaCaraListModle;
import aimi.android.com.ui.fragment.LaCaraListFragment;

public class LaCaraListActivity extends BaseActivity {
    private static final String TAG = LaCaraListActivity.class.getSimpleName();
    private LaCaraListFragment laCaraListFragment;

    public static void startThis(BaseActivity activity) {
        activity.startActivity(new Intent(activity, LaCaraListActivity.class));
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
        laCaraListFragment = (LaCaraListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (laCaraListFragment == null) {
            laCaraListFragment = LaCaraListFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    laCaraListFragment, R.id.contentFrame);
        }
        //初始化presenter
        new LaCaraListPresenter(laCaraListFragment, new LaCaraListModle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.REQUESTCODE_21){
                laCaraListFragment.onActivityResultF();
            }
        }
    }
}
