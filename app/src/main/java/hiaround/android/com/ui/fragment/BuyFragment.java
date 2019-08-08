package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.growalong.util.util.GALogger;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.modle.BuyAmountListResponse;
import hiaround.android.com.presenter.BuyPresenter;
import hiaround.android.com.presenter.contract.BuyContract;
import hiaround.android.com.presenter.modle.BuyModle;
import hiaround.android.com.ui.adapter.BuyFragmentAdapter;

public class BuyFragment extends BaseFragment implements BuyContract.View, BuyFragmentAdapter.OnBuyCheckListener {
    private static final String TAG = BuyFragment.class.getSimpleName();
    @BindView(R.id.rl_buy_recycleview)
    RecyclerView rlBuyRecycleview;
    @BindView(R.id.ll_choose_paytype)
    LinearLayout llChoosePaytype;
    @BindView(R.id.go_buy)
    TextView goBuy;
    @BindView(R.id.iv_pay_icon)
    ImageView ivPayIcon;
    @BindView(R.id.iv_pay_name)
    TextView ivPayName;
    private BuyFragmentAdapter buyFragmentAdapter;
    private BuyPresenter buyPresenter;
    private List<String> priceList;
    private List<String> wechatList;
    private List<String> cloudQuickPayList;
    private int payType = 2;//默认微信付款

    public static BuyFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        BuyFragment fragment = new BuyFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GALogger.d(TAG, "onCreate");
    }

    @Override
    protected int getRootView() {
        return R.layout.buy_ragment;
    }

    @Override
    protected void initView(View root) {
        if (priceList == null) {
            priceList = new ArrayList<>();
        }
        priceList.clear();
        GridLayoutManager manager = new GridLayoutManager(MyApplication.appContext, 3, LinearLayoutManager.VERTICAL, false);
        rlBuyRecycleview.setLayoutManager(manager);
        buyFragmentAdapter = new BuyFragmentAdapter(MyApplication.appContext, priceList, this);
        rlBuyRecycleview.setAdapter(buyFragmentAdapter);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        GALogger.d(TAG, "lazyLoadData   ");
        //初始化presenter
        new BuyPresenter(this, new BuyModle());
        buyPresenter.buyAmountList();
    }

    @Override
    public void setPresenter(BuyContract.Presenter presenter) {
        buyPresenter = (BuyPresenter) presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void onResume() {
        super.onResume();
        GALogger.d(TAG, "onResume   ");
    }

    @Override
    public void buyAmountListSuccess(BuyAmountListResponse buyResponse) {
        if (buyResponse != null) {
            priceList.clear();
            wechatList = buyResponse.getWechatList();
            cloudQuickPayList = buyResponse.getCloudQuickPayList();
            //默认展示微信价格
            priceList.addAll(wechatList);
            buyFragmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBuyCheck(int position, String s) {

    }

    @OnClick({R.id.ll_choose_paytype, R.id.go_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_choose_paytype:
                new XPopup.Builder(getContext())
                        .asBottomList("请选择支付方式", new String[]{"微信支付", "云闪付"}, new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                priceList.clear();
                                if (position == 0) {
                                    payType = 2;
                                    ivPayIcon.setImageResource(R.mipmap.h);
                                    priceList.addAll(wechatList);
                                } else if (position == 1) {
                                    payType = 4;
                                    ivPayIcon.setImageResource(R.mipmap.af);
                                    priceList.addAll(cloudQuickPayList);
                                }
                                buyFragmentAdapter.notifyDataSetChanged();
                                ivPayName.setText(text);
                            }
                        }).show();
                break;
            case R.id.go_buy:
                break;
        }
    }
}
