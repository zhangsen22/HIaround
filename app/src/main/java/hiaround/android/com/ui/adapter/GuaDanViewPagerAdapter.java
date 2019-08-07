package hiaround.android.com.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

import hiaround.android.com.BaseFragment;
import hiaround.android.com.ui.fragment.EntrustBuyFragment;
import hiaround.android.com.ui.fragment.EntrustSaleFragment;

public class GuaDanViewPagerAdapter extends FragmentPagerAdapter {

    private BaseFragment mCurrentPrimaryItem = null;
    private EntrustSaleFragment entrustSaleFragment;
    private EntrustBuyFragment entrustBuyFragment;
    private List<Fragment> fragmentList;
    private String[] mTitles;

    public GuaDanViewPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        if(entrustSaleFragment == null){
            entrustSaleFragment = EntrustSaleFragment.newInstance("");
        }
        fragmentList.add(entrustSaleFragment);
        if(entrustBuyFragment == null){
            entrustBuyFragment = EntrustBuyFragment.newInstance("");
        }
        fragmentList.add(entrustBuyFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0:fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    public BaseFragment getCurrentFragment(int position){
        if(position == 0){
            mCurrentPrimaryItem = entrustSaleFragment;
        }else if(position == 1){
            mCurrentPrimaryItem = entrustBuyFragment;
        }
        return mCurrentPrimaryItem;
    }
}
