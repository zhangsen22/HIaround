package hiaround.android.com.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import hiaround.android.com.BaseFragment;
import hiaround.android.com.ui.fragment.OrderItemDetailsFragment;

public class OrderItemViewPagerAdapter extends FragmentPagerAdapter {

    private BaseFragment mCurrentPrimaryItem = null;
    private OrderItemDetailsFragment orderItemDetailsFragment;
    private List<BaseFragment> fragmentList;
    private String[] mTitles;

    public OrderItemViewPagerAdapter(FragmentManager fm, String[] titles, int type) {
        super(fm);
        this.mTitles = titles;
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
            for (int i = 0; i < titles.length; i++) {
                orderItemDetailsFragment = OrderItemDetailsFragment.newInstance(type,i+1);
                fragmentList.add(orderItemDetailsFragment);
            }
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    public BaseFragment getCurrentFragment(int position) {
        mCurrentPrimaryItem = (BaseFragment) fragmentList.get(position);
        return mCurrentPrimaryItem;
    }
}
