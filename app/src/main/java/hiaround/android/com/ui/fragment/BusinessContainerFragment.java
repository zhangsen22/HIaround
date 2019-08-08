package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.ui.activity.MainActivity;
import hiaround.android.com.ui.adapter.BusinessViewPagerAdapter;
import hiaround.android.com.ui.widget.NoScrollViewPager;

public class BusinessContainerFragment extends BaseFragment {
    private static final String TAG = BusinessContainerFragment.class.getSimpleName();
    @BindView(R.id.business_viewPager)
    NoScrollViewPager businessViewPager;
    @BindView(R.id.ff_business_content)
    LinearLayout ffBusinessContent;
    @BindView(R.id.tv_usedprotery)
    TextView tvUsedprotery;
    @BindView(R.id.tv_dongjieprotery)
    TextView tvDongjieprotery;
    @BindView(R.id.ff_business_content1)
    LinearLayout ffBusinessContent1;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_sell)
    TextView tvSell;
    private MainActivity mainActivity;
    private BusinessViewPagerAdapter baseFragmentPagerAdapter;

    public static BusinessContainerFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        BusinessContainerFragment fragment = new BusinessContainerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.business_container_fragment;
    }

    @Override
    protected void initView(View root) {
        GALogger.d(TAG, "BusinessContainerFragment   is    initView");
        setRootViewPaddingTop(ffBusinessContent);
        setRootViewPaddingTop(ffBusinessContent1);
        baseFragmentPagerAdapter = new BusinessViewPagerAdapter(getChildFragmentManager());
        businessViewPager.setAdapter(baseFragmentPagerAdapter);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        setLoadDataWhenVisible();
    }

    @OnClick({R.id.tv_buy, R.id.tv_sell})
    public void onViewClicked(View view) {
        int currentItem = businessViewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.tv_buy:
                if (currentItem != 0) {
                    tvBuy.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_333333));
                    tvBuy.setText("我要买");
                    tvSell.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_999999));
                    tvSell.setText("我要卖");
                    businessViewPager.setCurrentItem(0, false);
                }
                break;
            case R.id.tv_sell:
                if (currentItem != 1) {
                    tvBuy.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_333333));
                    tvBuy.setText("我要卖");
                    tvSell.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_999999));
                    tvSell.setText("我要买");
                    businessViewPager.setCurrentItem(1, false);
                }
                break;
        }
    }
}
