package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.GsonUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.BuyAmountListResponse;
import hiaround.android.com.modle.BuyBusinessResponse;
import hiaround.android.com.modle.UsdtPriceResponse;
import hiaround.android.com.presenter.BuyPresenter;
import hiaround.android.com.presenter.contract.BuyContract;
import hiaround.android.com.presenter.modle.BuyModle;
import hiaround.android.com.ui.activity.BusinessBuyDetailsActivity;
import hiaround.android.com.ui.activity.MainActivity;
import hiaround.android.com.ui.adapter.BuyFragmentAdapter;
import hiaround.android.com.ui.widget.PagerBottomPopup;
import hiaround.android.com.util.SharedPreferencesUtils;
import hiaround.android.com.util.ToastUtil;

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
    @BindView(R.id.tv_usdt_rmb)
    TextView tvUsdtRmb;
    private BuyFragmentAdapter buyFragmentAdapter;
    private BuyPresenter buyPresenter;
    private List<String> priceList;
    private List<String> wechatList;
    private List<String> cloudQuickPayList;
    private int payType = 2;//默认微信付款
    private String payMoney;//付款的金额
    private MainActivity mainActivity;

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
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.buy_ragment;
    }

    @Override
    protected void initView(View root) {
        UsdtPriceResponse usdtPriceResponse = GsonUtil.getInstance().getServerBean(SharedPreferencesUtils.getString(Constants.USDTPRICE), UsdtPriceResponse.class);
        if (usdtPriceResponse != null) {
            tvUsdtRmb.setText(new DecimalFormat("0.000").format(usdtPriceResponse.getMinSellUsdtPrice()));
        }

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
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
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
    public void quickBuySuccess(BuyBusinessResponse buyBusinessResponse, int type) {
        if (buyBusinessResponse != null) {
            buyBusinessResponse.setCurrentTime(System.currentTimeMillis());
            UsdtPriceResponse usdtPriceResponse = GsonUtil.getInstance().getServerBean(SharedPreferencesUtils.getString(Constants.USDTPRICE), UsdtPriceResponse.class);
            if (usdtPriceResponse != null) {
                BusinessBuyDetailsActivity.startThis(mainActivity, buyBusinessResponse, usdtPriceResponse.getMinSellUsdtPrice(), Double.parseDouble(payMoney) / usdtPriceResponse.getMinSellUsdtPrice(), type);
            }
        }
    }

    @Override
    public void onBuyCheck(int position, String s) {
        payMoney = s.substring(0, s.length());
    }

    @OnClick({R.id.ll_choose_paytype, R.id.go_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_choose_paytype:
                new XPopup.Builder(getContext())
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .asCustom(new PagerBottomPopup(getContext(), new OnSelectListener() {
                            @Override
                            public void onSelect(int type, String text) {
                                priceList.clear();
                                if (type == 2) {
                                    ivPayIcon.setImageResource(R.mipmap.h);
                                    priceList.addAll(wechatList);
                                } else if (type == 4) {
                                    ivPayIcon.setImageResource(R.mipmap.af);
                                    priceList.addAll(cloudQuickPayList);
                                }
                                int checkedPosition = buyFragmentAdapter.getCheckedPosition();
                                if (checkedPosition > priceList.size() - 1) {
                                    buyFragmentAdapter.setCheckedPosition(-1);
                                }
                                buyFragmentAdapter.notifyDataSetChanged();
                                payType = type;
                                ivPayName.setText(text);
                            }
                        }))
                        .show();
                break;
            case R.id.go_buy:
                int checkedPosition = buyFragmentAdapter.getCheckedPosition();
                GALogger.d(TAG, "checkedPosition === " + checkedPosition);
                if (checkedPosition == -1) {
                    ToastUtil.shortShow("请选择金额");
                    return;
                }
                if (TextUtils.isEmpty(payMoney)) {
                    ToastUtil.shortShow("请选择金额");
                    return;
                }
                buyPresenter.quickBuy(payMoney, payType);
                break;
        }
    }
}
