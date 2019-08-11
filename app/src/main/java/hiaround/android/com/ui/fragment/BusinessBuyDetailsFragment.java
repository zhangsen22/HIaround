package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.growalong.util.util.DateUtil;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.GsonUtil;
import com.growalong.util.util.bean.MessageEvent;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.modle.AliPayee;
import hiaround.android.com.modle.BankPayee;
import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.modle.BuyBusinessResponse;
import hiaround.android.com.modle.WechatPayee;
import hiaround.android.com.presenter.BusinessBuyDetailsPresenter;
import hiaround.android.com.presenter.contract.BusinessBuyDetailsContract;
import hiaround.android.com.ui.activity.BusinessBuyDetailsActivity;
import hiaround.android.com.ui.widget.CenterErWeiMaPopupView;
import hiaround.android.com.util.ToastUtil;

public class BusinessBuyDetailsFragment extends BaseFragment implements BusinessBuyDetailsContract.View {
    private static final String TAG = BusinessBuyDetailsFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;
    @BindView(R.id.tv_order_code)
    TextView tvOrderCode;
    @BindView(R.id.tv_pay_price)
    TextView tvPayPrice;
    @BindView(R.id.tv_biusness_price)
    TextView tvBiusnessPrice;
    @BindView(R.id.tv_biusness_num)
    TextView tvBiusnessNum;
    @BindView(R.id.tv_pay_type_name)
    TextView tvPayTypeName;
    @BindView(R.id.tv_shoukuai_type_name)
    TextView tvShoukuaiTypeName;
    @BindView(R.id.tv_shoukuai_name)
    TextView tvShoukuaiName;
    @BindView(R.id.tv_shoukuai_account)
    TextView tvShoukuaiAccount;
    @BindView(R.id.tv_shoukuai_cankaoma)
    TextView tvShoukuaiCankaoma;
    @BindView(R.id.tv_cancel_order)
    TextView tvCancelOrder;
    @BindView(R.id.tv_re_pay)
    TextView tvRePay;
    @BindView(R.id.tv_pay_image)
    ImageView tvPayImage;
    @BindView(R.id.iv_popview)
    ImageView ivPopview;
    private BusinessBuyDetailsActivity businessBuyDetailsActivity;
    private BusinessBuyDetailsPresenter presenter;
    private BuyBusinessResponse buyBusinessResponse;
    private long createTime = 0;
    private int type;
    private CountDownTimer timer;

