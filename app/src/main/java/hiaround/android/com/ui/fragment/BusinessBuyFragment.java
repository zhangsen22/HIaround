package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.TextWatcherUtils;
import java.text.DecimalFormat;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.modle.BuyBusinessResponse;
import hiaround.android.com.modle.BuyItem;
import hiaround.android.com.presenter.BusinessBuyPresenter;
import hiaround.android.com.presenter.contract.BusinessBuyContract;
import hiaround.android.com.ui.activity.BusinessBuyActivity;
import hiaround.android.com.ui.activity.BusinessBuyDetailsActivity;
import hiaround.android.com.util.ToastUtil;

public class BusinessBuyFragment extends BaseFragment implements BusinessBuyContract.View, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = BusinessBuyFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_business_buy_price)
    TextView tvBusinessBuyPrice;
    @BindView(R.id.tv_business_buy_more)
    TextView tvBusinessBuyMore;
    @BindView(R.id.tv_business_buy_num)
    TextView tvBusinessBuyNum;
    @BindView(R.id.et_business_buy_price)
    TextView etBusinessBuyPrice;
    @BindView(R.id.et_business_buy_num)
    EditText etBusinessBuyNum;
    @BindView(R.id.tv_num_all)
    TextView tvNumAll;
    @BindView(R.id.et_business_buy_money)
    EditText etBusinessBuyMoney;
    @BindView(R.id.tv_price_all)
    TextView tvPriceAll;
    @BindView(R.id.cb_alipay)
    CheckBox cbAlipay;
    @BindView(R.id.cb_webchat)
    CheckBox cbWebchat;
    @BindView(R.id.cb_idcast)
    CheckBox cbIdcast;
    @BindView(R.id.tv_cancel_order)
    TextView tvCancelOrder;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.iv_webchat)
    ImageView ivWebchat;
    @BindView(R.id.iv_alipay)
    ImageView ivAlipay;
    @BindView(R.id.iv_idcast)
    ImageView ivIdcast;
    @BindView(R.id.ll_alipay)
    LinearLayout llAlipay;
    @BindView(R.id.ll_webchat)
    LinearLayout llWebchat;
    @BindView(R.id.ll_idcast)
    LinearLayout llIdcast;
    @BindView(R.id.iv_buy_name)
    TextView ivBuyName;

    private BusinessBuyActivity businessBuyActivity;
    private BusinessBuyPresenter presenter;
    private BuyItem buyItem;
    private boolean flag = true;//添加标志位，标志是否被编辑
    private int type;//1为支付宝，2为微信，3为银行账户

    public static BusinessBuyFragment newInstance(@Nullable BuyItem buyItem) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("buyItem", buyItem);
        BusinessBuyFragment fragment = new BusinessBuyFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        businessBuyActivity = (BusinessBuyActivity) getActivity();
        buyItem = getArguments().getParcelable("buyItem");
    }

    @Override
    protected int getRootView() {
        return R.layout.business_buy_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("购买"+MyApplication.appContext.getResources().getString(R.string.nbc));
        if(!buyItem.isLargeAmount()) {
            etBusinessBuyNum.addTextChangedListener(new TextWatcherUtils() {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    if (!TextUtils.isEmpty(s.toString())) {
                        if (flag) {
                            flag = false;
                            double num = Double.parseDouble(s.toString());
                            if (num > buyItem.getMaxNum()) {
                                ToastUtil.shortShow("超出了最大购买数量");
                                return;
                            }
                            etBusinessBuyMoney.setText((num * buyItem.getPrice()) + "");
                        } else {
                            flag = true;
                        }
                    }
                }
            });

            etBusinessBuyMoney.addTextChangedListener(new TextWatcherUtils() {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    if (!TextUtils.isEmpty(s.toString())) {
                        if (flag) {
                            flag = false;
                            double price = Double.parseDouble(s.toString());
                            if (price > buyItem.getMaxNum() * buyItem.getPrice()) {
                                ToastUtil.shortShow("超出了最大购买金额");
                                return;
                            }
                            if (price < buyItem.getMinNum() * buyItem.getPrice()) {
                                ToastUtil.shortShow("小于最小购买金额");
                                return;
                            }
                            etBusinessBuyNum.setText((price / buyItem.getPrice()) + "");
                        } else {
                            flag = true;
                        }
                    }
                }
            });
        }

        cbAlipay.setOnCheckedChangeListener(this);
        cbWebchat.setOnCheckedChangeListener(this);
        cbIdcast.setOnCheckedChangeListener(this);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        if (buyItem.isSupportBank()) {
            ivIdcast.setVisibility(View.VISIBLE);
            llIdcast.setVisibility(View.VISIBLE);
        } else {
            ivIdcast.setVisibility(View.GONE);
            llIdcast.setVisibility(View.GONE);
        }

        if (buyItem.isSupportAli()) {
            ivAlipay.setVisibility(View.VISIBLE);
            llAlipay.setVisibility(View.VISIBLE);
        } else {
            ivAlipay.setVisibility(View.GONE);
            llAlipay.setVisibility(View.GONE);
        }

        if (buyItem.isSupportWechat()) {
            ivWebchat.setVisibility(View.VISIBLE);
            llWebchat.setVisibility(View.VISIBLE);
        } else {
            ivWebchat.setVisibility(View.GONE);
            llWebchat.setVisibility(View.GONE);
        }
        ivBuyName.setText(buyItem.getNickname());
        tvBusinessBuyPrice.setText(new DecimalFormat("0.00").format(buyItem.getPrice()));
        tvBusinessBuyNum.setText(buyItem.getMaxNum() + "");
        etBusinessBuyPrice.setText(new DecimalFormat("0.00").format(buyItem.getPrice()));

        if(buyItem.isLargeAmount()){
            tvBusinessBuyMore.setText(MyApplication.appContext.getResources().getString(R.string.rmb)+new DecimalFormat("0.00").format(buyItem.getPrice() * buyItem.getMaxNum()));
            tvNumAll.setVisibility(View.GONE);
            tvPriceAll.setVisibility(View.GONE);
            etBusinessBuyNum.setFocusable(false);
            etBusinessBuyNum.setFocusableInTouchMode(false);
            etBusinessBuyNum.setText(buyItem.getMaxNum() + "");
            etBusinessBuyMoney.setFocusable(false);
            etBusinessBuyMoney.setFocusableInTouchMode(false);
            etBusinessBuyMoney.setText(new DecimalFormat("0.00").format(buyItem.getPrice() * buyItem.getMaxNum()));
        }else {
            tvBusinessBuyMore.setText(MyApplication.appContext.getResources().getString(R.string.rmb)+new DecimalFormat("0.00").format(buyItem.getPrice() * buyItem.getMinNum()) + " - " + MyApplication.appContext.getResources().getString(R.string.rmb)+ new DecimalFormat("0.00").format(buyItem.getPrice() * buyItem.getMaxNum()));
            tvNumAll.setVisibility(View.VISIBLE);
            tvPriceAll.setVisibility(View.VISIBLE);
            etBusinessBuyNum.setFocusable(true);
            etBusinessBuyNum.setFocusableInTouchMode(true);
            etBusinessBuyMoney.setFocusable(true);
            etBusinessBuyMoney.setFocusableInTouchMode(true);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_num_all, R.id.tv_price_all, R.id.tv_cancel_order, R.id.tv_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                businessBuyActivity.finish();
                break;
            case R.id.tv_num_all:
            case R.id.tv_price_all:
                etBusinessBuyNum.setText(buyItem.getMaxNum() + "");
                etBusinessBuyMoney.setText(new DecimalFormat("0.00").format(buyItem.getMaxNum() * buyItem.getPrice()));
                break;
            case R.id.tv_cancel_order:
                businessBuyActivity.finish();
                break;
            case R.id.tv_order:
                String businessBuyNum = etBusinessBuyNum.getText().toString().trim();
                if (TextUtils.isEmpty(businessBuyNum)) {
                    ToastUtil.shortShow("数量为空");
                    return;
                }
                double d_businessBuyNum = Double.parseDouble(businessBuyNum);
                if (d_businessBuyNum <= 0) {
                    ToastUtil.shortShow("数量不能为零");
                    return;
                }
                String businessBuyMoney = etBusinessBuyMoney.getText().toString().trim();
                if (TextUtils.isEmpty(businessBuyMoney)) {
                    ToastUtil.shortShow("金额为空");
                    return;
                }
                double d_businessBuyMoney = Double.parseDouble(businessBuyMoney);
                if (d_businessBuyMoney <= 0) {
                    ToastUtil.shortShow("金额不能为零");
                    return;
                }
                if (type == 0) {
                    ToastUtil.shortShow("请选择一种支付方式");
                    return;
                }
                presenter.buy(buyItem.getId(), d_businessBuyNum, type);
                break;
        }
    }

    @Override
    public void buySuccess(BuyBusinessResponse buyBusinessResponse,int type) {
        if (buyBusinessResponse != null) {
            buyBusinessResponse.setCurrentTime(System.currentTimeMillis());
            BusinessBuyDetailsActivity.startThis(businessBuyActivity, buyBusinessResponse, buyItem.getPrice(), Double.parseDouble(etBusinessBuyNum.getText().toString().trim()), type);
            businessBuyActivity.finish();
        }
    }

    @Override
    public void setPresenter(BusinessBuyContract.Presenter presenter) {
        this.presenter = (BusinessBuyPresenter) presenter;
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_alipay:
                    if (isChecked) {
                        cbWebchat.setChecked(false);
                        cbIdcast.setChecked(false);
                        type = 1;
                    } else {
                        type = 0;
                    }
                break;
            case R.id.cb_webchat:
                    if (isChecked) {
                        cbAlipay.setChecked(false);
                        cbIdcast.setChecked(false);
                        type = 2;
                    } else {
                        type = 0;
                    }
                break;
            case R.id.cb_idcast:
                    if (isChecked) {
                        cbAlipay.setChecked(false);
                        cbWebchat.setChecked(false);
                        type = 3;
                    } else {
                        type = 0;
                    }
                break;
        }
    }
}
