package hiaround.android.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.growalong.util.util.ActivityUtils;
import hiaround.android.com.BaseActivity;
import hiaround.android.com.R;
import hiaround.android.com.modle.MySellOrBuyinfoItem;
import hiaround.android.com.ui.fragment.OrderDetailsFragment;

public class OrderDetailsActivity extends BaseActivity {
    private static final String TAG = OrderDetailsActivity.class.getSimpleName();

    public static void startThis(Context context, MySellOrBuyinfoItem orderDetailsModle) {
        Intent intent = new Intent(context, OrderDetailsActivity.class);
        intent.putExtra("orderDetailsModle",orderDetailsModle);
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
        MySellOrBuyinfoItem orderDetailsModle = getIntent().getParcelableExtra("orderDetailsModle");
        OrderDetailsFragment orderDetailsFragment = (OrderDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (orderDetailsFragment == null) {
            orderDetailsFragment = OrderDetailsFragment.newInstance(orderDetailsModle);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    orderDetailsFragment, R.id.contentFrame);
        }
    }
}
