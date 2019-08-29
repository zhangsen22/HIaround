package aimi.android.com.ui.activity;

import android.content.Intent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import aimi.android.com.BaseActivity;
import aimi.android.com.R;
import aimi.android.com.modle.SellResponse;
import aimi.android.com.presenter.BusinessSellDetailsPresenter;
import aimi.android.com.presenter.modle.BusinessSellDetailsModle;
import aimi.android.com.ui.fragment.BusinessSellDetailsFragment;

public class BusinessSellDetailsActivity extends BaseActivity {
    private static final String TAG = ChangePwdActivity.class.getSimpleName();

    public static void startThis(BaseActivity activity, SellResponse sellResponse,String nickname,int requestCode) {
        Intent intent = new Intent(activity, BusinessSellDetailsActivity.class);
        intent.putExtra("sellResponse",sellResponse);
        intent.putExtra("nickname",nickname);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void startThis(BaseActivity activity, SellResponse sellResponse,String nickname) {
        Intent intent = new Intent(activity, BusinessSellDetailsActivity.class);
        intent.putExtra("sellResponse",sellResponse);
        intent.putExtra("nickname",nickname);
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
        SellResponse sellResponse = getIntent().getParcelableExtra("sellResponse");
        String nickname = getIntent().getStringExtra("nickname");
        BusinessSellDetailsFragment businessSellDetailsFragment = (BusinessSellDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (businessSellDetailsFragment == null) {
            businessSellDetailsFragment = BusinessSellDetailsFragment.newInstance(sellResponse,nickname);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    businessSellDetailsFragment, R.id.contentFrame);
        }
        //初始化presenter
        new BusinessSellDetailsPresenter(businessSellDetailsFragment, new BusinessSellDetailsModle());
    }
}
