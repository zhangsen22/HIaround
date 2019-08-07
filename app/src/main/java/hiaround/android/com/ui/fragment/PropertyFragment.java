package hiaround.android.com.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.DensityUtil;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.GsonUtil;
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
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.UsdtPriceResponse;
import hiaround.android.com.modle.WalletResponse;
import hiaround.android.com.presenter.PropertyPresenter;
import hiaround.android.com.presenter.contract.PropertyContract;
import hiaround.android.com.presenter.modle.PropertyModle;
import hiaround.android.com.ui.activity.ChongBiActivity;
import hiaround.android.com.ui.activity.FinancialRecordsActivity;
import hiaround.android.com.ui.activity.MainActivity;
import hiaround.android.com.ui.activity.RansferOfFundsActivity;
import hiaround.android.com.ui.activity.TiBiActivity;
import hiaround.android.com.ui.adapter.PropertyViewPagerAdapter;
import hiaround.android.com.util.SharedPreferencesUtils;

public class PropertyFragment extends BaseFragment implements ViewPager.OnPageChangeListener, PropertyContract.View {
    private static final String TAG = PropertyFragment.class.getSimpleName();

    @BindView(R.id.property_magicindicator)
    MagicIndicator propertyMagicindicator;
    @BindView(R.id.property_viewPager)
    ViewPager propertyViewPager;
    @BindView(R.id.ll_property_bg)
    LinearLayout llPropertyBg;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.iv_financial_records)
    ImageView ivFinancialRecords;
    @BindView(R.id.tv_account_sp)
    TextView tvAccountSp;
    @BindView(R.id.tv_account_money1)
    TextView tvAccountMoney1;
    @BindView(R.id.tv_account_money2)
    TextView tvAccountMoney2;
    @BindView(R.id.tv_account_tibi)
    TextView tvAccountTibi;
    @BindView(R.id.tv_account_chongbi)
    TextView tvAccountChongbi;
    @BindView(R.id.tv_account_zjhz)
    TextView tvAccountZjhz;
    @BindView(R.id.tv_bi_type)
    TextView tvBiType;
    private PropertyViewPagerAdapter propertyViewPagerAdapter;
    private PropertyPresenter propertyPresenter;
    private MainActivity mainActivity;
    private WalletResponse mWalletResponse = null;
    private int goPosition = 1;

    public static PropertyFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        PropertyFragment fragment = new PropertyFragment();
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
        return R.layout.property_fragment;
    }

    @Override
    protected void initView(View root) {
        GALogger.d(TAG, "PropertyFragment   is    initView");
        setRootViewPaddingTop(llPropertyBg);
        final String[] propertyTitle = mainActivity.getResources().getStringArray(R.array.property_title);
        propertyViewPager.setOffscreenPageLimit(propertyTitle.length - 1);
        propertyViewPagerAdapter = new PropertyViewPagerAdapter(getChildFragmentManager(), propertyTitle);
        propertyViewPager.setAdapter(propertyViewPagerAdapter);
        CommonNavigator commonNavigator = new CommonNavigator(mainActivity);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return propertyTitle.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#333333"));
                colorTransitionPagerTitleView.setTextSize(14);
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#FF5100"));
                colorTransitionPagerTitleView.setText(propertyTitle[index]);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentItem = propertyViewPager.getCurrentItem();
                        if (currentItem != index) {
                            propertyViewPager.setCurrentItem(index);
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
                indicator.setColors(Color.parseColor("#FF5100"));
                indicator.setYOffset(UIUtil.dip2px(context, 8));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                return indicator;
            }
        });

        propertyMagicindicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(propertyMagicindicator, propertyViewPager);
        // do this in a runnable to make sure the viewPager's views are already instantiated before triggering the onPageSelected call
        propertyViewPager.addOnPageChangeListener(this);
        /**
         * 此代码解决进来不调用onPageSelected
         */
//        propertyViewPager.post(new Runnable() {
//            @Override
//            public void run() {
//                onPageSelected(propertyViewPager.getCurrentItem());
//            }
//        });
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        GALogger.d(TAG, "PropertyFragment   is    lazyLoadData");
        setLoadDataWhenVisible();
        //初始化presenter
        new PropertyPresenter(PropertyFragment.this, new PropertyModle());
        propertyPresenter.getInfo();
