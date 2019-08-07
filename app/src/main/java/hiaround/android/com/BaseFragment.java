package hiaround.android.com;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.growalong.util.util.GALogger;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import hiaround.android.com.modle.UsdtPriceResponse;
import hiaround.android.com.modle.WalletResponse;
import hiaround.android.com.observer.NetChangeObserver;
import hiaround.android.com.observer.NetworkChangedReceiver;
import hiaround.android.com.ui.widget.LoadingDialog;
import hiaround.android.com.util.CommonFunction;
import hiaround.android.com.util.PhoneInfoUtil;


public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    private final String CLASS_NAME = getClass().getSimpleName();
    protected FrameLayout mRootContainer;
    protected boolean mIsCreateView = false; //View是否已经创建
    private Unbinder unbinder;
    //懒加载
    protected boolean mEnableLazyLoad = true; //延迟加载开关   true  加载数据   false  不加载数据   可用于判断viewpager预加载的fragment   第一次不可见时是否加载数据
    protected boolean mIsLoadData = false; //是否已经加载数据

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开启广播去监听 网络 改变事件
        NetworkChangedReceiver.getReceiverInstance().addObserver(mNetChangeObserver);
    }

    @Nullable
    @Override
    final public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_base, container, false);
        mRootContainer = root.findViewById(R.id.fragment_root_container);
        int resID = getRootView();
        if (resID > 0) {
            View child = inflater.inflate(resID, mRootContainer, false);
            //自动绑定视图
            unbinder = ButterKnife.bind(this, child);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            mRootContainer.addView(child, lp);
        } else {
            throw new RuntimeException("必须设置Fragment的布局ID");
        }

        //初始化业务视图
        initView(root);

        mIsCreateView = true;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mIsCreateView = false;
        mIsLoadData = false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 返回业务视图的 resource ID
     *
     * @return resource ID
     */
    protected abstract int getRootView();

    /**
     * 初始化业务视图
     */
    protected abstract void initView(View root);

    /**
     * 初始化业务数据，因为setUserVisibleHint调用顺序在onCreateView之前，
     * 所以需要在onCreateView或者onViewCreated中再次调用一次
     */
    protected void initData() {
        tryLazyLoadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        GALogger.d(TAG,"isVisibleToUser     "+isVisibleToUser);
        tryLazyLoadData();
    }

    /**
     * 设置懒加载开关
     *
     * @param enableLazyLoad
     */
    public void setEnableLazyLoad(boolean enableLazyLoad) {
        this.mEnableLazyLoad = enableLazyLoad;
    }

    public boolean ismEnableLazyLoad() {
        return mEnableLazyLoad;
    }

    /**
     * 设置每次进入 fragment 都要加载数据
     */
    public void setLoadDataWhenVisible() {
        mIsLoadData = false;
    }

    private void tryLazyLoadData() {
        GALogger.d(TAG,"mEnableLazyLoad   "+mEnableLazyLoad+"   mIsCreateView   "+mIsCreateView+"  getUserVisibleHint()  "+getUserVisibleHint()+"   mIsLoadData   "+mIsLoadData);
        if (mEnableLazyLoad == true && mIsCreateView == true && getUserVisibleHint() == true && mIsLoadData == false) {
            lazyLoadData();
        }
    }

    /**
     * 懒加载加载数据
     * 延迟加载业务
     */
    public void lazyLoadData() {
        mIsLoadData = true;
    }

    private LoadingDialog customDialog;

    public void showLoadingDialog() {
        if (customDialog == null) {
            customDialog = new LoadingDialog(getActivity());
        }

        if (customDialog.isShow()) {
            return;
        }
        customDialog.show();//显示,显示时页面不可点击,只能点击返回
    }

    public void hideLoadingDialog() {
        if (customDialog != null) {
            if (customDialog.isShow()) {
                customDialog.dismiss();
            }
            customDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNetChangeObserver != null) {
            NetworkChangedReceiver.getReceiverInstance().removeRegisterObserver(mNetChangeObserver);
        }
    }

    /**
     * 网络观察者
     */
    protected final NetChangeObserver mNetChangeObserver = new NetChangeObserver() {
        @Override
        public void onNetConnected(CommonFunction.NetType type) {
            if (type == CommonFunction.NetType.WIFI) {
                GALogger.d(TAG, "wifi网络已连接");
                onChildNetConnected(type);
            } else if (type == CommonFunction.NetType.TYPE_MOBILE) {
                GALogger.d(TAG, "移动网络已连接");
                onChildNetConnected(type);
            }
        }

        @Override
        public void onNetDisConnect() {
            GALogger.d(TAG, "网络已经断开");
            onChildNetDisConnect();
        }
    };

    public void onChildNetConnected(CommonFunction.NetType type){

    }

    public void onChildNetDisConnect(){

    }

    /**
     * 布局整体下移 状态栏高度，保持背景不下移
     * @param view
     */
    protected void setRootViewPaddingTop(View view) {
        view.setPadding(0, PhoneInfoUtil.getInstance().getStatusBarHeight(), 0, 0);
    }

    public void lazyLoadData_now(UsdtPriceResponse usdtPriceResponse, WalletResponse mWalletResponse) {
    }
}
