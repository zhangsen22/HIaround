package aimi.android.com.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.growalong.util.util.ActivityUtils;

import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.app.Constants;
import aimi.android.com.presenter.YunShanFuListPresenter;
import aimi.android.com.presenter.modle.YunShanFuListModle;
import aimi.android.com.ui.fragment.YunShanFuListFragment;

public class YunShanFuListActivity extends BaseActivity {
    private static final String TAG = YunShanFuListActivity.class.getSimpleName();
    private YunShanFuListFragment yunShanFuListFragment;

    public static void startThis(BaseActivity activity) {
        activity.startActivity(new Intent(activity, YunShanFuListActivity.class));
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
        yunShanFuListFragment = (YunShanFuListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (yunShanFuListFragment == null) {
            yunShanFuListFragment = YunShanFuListFragment.newInstance("");
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    yunShanFuListFragment, R.id.contentFrame);
        }
        //初始化presenter
        new YunShanFuListPresenter(yunShanFuListFragment, new YunShanFuListModle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.REQUESTCODE_19){
                yunShanFuListFragment.onActivityResultF();
            }
        }
    }
}
