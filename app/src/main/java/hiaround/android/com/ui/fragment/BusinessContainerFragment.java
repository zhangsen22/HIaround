package hiaround.android.com.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.DensityUtil;
import com.growalong.util.util.GALogger;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import java.text.DecimalFormat;
import butterknife.BindView;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.modle.WalletResponse;
import hiaround.android.com.presenter.BusinessContainerPresenter;
import hiaround.android.com.presenter.contract.BusinessContainerContract;
import hiaround.android.com.presenter.modle.BusinessContainerModle;
import hiaround.android.com.ui.activity.MainActivity;
import hiaround.android.com.ui.adapter.BusinessViewPagerAdapter;
import hiaround.android.com.ui.widget.NoScrollViewPager;

public class BusinessContainerFragment extends BaseFragment implements BusinessContainerContract.View {
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
    @BindView(R.id.gusadan_magicindicator)
    MagicIndicator gusadanMagicindicator;
    private MainActivity mainActivity;
    private BusinessViewPagerAdapter baseFragmentPagerAdapter;
    private BusinessContainerPresenter presenter;

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
        final String[] guadanTitle = mainActivity.getResources().getStringArray(R.array.guadan_t1itle);
        businessViewPager.setOffscreenPageLimit(guadanTitle.length - 1);
        baseFragmentPagerAdapter = new BusinessViewPagerAdapter(getChildFragmentManager(),guadanTitle);
        businessViewPager.setAdapter(baseFragmentPagerAdapter);
        CommonNavigator commonNavigator = new CommonNavigator(mainActivity);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return guadanTitle.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#ff666666"));
                colorTransitionPagerTitleView.setTextSize(14);
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#FF3B51FF"));
                colorTransitionPagerTitleView.setText(guadanTitle[index]);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentItem = businessViewPager.getCurrentItem();
                        if (currentItem != index) {
                            businessViewPager.setCurrentItem(index);
                        } else {

                        }
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(DensityUtil.dip2px(MyApplication.appContext, 2));
                indicator.setLineWidth(UIUtil.dip2px(context, 25));
                indicator.setColors(Color.parseColor("#FF3B51FF"));
                indicator.setYOffset(UIUtil.dip2px(context, 8));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                return indicator;
            }
        });
        gusadanMagicindicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(gusadanMagicindicator, businessViewPager);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        setLoadDataWhenVisible();
        new BusinessContainerPresenter(this, new BusinessContainerModle());
        MyApplication.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                presenter.getInfo();
            }
        },1000);
    }

    @Override
    public void getInfoSuccess(WalletResponse walletResponse) {
        if (walletResponse != null) {
            tvUsedprotery.setText(new DecimalFormat("0.000").format(walletResponse.getHotNum()));
            tvDongjieprotery.setText(new DecimalFormat("0.000").format(walletResponse.getHotFreezeNum()));
        }
    }

    @Override
    public void setPresenter(BusinessContainerContract.Presenter presenter) {
        this.presenter = (BusinessContainerPresenter) presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
