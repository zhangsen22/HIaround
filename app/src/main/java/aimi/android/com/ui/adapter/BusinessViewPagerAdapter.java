package aimi.android.com.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;
import aimi.android.com.BaseFragment;
import aimi.android.com.ui.fragment.BuyFragment;
import aimi.android.com.ui.fragment.SellFragment;

public class BusinessViewPagerAdapter extends FragmentPagerAdapter {

    private BaseFragment mCurrentPrimaryItem = null;
    private BuyFragment buyFragment;
    private SellFragment sellFragment;
    private List<Fragment> fragmentList;
    private String[] mTitles;

    public BusinessViewPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        if(buyFragment == null){
            buyFragment = BuyFragment.newInstance("");
        }
        fragmentList.add(buyFragment);
        if(sellFragment == null){
            sellFragment = SellFragment.newInstance("");
        }
        fragmentList.add(sellFragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0:fragmentList.size();
    }

    public BaseFragment getCurrentFragment(int position){
        if(position == 0){
            mCurrentPrimaryItem = buyFragment;
        }else if(position == 1){
            mCurrentPrimaryItem = sellFragment;
        }
        return mCurrentPrimaryItem;
    }
}
