package hiaround.android.com.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

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

import butterknife.BindView;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.Constants;
import hiaround.android.com.ui.adapter.OrderViewPagerAdapter;

public class OrderFragment extends BaseFragment {
    private static final String TAG = OrderFragment.class.getSimpleName();
    @BindView(R.id.order_magicindicator)
    MagicIndicator orderMagicindicator;
    @BindView(R.id.order_viewPager)
    ViewPager orderViewPager;
    @BindView(R.id.ff_order_content)
    FrameLayout ffOrderContent;
    private Context mContext;
    private OrderViewPagerAdapter orderViewPagerAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    public static OrderFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected int getRootView() {
        return R.layout.order_fragment;
    }

    @Override
    protected void initView(View root) {
        GALogger.d(TAG, "OrderFragment   is    initView");
        setRootViewPaddingTop(ffOrderContent);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        final String[] orderTitle = mContext.getResources().getStringArray(R.array.order_title);
        orderViewPager.setOffscreenPageLimit(orderTitle.length - 1);
        orderViewPagerAdapter = new OrderViewPagerAdapter(getChildFragmentManager(), orderTitle);
        orderViewPager.setAdapter(orderViewPagerAdapter);
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return orderTitle.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#f2c4c4"));
                colorTransitionPagerTitleView.setTextSize(14);
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));
                colorTransitionPagerTitleView.setText(orderTitle[index]);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentItem = orderViewPager.getCurrentItem();
                        if (currentItem != index) {
                            orderViewPager.setCurrentItem(index);
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
                indicator.setColors(Color.WHITE);
                indicator.setYOffset(UIUtil.dip2px(context, 8));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                return indicator;
            }
        });

        orderMagicindicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(orderMagicindicator, orderViewPager);
    }

    public void onActivityResultOrder(int requestCode) {
        if (requestCode == Constants.REQUESTCODE_12) {
            int currentItem = orderViewPager.getCurrentItem();
            if (orderViewPagerAdapter != null) {
                OrderItemFragment currentFragment = (OrderItemFragment) orderViewPagerAdapter.getCurrentFragment(currentItem);
                if(currentFragment != null) {
                    currentFragment.onActivityResultOrderItem(requestCode);
                }
            }
        } else if (requestCode == Constants.REQUESTCODE_13 || requestCode == Constants.REQUESTCODE_14) {
            orderViewPager.setCurrentItem(2, false);
            if (orderViewPagerAdapter != null) {
                OrderItemFragment currentFragment = (OrderItemFragment) orderViewPagerAdapter.getCurrentFragment(2);
                if(currentFragment != null) {
                    currentFragment.onActivityResultOrderItem(requestCode);
                }
            }
        }else if(requestCode == Constants.REQUESTCODE_18){
            orderViewPager.setCurrentItem(1, false);
            if (orderViewPagerAdapter != null) {
                OrderItemFragment currentFragment = (OrderItemFragment) orderViewPagerAdapter.getCurrentFragment(1);
                if(currentFragment != null) {
                    currentFragment.onActivityResultOrderItem(requestCode);
                }
            }
        }
    }
}
