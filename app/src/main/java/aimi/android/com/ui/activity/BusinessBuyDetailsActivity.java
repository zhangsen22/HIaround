package aimi.android.com.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.modle.BuyBusinessResponse;
import aimi.android.com.presenter.modle.BusinessBuyDetailsModle;
import aimi.android.com.presenter.BusinessBuyDetailsPresenter;
import aimi.android.com.ui.fragment.BusinessBuyDetailsFragment;

public class BusinessBuyDetailsActivity extends BaseActivity {
    private static final String TAG = BusinessBuyDetailsActivity.class.getSimpleName();
    private BusinessBuyDetailsFragment businessBuyDetailsFragment;

    public static void startThis(BaseActivity activity, BuyBusinessResponse buyBusinessResponse) {
        Intent intent = new Intent(activity, BusinessBuyDetailsActivity.class);
        intent.putExtra("buyBusinessResponse",buyBusinessResponse);
        activity.startActivity(intent);
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
        BuyBusinessResponse buyBusinessResponse = getIntent().getParcelableExtra("buyBusinessResponse");
        businessBuyDetailsFragment = (BusinessBuyDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (businessBuyDetailsFragment == null) {
            businessBuyDetailsFragment = BusinessBuyDetailsFragment.newInstance(buyBusinessResponse);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    businessBuyDetailsFragment, R.id.contentFrame);
        }
        //初始化presenter
        new BusinessBuyDetailsPresenter(businessBuyDetailsFragment, new BusinessBuyDetailsModle());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            businessBuyDetailsFragment.onKeyDownF();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
