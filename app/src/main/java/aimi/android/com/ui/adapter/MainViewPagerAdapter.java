package aimi.android.com.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;
import aimi.android.com.ui.fragment.BusinessContainerFragment;
import aimi.android.com.ui.fragment.CenterFragment;
import aimi.android.com.ui.fragment.OrderFragment;
import aimi.android.com.ui.fragment.PropertyFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment mCurrentPrimaryItem = null;
    private BusinessContainerFragment businessContainerFragment;
    private OrderFragment orderFragment;
    private PropertyFragment propertyFragment;
    private CenterFragment centerFragment;
    private List<Fragment> fragmentList;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        if(businessContainerFragment == null){
            businessContainerFragment = BusinessContainerFragment.newInstance("");
        }
        fragmentList.add(businessContainerFragment);
        if(orderFragment == null){
            orderFragment = OrderFragment.newInstance("");
        }
        fragmentList.add(orderFragment);
        if(propertyFragment == null){
            propertyFragment = PropertyFragment.newInstance("");
        }
        fragmentList.add(propertyFragment);
        if(centerFragment == null){
            centerFragment = CenterFragment.newInstance("");
        }
        fragmentList.add(centerFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0:fragmentList.size();
    }

    public Fragment getCurrentFragment(){
        return mCurrentPrimaryItem;
    }

    public CenterFragment getCenterFragment() {
        return centerFragment;
    }

    public PropertyFragment getPropertyFragment() {
        return propertyFragment;
    }

    public OrderFragment getOrderFragment() {
        return orderFragment;
    }
}
