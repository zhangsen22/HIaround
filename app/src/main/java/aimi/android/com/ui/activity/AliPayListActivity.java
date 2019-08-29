package aimi.android.com.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.app.Constants;
import aimi.android.com.presenter.AliPayListPresenter;
import aimi.android.com.presenter.modle.AliPayListModle;
import aimi.android.com.ui.fragment.AliPayListFragment;

public class AliPayListActivity extends BaseActivity {
    private static final String TAG = AliPayListActivity.class.getSimpleName();
    private AliPayListFragment aliPayListFragment;

    public static void startThis(BaseActivity activity) {
        activity.startActivity(new Intent(activity, AliPayListActivity.class));
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
        aliPayListFragment = (AliPayListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (aliPayListFragment == null) {
            aliPayListFragment = AliPayListFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    aliPayListFragment, R.id.contentFrame);
        }
        //初始化presenter
        new AliPayListPresenter(aliPayListFragment, new AliPayListModle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.REQUESTCODE_15){
                aliPayListFragment.onActivityResultF();
            }
        }
    }
}
