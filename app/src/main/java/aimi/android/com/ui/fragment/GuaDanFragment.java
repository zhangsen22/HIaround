package aimi.android.com.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import butterknife.OnClick;
import aimi.android.com.BaseFragment;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.ui.activity.GuaDanActivity;
import aimi.android.com.ui.adapter.GuaDanViewPagerAdapter;

public class GuaDanFragment extends BaseFragment {
    private static final String TAG = GuaDanFragment.class.getSimpleName();
    @BindView(R.id.guadan_magicindicator)
    MagicIndicator guadanMagicindicator;
    @BindView(R.id.guadan_viewPager)
    ViewPager guadanViewPager;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_guadan_content)
    LinearLayout llGuadanContent;
    private GuaDanViewPagerAdapter guaDanViewPagerAdapter;
    private GuaDanActivity guaDanActivity;

    public static GuaDanFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        GuaDanFragment fragment = new GuaDanFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        guaDanActivity = (GuaDanActivity) getActivity();
    }


    @Override
    protected int getRootView() {
        return R.layout.guadan_fragment;
    }

    @Override
    protected void initView(View root) {
        GALogger.d(TAG, "GuaDanFragment   is    initView");
        setRootViewPaddingTop(llGuadanContent);
        final String[] guadanTitle = guaDanActivity.getResources().getStringArray(R.array.guadan_title);
        guadanViewPager.setOffscreenPageLimit(guadanTitle.length - 1);
        guaDanViewPagerAdapter = new GuaDanViewPagerAdapter(getChildFragmentManager(), guadanTitle);
        guadanViewPager.setAdapter(guaDanViewPagerAdapter);

        CommonNavigator commonNavigator = new CommonNavigator(guaDanActivity);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return guadanTitle.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#f2c4c4"));
                colorTransitionPagerTitleView.setTextSize(14);
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));
                colorTransitionPagerTitleView.setText(guadanTitle[index]);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentItem = guadanViewPager.getCurrentItem();
                        if (currentItem != index) {
                            guadanViewPager.setCurrentItem(index);
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

        guadanMagicindicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(guadanMagicindicator, guadanViewPager);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        GALogger.d(TAG, "GuaDanFragment   is    lazyLoadData");
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        guaDanActivity.finish();
    }
}
