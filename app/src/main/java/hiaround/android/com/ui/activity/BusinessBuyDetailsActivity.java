package hiaround.android.com.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.modle.BuyBusinessResponse;
import hiaround.android.com.presenter.modle.BusinessBuyDetailsModle;
import hiaround.android.com.presenter.BusinessBuyDetailsPresenter;
import hiaround.android.com.ui.fragment.BusinessBuyDetailsFragment;

public class BusinessBuyDetailsActivity extends BaseActivity {
    private static final String TAG = BusinessBuyDetailsActivity.class.getSimpleName();
    private BusinessBuyDetailsFragment businessBuyDetailsFragment;

    public static void startThis(BaseActivity activity, BuyBusinessResponse buyBusinessResponse, double price,double num,int type) {
        Intent intent = new Intent(activity, BusinessBuyDetailsActivity.class);
        intent.putExtra("buyBusinessResponse",buyBusinessResponse);
        intent.putExtra("price",price);
        intent.putExtra("num",num);
        intent.putExtra("type",type);
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
        double price = getIntent().getDoubleExtra("price",0);
        double num = getIntent().getDoubleExtra("num",0);
        int type = getIntent().getIntExtra("type",0);
        businessBuyDetailsFragment = (BusinessBuyDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (businessBuyDetailsFragment == null) {
            businessBuyDetailsFragment = BusinessBuyDetailsFragment.newInstance(buyBusinessResponse,price,num,type);
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