    public static BusinessBuyDetailsFragment newInstance(@Nullable BuyBusinessResponse buyBusinessResponse) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("buyBusinessResponse", buyBusinessResponse);
        BusinessBuyDetailsFragment fragment = new BusinessBuyDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        businessBuyDetailsActivity = (BusinessBuyDetailsActivity) getActivity();
        buyBusinessResponse = getArguments().getParcelable("buyBusinessResponse");
    }

    @Override
    protected int getRootView() {
        return R.layout.business_buy_details_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("购买"+MyApplication.appContext.getResources().getString(R.string.usdt));
        type = buyBusinessResponse.getPayType();
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        tvOrderCode.setText(buyBusinessResponse.getTradeId());
        tvPayPrice.setText(MyApplication.appContext.getResources().getString(R.string.rmb) + buyBusinessResponse.getUsdtTotalMoneyFmt());
        tvBiusnessPrice.setText(MyApplication.appContext.getResources().getString(R.string.rmb) + buyBusinessResponse.getUsdtPriceFmt());
        tvBiusnessNum.setText(buyBusinessResponse.getUsdtNumFmt());
        createTime = buyBusinessResponse.getCurrentTime();
        String payee = GsonUtil.getInstance().objTojson(buyBusinessResponse.getPayee());
        if (!TextUtils.isEmpty(payee)) {
            if ( type == 1) {
                tvPayTypeName.setText("支付宝");
                tvShoukuaiTypeName.setText("支付宝");
                tvPayImage.setImageResource(R.mipmap.g);
                AliPayee aliPayee = GsonUtil.getInstance().getServerBean(payee, AliPayee.class);
                if (aliPayee != null) {
                    tvShoukuaiName.setText(aliPayee.getName());
                    tvShoukuaiAccount.setText(aliPayee.getAccount());
                    ivPopview.setVisibility(View.VISIBLE);
                }
            } else if (type == 2) {
                tvPayTypeName.setText("微信");
                tvShoukuaiTypeName.setText("微信");
                tvPayImage.setImageResource(R.mipmap.h);
                WechatPayee wechatPayee = GsonUtil.getInstance().getServerBean(payee, WechatPayee.class);
                if (wechatPayee != null) {
                    tvShoukuaiName.setText(wechatPayee.getName());
                    tvShoukuaiAccount.setText(wechatPayee.getAccount());
                    ivPopview.setVisibility(View.VISIBLE);
                }
            } else if (type == 3) {
                tvPayTypeName.setText("银行账户");
                tvShoukuaiTypeName.setText("银行账户");
                tvPayImage.setImageResource(R.mipmap.f);
                BankPayee bankPayee = GsonUtil.getInstance().getServerBean(payee, BankPayee.class);
                if (bankPayee != null) {
                    tvShoukuaiName.setText(bankPayee.getName());
                    tvShoukuaiAccount.setText(bankPayee.getAccount());
                    ivPopview.setVisibility(View.GONE);
                }
            }else if (type == 4) {
                tvPayTypeName.setText("云闪付");
                tvShoukuaiTypeName.setText("云闪付");
                tvPayImage.setImageResource(R.mipmap.af);
//                BankPayee bankPayee = GsonUtil.getInstance().getServerBean(payee, BankPayee.class);
//                if (bankPayee != null) {
//                    tvShoukuaiName.setText(bankPayee.getName());
//                    tvShoukuaiAccount.setText(bankPayee.getAccount());
//                    ivPopview.setVisibility(View.GONE);
//                }
            }

            tvShoukuaiCankaoma.setText(buyBusinessResponse.getPayCode() + "");

            long currentTime = System.currentTimeMillis();
            if (currentTime >= createTime + 10 * 60 * 1000) {

            } else {
                timer = new CountDownTimer(createTime + 10 * 60 * 1000 - currentTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //如果是Fragment 就判断getActivity() 是否为NULL
                        //如果是Activity 就判断!activity.isFinishing() 是否为NULL
                        if (businessBuyDetailsActivity != null) {
                            int left = (int) ((millisUntilFinished - 1000) / 1000);
                            GALogger.d(TAG, "left       " + left);
                            if (left > 0) {
                                tvCountDown.setText(DateUtil.getCurrentDateString2(millisUntilFinished));
                            } else {

                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        ToastUtil.shortShow("订单已超时");
                        businessBuyDetailsActivity.finish();
                    }
                };
                timer.start();
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel_order, R.id.tv_re_pay,R.id.iv_popview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                initPopView();
                break;
            case R.id.tv_cancel_order:
                //带确认和取消按钮的弹窗
                new XPopup.Builder(getContext())
//                         .dismissOnTouchOutside(false)
                        // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                        .setPopupCallback(new XPopupCallback() {
                            @Override
                            public void onShow() {
                                Log.e("tag", "onShow");
                            }

                            @Override
                            public void onDismiss() {
                                Log.e("tag", "onDismiss");
                            }
                        }).asConfirm("你确定要取消订单吗?", "",
                        "取消", "确定",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                presenter.ordercancel(buyBusinessResponse.getTradeId());
                            }
                        }, null, false)
                        .show();
                break;
            case R.id.tv_re_pay:
                presenter.manualPay(buyBusinessResponse.getTradeId());
                break;
            case R.id.iv_popview:
                new XPopup.Builder(getContext())
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .asCustom(new CenterErWeiMaPopupView(getContext(),type,GsonUtil.getInstance().objTojson(buyBusinessResponse.getPayee())))
                        .show();
                break;
        }
    }

    private void initPopView() {
        //带确认和取消按钮的弹窗
        new XPopup.Builder(getContext())
//                         .dismissOnTouchOutside(false)
                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                .setPopupCallback(new XPopupCallback() {
                    @Override
                    public void onShow() {
                        Log.e("tag", "onShow");
                    }

                    @Override
                    public void onDismiss() {
                        Log.e("tag", "onDismiss");
                    }
                }).asConfirm("你确定要退出购买"+MyApplication.appContext.getResources().getString(R.string.usdt)+"吗?", "",
                "取消", "确定",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        businessBuyDetailsActivity.finish();
                    }
                }, null, false)
                .show();
    }

    @Override
    public void ordercancelSuccess(BaseBean baseBean) {
        businessBuyDetailsActivity.finish();
    }

    @Override
    public void manualPaySuccess(BaseBean baseBean) {
        businessBuyDetailsActivity.finish();
    }

    @Override
    public void goOrderMySellComplete() {
        EventBus.getDefault().post(new MessageEvent(3));
        businessBuyDetailsActivity.finish();
    }

    @Override
    public void setPresenter(BusinessBuyDetailsContract.Presenter presenter) {
        this.presenter = (BusinessBuyDetailsPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    public void onKeyDownF() {
        initPopView();
    }

    @Override
    public void onDestroyView() {
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        super.onDestroyView();
    }
}
