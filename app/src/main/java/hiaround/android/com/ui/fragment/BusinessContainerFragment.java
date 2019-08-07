package hiaround.android.com.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.Constants;
import hiaround.android.com.ui.activity.GuaDanActivity;
import hiaround.android.com.ui.activity.MainActivity;
import hiaround.android.com.ui.activity.WebViewActivity;
import hiaround.android.com.ui.adapter.BusinessViewPagerAdapter;

public class BusinessContainerFragment extends BaseFragment {
    private static final String TAG = BusinessContainerFragment.class.getSimpleName();
    @BindView(R.id.business_magicindicator)
    MagicIndicator businessMagicindicator;
    @BindView(R.id.business_viewPager)
    ViewPager businessViewPager;
    @BindView(R.id.ll_notry_click)
    LinearLayout llNotryClick;
    @BindView(R.id.iv_guadan)
    ImageView ivGuadan;
    @BindView(R.id.ff_business_content)
    FrameLayout ffBusinessContent;
    @BindView(R.id.banner)
    ConvenientBanner banner;
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
        initBanner();
        final String[] businessTitle = mainActivity.getResources().getStringArray(R.array.business_title);
        businessViewPager.setOffscreenPageLimit(businessTitle.length - 1);
        baseFragmentPagerAdapter = new BusinessViewPagerAdapter(getChildFragmentManager(), businessTitle);
        businessViewPager.setAdapter(baseFragmentPagerAdapter);
        CommonNavigator commonNavigator = new CommonNavigator(mainActivity);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return businessTitle.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#f2c4c4"));
                colorTransitionPagerTitleView.setTextSize(14);
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));
                colorTransitionPagerTitleView.setText(businessTitle[index]);
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
                indicator.setColors(Color.WHITE);
                indicator.setYOffset(UIUtil.dip2px(context, 8));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                return indicator;
            }
        });

        businessMagicindicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(businessMagicindicator, businessViewPager);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        setLoadDataWhenVisible();
        int currentItem = businessViewPager.getCurrentItem();
        if (baseFragmentPagerAdapter != null) {
            BaseFragment currentFragment = baseFragmentPagerAdapter.getCurrentFragment(currentItem);
            if (currentFragment != null && currentFragment.isVisible()) {
                currentFragment.lazyLoadData();
            }
        }
    }

    @OnClick({R.id.ll_notry_click, R.id.iv_guadan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_notry_click:
                WebViewActivity.launchVerifyCode(MyApplication.appContext, Constants.NOTIFYCLICK, true);
                break;
            case R.id.iv_guadan:
                GuaDanActivity.startThis(mainActivity);
                break;
        }
    }

    private void initBanner() {
        List<Integer> imgs = new ArrayList<>();
        imgs.add(R.mipmap.banner);
        imgs.add(R.mipmap.banner1);
        banner.setHorizontalScrollBarEnabled(false);
        banner.setPages(new CBViewHolderCreator<LocalImageHolderView>(){

            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        }, imgs).setPageIndicator(new int[]{R.drawable.shape_banner_no, R.drawable.shape_banner_yes})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT).startTurning(5000)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                    }
                });
    }

    public class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            imageView.setImageResource(data);
        }
    }
}
