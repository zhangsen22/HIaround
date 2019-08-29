package aimi.android.com.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

import aimi.android.com.BaseFragment;
import aimi.android.com.ui.fragment.TradingAccountFragment;
import aimi.android.com.ui.fragment.WalletAccountFragment;

public class PropertyViewPagerAdapter extends FragmentPagerAdapter {

    private BaseFragment mCurrentPrimaryItem = null;
    private WalletAccountFragment walletAccountFragment;
    private TradingAccountFragment tradingAccountFragment;
    private List<Fragment> fragmentList;
    private String[] mTitles;

    public PropertyViewPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        if(walletAccountFragment == null){
            walletAccountFragment = WalletAccountFragment.newInstance("");
//            walletAccountFragment.setEnableLazyLoad(false);
        }
        fragmentList.add(walletAccountFragment);
        if(tradingAccountFragment == null){
            tradingAccountFragment = TradingAccountFragment.newInstance("");
        }
        fragmentList.add(tradingAccountFragment);
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
            mCurrentPrimaryItem = walletAccountFragment;
        }else if(position == 1){
            mCurrentPrimaryItem = tradingAccountFragment;
        }
        return mCurrentPrimaryItem;
    }
}
