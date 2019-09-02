package aimi.android.com.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.DateUtil;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.GsonUtil;
import com.growalong.util.util.bean.MessageEvent;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import org.greenrobot.eventbus.EventBus;

import aimi.android.com.modle.LaCaraPayee;
import butterknife.BindView;
import butterknife.OnClick;
import aimi.android.com.BaseFragment;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.modle.AliPayee;
import aimi.android.com.modle.BankPayee;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.BuyBusinessResponse;
import aimi.android.com.modle.WechatPayee;
import aimi.android.com.modle.YunShanFuPayee;
import aimi.android.com.presenter.BusinessBuyDetailsPresenter;
import aimi.android.com.presenter.contract.BusinessBuyDetailsContract;
import aimi.android.com.ui.activity.BusinessBuyDetailsActivity;
import aimi.android.com.ui.widget.CenterErWeiMaPopupView;
import aimi.android.com.util.ToastUtil;

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
    @BindView(R.id.iv_copy)
    ImageView ivCopy;


    @BindView(R.id.ll_shoukuanfangshi)
    LinearLayout llShoukuanfangshi;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.iv_copy_bankname)
    ImageView ivCopyBankname;
    @BindView(R.id.ll_bankname)
    LinearLayout llBankname;
    @BindView(R.id.tv_zhibank_name)
    TextView tvZhibankName;
    @BindView(R.id.iv_copy_zhibank_name)
    ImageView ivCopyZhibankName;
    @BindView(R.id.ll_zhibankname)
    LinearLayout llZhibankname;
    @BindView(R.id.iv_copy_username)
    ImageView ivCopyUsername;
    @BindView(R.id.iv_copy_tuijianma)
    ImageView ivCopyTuijianma;
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
        tvTitle.setText("购买" + MyApplication.appContext.getResources().getString(R.string.usdt));
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
            if (type == 1) {
                llShoukuanfangshi.setVisibility(View.VISIBLE);
                tvPayTypeName.setText("支付宝");
                tvShoukuaiTypeName.setText("支付宝");
                tvPayImage.setImageResource(R.mipmap.g);
                AliPayee aliPayee = GsonUtil.getInstance().getServerBean(payee, AliPayee.class);
                if (aliPayee != null) {
                    tvShoukuaiName.setText(aliPayee.getName());
                    tvShoukuaiAccount.setText(aliPayee.getAccount());
                }
            } else if (type == 2) {
                llShoukuanfangshi.setVisibility(View.VISIBLE);
                tvPayTypeName.setText("微信");
                tvShoukuaiTypeName.setText("微信");
                tvPayImage.setImageResource(R.mipmap.h);
                WechatPayee wechatPayee = GsonUtil.getInstance().getServerBean(payee, WechatPayee.class);
                if (wechatPayee != null) {
                    tvShoukuaiName.setText(wechatPayee.getName());
                    tvShoukuaiAccount.setText(wechatPayee.getAccount());
                }
            } else if (type == 3) {
                llBankname.setVisibility(View.VISIBLE);
                llZhibankname.setVisibility(View.VISIBLE);
                tvPayTypeName.setText("银行卡");
                tvPayImage.setImageResource(R.mipmap.f);
                BankPayee bankPayee = GsonUtil.getInstance().getServerBean(payee, BankPayee.class);
                if (bankPayee != null) {
                    tvBankName.setText(bankPayee.getBankName());
                    tvZhibankName.setText(bankPayee.getSubName());
                    tvShoukuaiName.setText(bankPayee.getName());
                    tvShoukuaiAccount.setText(bankPayee.getAccount());
                }
            }else if (type == 4) {
                llShoukuanfangshi.setVisibility(View.VISIBLE);
                tvPayTypeName.setText("云闪付");
                tvShoukuaiTypeName.setText("云闪付");
                tvPayImage.setImageResource(R.mipmap.af);
                YunShanFuPayee yunShanFuPayee = GsonUtil.getInstance().getServerBean(payee, YunShanFuPayee.class);
                if (yunShanFuPayee != null) {
                    tvShoukuaiName.setText(yunShanFuPayee.getName());
                    tvShoukuaiAccount.setText(yunShanFuPayee.getAccount());
                }
            }else if (type == 5) {
                llShoukuanfangshi.setVisibility(View.VISIBLE);
                tvPayTypeName.setText("拉卡拉");
                tvShoukuaiTypeName.setText("拉卡拉");
                tvPayImage.setImageResource(R.mipmap.aq);
                LaCaraPayee laCaraPayee = GsonUtil.getInstance().getServerBean(payee, LaCaraPayee.class);
                if (laCaraPayee != null) {
                    tvShoukuaiName.setText(laCaraPayee.getName());
                    tvShoukuaiAccount.setText(laCaraPayee.getAccount());
                }
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

    @OnClick({R.id.iv_back, R.id.tv_cancel_order, R.id.tv_re_pay, R.id.iv_popview,R.id.iv_copy, R.id.iv_copy_bankname, R.id.iv_copy_zhibank_name, R.id.iv_copy_username,R.id.iv_copy_tuijianma})
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
                        .asCustom(new CenterErWeiMaPopupView(getContext(), type, GsonUtil.getInstance().objTojson(buyBusinessResponse.getPayee()), buyBusinessResponse.getUsdtTotalMoneyFmt()))
                        .show();
                break;
            case R.id.iv_copy:
                String trim = tvShoukuaiAccount.getText().toString().trim();
                if(TextUtils.isEmpty(trim)){
                    return;
                }
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) businessBuyDetailsActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", trim);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.shortShow("已复制到剪贴板");
                break;
            case R.id.iv_copy_bankname:
                String trim1 = tvBankName.getText().toString().trim();
                if (TextUtils.isEmpty(trim1)) {
                    return;
                }
                //获取剪贴板管理器：
                ClipboardManager cm1 = (ClipboardManager) businessBuyDetailsActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData1 = ClipData.newPlainText("Label", trim1);
                // 将ClipData内容放到系统剪贴板里。
                cm1.setPrimaryClip(mClipData1);
                ToastUtil.shortShow("已复制到剪贴板");
                break;
            case R.id.iv_copy_zhibank_name:
                String trim2 = tvZhibankName.getText().toString().trim();
                if (TextUtils.isEmpty(trim2)) {
                    return;
                }
                //获取剪贴板管理器：
                ClipboardManager cm2 = (ClipboardManager) businessBuyDetailsActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData2 = ClipData.newPlainText("Label", trim2);
                // 将ClipData内容放到系统剪贴板里。
                cm2.setPrimaryClip(mClipData2);
                ToastUtil.shortShow("已复制到剪贴板");
                break;
            case R.id.iv_copy_username:
                String trim3 = tvShoukuaiName.getText().toString().trim();
                if (TextUtils.isEmpty(trim3)) {
                    return;
                }
                //获取剪贴板管理器：
                ClipboardManager cm3 = (ClipboardManager) businessBuyDetailsActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData3 = ClipData.newPlainText("Label", trim3);
                // 将ClipData内容放到系统剪贴板里。
                cm3.setPrimaryClip(mClipData3);
                ToastUtil.shortShow("已复制到剪贴板");
                break;
            case R.id.iv_copy_tuijianma:
                String trim4 = tvShoukuaiCankaoma.getText().toString().trim();
                if (TextUtils.isEmpty(trim4)) {
                    return;
                }
                //获取剪贴板管理器：
                ClipboardManager cm4 = (ClipboardManager) businessBuyDetailsActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData4 = ClipData.newPlainText("Label", trim4);
                // 将ClipData内容放到系统剪贴板里。
                cm4.setPrimaryClip(mClipData4);
                ToastUtil.shortShow("已复制到剪贴板");
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
                }).asConfirm("你确定要退出购买" + MyApplication.appContext.getResources().getString(R.string.usdt) + "吗?", "",
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
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroyView();
    }
}