//        int currentItem = propertyViewPager.getCurrentItem();
//        if (propertyViewPagerAdapter != null) {
//            BaseFragment currentFragment = propertyViewPagerAdapter.getCurrentFragment(currentItem);
//            GALogger.d(TAG, "currentFragment.isVisible()   " + currentFragment.isVisible());
//            if (currentFragment != null && currentFragment.isVisible()) {
//                MyApplication.runOnUIThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        currentFragment.setEnableLazyLoad(true);
//                        currentFragment.lazyLoadData();
//                    }
//                }, 1000);
//            }
//        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        GALogger.d(TAG, "i    " + i);
        UsdtPriceResponse usdtPriceResponse = GsonUtil.getInstance().getServerBean(SharedPreferencesUtils.getString(Constants.USDTPRICE), UsdtPriceResponse.class);
        if (mWalletResponse != null && usdtPriceResponse != null) {
            if (i == 0) {
                goPosition = 1;
                tvAccountName.setText(MyApplication.appContext.getResources().getString(R.string.text7));
                tvAccountSp.setText(MyApplication.appContext.getResources().getString(R.string.text9));
                double walletNum = mWalletResponse.getWalletNum();
                double walletFreezeNum = mWalletResponse.getWalletFreezeNum();
                double minSellPrice = usdtPriceResponse.getMinSellUsdtPrice();
                tvAccountMoney1.setText(new DecimalFormat("0.00").format(walletNum + walletFreezeNum));
                tvAccountMoney2.setText(MyApplication.appContext.getResources().getString(R.string.rmb) + new DecimalFormat("0.00").format((walletNum + walletFreezeNum) * minSellPrice));
                tvBiType.setText(MyApplication.appContext.getResources().getString(R.string.usdt));
            } else if (i == 1) {
                goPosition = 2;
                tvAccountName.setText(MyApplication.appContext.getResources().getString(R.string.text8));
                tvAccountSp.setText(MyApplication.appContext.getResources().getString(R.string.text10));
                double hotNum = mWalletResponse.getHotNum();
                double hotFreezeNum = mWalletResponse.getHotFreezeNum();
                tvAccountMoney1.setText(new DecimalFormat("0.00").format(hotNum + hotFreezeNum));
                tvAccountMoney2.setText(MyApplication.appContext.getResources().getString(R.string.rmb) + new DecimalFormat("0.00").format(hotNum + hotFreezeNum));
                tvBiType.setText(MyApplication.appContext.getResources().getString(R.string.nbc));
            }

            int currentItem = propertyViewPager.getCurrentItem();
            if (propertyViewPagerAdapter != null) {
                BaseFragment currentFragment = propertyViewPagerAdapter.getCurrentFragment(currentItem);
                currentFragment.lazyLoadData_now(usdtPriceResponse, mWalletResponse);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void onActivityResultProperty(int requestCode) {
        GALogger.d(TAG, "requestCode == " + requestCode);
        propertyPresenter.getInfo();
    }

    @Override
    public void getInfoSuccess(WalletResponse walletResponse) {
        if (walletResponse != null) {
            this.mWalletResponse = walletResponse;
            onPageSelected(propertyViewPager.getCurrentItem());
        }
    }

    @Override
    public void setPresenter(PropertyContract.Presenter presenter) {
        this.propertyPresenter = (PropertyPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @OnClick({R.id.iv_financial_records, R.id.tv_account_tibi, R.id.tv_account_chongbi, R.id.tv_account_zjhz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_financial_records:
                FinancialRecordsActivity.startThis(mainActivity);
                break;
            case R.id.tv_account_tibi:
                TiBiActivity.startThis(mainActivity);
                break;
            case R.id.tv_account_chongbi:
                ChongBiActivity.startThis(mainActivity);
                break;
            case R.id.tv_account_zjhz:
                RansferOfFundsActivity.startThis(mainActivity, goPosition, Constants.REQUESTCODE_11);
                break;
        }
    }
}
