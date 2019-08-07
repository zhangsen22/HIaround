package hiaround.android.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.modle.BuyItem;
import hiaround.android.com.presenter.BusinessSellPresenter;
import hiaround.android.com.presenter.modle.BusinessSellModle;
import hiaround.android.com.ui.fragment.BusinessSellFragment;

public class BusinessSellActivity extends BaseActivity {
    private static final String TAG = BusinessSellActivity.class.getSimpleName();
    public static void startThis(Context context,BuyItem buyItem) {
        Intent intent = new Intent(context, BusinessSellActivity.class);
        intent.putExtra("buyItem",buyItem);
        context.startActivity(intent);
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
        BuyItem buyItem = getIntent().getParcelableExtra("buyItem");
        BusinessSellFragment businessSellFragment = (BusinessSellFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (businessSellFragment == null) {
            businessSellFragment = BusinessSellFragment.newInstance(buyItem);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    businessSellFragment, R.id.contentFrame);
        }
        //初始化presenter
        new BusinessSellPresenter(businessSellFragment, new BusinessSellModle());
    }
}
