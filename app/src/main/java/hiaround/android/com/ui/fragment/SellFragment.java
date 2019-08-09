package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.GsonUtil;
import com.growalong.util.util.Md5Utils;
import com.growalong.util.util.TextWatcherUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import java.text.DecimalFormat;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.R;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.UsdtPriceResponse;
import hiaround.android.com.presenter.SellPresenter;
import hiaround.android.com.presenter.contract.SellContract;
import hiaround.android.com.presenter.modle.BuyModle;
import hiaround.android.com.ui.activity.BalancePassWordActivity;
import hiaround.android.com.ui.activity.MainActivity;
import hiaround.android.com.ui.widget.PagerBottomPopup1;
import hiaround.android.com.util.SharedPreferencesUtils;
import hiaround.android.com.util.ToastUtil;

public class SellFragment extends BaseFragment implements SellContract.View {
    private static final String TAG = SellFragment.class.getSimpleName();
    @BindView(R.id.et_sell_num)
    EditText etSellNum;
    @BindView(R.id.tv_usdt_rmb)
    TextView tvUsdtRmb;
    @BindView(R.id.go_sell)
    TextView goSell;
    @BindView(R.id.tv_sell_usdtnum)
    TextView tvSellUsdtnum;
    @BindView(R.id.iv_sell_icon)
    ImageView ivSellIcon;
    @BindView(R.id.iv_sell_name)
    TextView ivSellName;
    @BindView(R.id.ll_choose_paytype1)
    LinearLayout llChoosePaytype1;
    @BindView(R.id.et_alipay_password)
    EditText etAlipayPassword;
    @BindView(R.id.tv_forget_alipay_password)
    TextView tvForgetAlipayPassword;
    private MainActivity mainActivity;
    private SellPresenter sellPresenter;
    private int payType = 0;//默认请选择一种支付方式

    public static SellFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        SellFragment fragment = new SellFragment();
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
        return R.layout.sell_ragment;
    }

    @Override
    protected void initView(View root) {
        UsdtPriceResponse usdtPriceResponse = GsonUtil.getInstance().getServerBean(SharedPreferencesUtils.getString(Constants.USDTPRICE), UsdtPriceResponse.class);
        if (usdtPriceResponse != null) {
            tvUsdtRmb.setText(new DecimalFormat("0.000").format(usdtPriceResponse.getMinSellUsdtPrice()));
        }

        etSellNum.addTextChangedListener(new TextWatcherUtils() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (!TextUtils.isEmpty(s.toString())) {
                    double num = Double.parseDouble(s.toString());
                    if (num <= 0) {
                        ToastUtil.shortShow("出售不能小于0");
                        tvSellUsdtnum.setText(new DecimalFormat("0.000").format(0));
                        return;
                    }
                    UsdtPriceResponse usdtPriceResponse = GsonUtil.getInstance().getServerBean(SharedPreferencesUtils.getString(Constants.USDTPRICE), UsdtPriceResponse.class);
                    if (usdtPriceResponse != null) {
                        tvSellUsdtnum.setText(new DecimalFormat("0.000").format(num / usdtPriceResponse.getMinSellUsdtPrice()));
                    }
                }
            }
        });
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        GALogger.d(TAG, "SellFragment  is  lazyLoadData");
        //初始化presenter
        new SellPresenter(this, new BuyModle());
    }

    @Override
    public void setPresenter(SellContract.Presenter presenter) {
        sellPresenter = (SellPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @OnClick({R.id.ll_choose_paytype1, R.id.go_sell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_choose_paytype1:
                new XPopup.Builder(getContext())
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .asCustom(new PagerBottomPopup1(getContext(), new OnSelectListener() {
                            @Override
                            public void onSelect(int type, String text) {
//private int type = 0;//1为支付宝，2为微信，3为银行账户
                                if (type == 0) {
                                    ivSellIcon.setVisibility(View.GONE);
                                } else {
                                    ivSellIcon.setVisibility(View.VISIBLE);
                                    if (type == 1) {
                                        ivSellIcon.setImageResource(R.mipmap.g);
                                    } else if (type == 2) {
                                        ivSellIcon.setImageResource(R.mipmap.h);
                                    } else if (type == 3) {
                                        ivSellIcon.setImageResource(R.mipmap.f);
                                    }
                                }
                                payType = type;
                                ivSellName.setText(text);
                            }
                        }))
                        .show();
                break;
            case R.id.go_sell:
                String trim = etSellNum.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    ToastUtil.shortShow("金额为空");
                    return;
                }
                double d_businessBuyMoney = Double.parseDouble(trim);
                if (d_businessBuyMoney <= 0) {
                    ToastUtil.shortShow("金额不能为零");
                    return;
                }
                if (payType == 0) {
                    ToastUtil.shortShow("请选择一种支付方式");
                    return;
                }
                String forgetPassword = etAlipayPassword.getText().toString().trim();
                if(TextUtils.isEmpty(forgetPassword)){
                    ToastUtil.shortShow("请输入资金密码");
                    return;
                }
                long currentTime = System.currentTimeMillis();
                sellPresenter.quickSell(payType, Md5Utils.getMD5(forgetPassword + currentTime), d_businessBuyMoney, currentTime);
                break;
        }
    }

    @Override
    public void quickSellSuccess() {
        ToastUtil.shortShow("您的单子已出售,请到订单列表查看");

    }

    @OnClick(R.id.tv_forget_alipay_password)
    public void onViewClicked() {
        BalancePassWordActivity.startThis(mainActivity);
    }
}
