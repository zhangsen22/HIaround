package aimi.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.GsonUtil;
import com.growalong.util.util.Md5Utils;
import com.growalong.util.util.bean.MessageEvent;
import org.greenrobot.eventbus.EventBus;
import java.text.DecimalFormat;
import butterknife.BindView;
import butterknife.OnClick;
import aimi.android.com.BaseFragment;
import aimi.android.com.R;
import aimi.android.com.app.Constants;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.UsdtPriceResponse;
import aimi.android.com.presenter.EntrustBuyPresenter;
import aimi.android.com.presenter.contract.EntrustBuyContract;
import aimi.android.com.presenter.modle.EntrustBuyModle;
import aimi.android.com.ui.activity.BalancePassWordActivity;
import aimi.android.com.ui.activity.GuaDanActivity;
import aimi.android.com.util.SharedPreferencesUtils;
import aimi.android.com.util.ToastUtil;

public class EntrustBuyFragment extends BaseFragment implements EntrustBuyContract.View {
    private static final String TAG = EntrustBuyFragment.class.getSimpleName();
    @BindView(R.id.et_business_buy_price)
    EditText etBusinessBuyPrice;
    @BindView(R.id.et_expect_buy_minnum)
    EditText etExpectBuyMinnum;
    @BindView(R.id.et_expect_buy_maxnum)
    EditText etExpectBuyMaxnum;
    @BindView(R.id.et_monery_buy_password)
    EditText etMoneryBuyPassword;
    @BindView(R.id.tv_forget_buy_password)
    TextView tvForgetBuyPassword;
    @BindView(R.id.tv_buy_publish)
    TextView tvBuyPublish;
    @BindView(R.id.tv_buy_cankaojia)
    TextView tvBuyCanKaoJia;
    private double minBuyPrice;
    private double maxBuyPrice;

    private EntrustBuyPresenter entrustBuyPresenter;
    private GuaDanActivity guaDanActivity;

    public static EntrustBuyFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        EntrustBuyFragment fragment = new EntrustBuyFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        guaDanActivity = (GuaDanActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.entrust_buy_fragment;
    }

    @Override
    protected void initView(View root) {
        UsdtPriceResponse usdtPriceResponse = GsonUtil.getInstance().getServerBean(SharedPreferencesUtils.getString(Constants.USDTPRICE), UsdtPriceResponse.class);
        if(usdtPriceResponse != null){
            minBuyPrice = usdtPriceResponse.getMinBuyPrice();
            maxBuyPrice = usdtPriceResponse.getMaxBuyPrice();
            etBusinessBuyPrice.setHint("交易价格请限于"+new DecimalFormat("0.000").format(minBuyPrice)+" ~ "+new DecimalFormat("0.000").format(maxBuyPrice));
        }
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        //初始化presenter
        new EntrustBuyPresenter(this, new EntrustBuyModle());
    }

    @OnClick({R.id.tv_forget_buy_password, R.id.tv_buy_publish,R.id.tv_buy_cankaojia})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_buy_password:
                BalancePassWordActivity.startThis(guaDanActivity);
                break;
            case R.id.tv_buy_publish:
                String businessPrice = etBusinessBuyPrice.getText().toString().trim();
                if (TextUtils.isEmpty(businessPrice)) {
                    ToastUtil.shortShow("请输入交易价格");
                    return;
                }
                double d_businessPrice = Double.parseDouble(businessPrice);
                if (d_businessPrice <= 0) {
                    ToastUtil.shortShow("交易价格不能小于零");
                    return;
                }

                if(minBuyPrice > 0 && maxBuyPrice > 0 && minBuyPrice <= maxBuyPrice){
                    if(d_businessPrice < minBuyPrice || d_businessPrice > maxBuyPrice){
                        ToastUtil.shortShow("交易价格请限于" + minBuyPrice + " - " + maxBuyPrice + "之间");
                        return;
                    }
                }else {
                    ToastUtil.shortShow("请获取交易价格区间");
                    return;
                }

                String expectMinnum = etExpectBuyMinnum.getText().toString().trim();
                if (TextUtils.isEmpty(expectMinnum)) {
                    ToastUtil.shortShow("请输入您预想的最小售出数量");
                    return;
                }

                double d_expectMinnum = Double.parseDouble(expectMinnum);
                if (d_expectMinnum <= 0) {
                    ToastUtil.shortShow("请输入您预想的最小售出数量大于零");
                    return;
                }

                String expectMaxnum = etExpectBuyMaxnum.getText().toString().trim();
                if (TextUtils.isEmpty(expectMaxnum)) {
                    ToastUtil.shortShow("请输入您预想的最大售出数量");
                    return;
                }

                double d_expectMaxnum = Double.parseDouble(expectMaxnum);
                if (d_expectMaxnum <= 0) {
                    ToastUtil.shortShow("请输入您预想的最大售出数量大于零");
                    return;
                }

                if (d_expectMaxnum < d_expectMinnum) {
                    ToastUtil.shortShow("您预想的最大售出数量不能小于您预想的最小售出数量");
                    return;
                }

                String moneryPassword = etMoneryBuyPassword.getText().toString().trim();
                if (TextUtils.isEmpty(moneryPassword)) {
                    ToastUtil.shortShow("请输入资金密码");
                    return;
                }

                long currentTime = System.currentTimeMillis();
                entrustBuyPresenter.putUpBuy(d_businessPrice,d_expectMinnum,d_expectMaxnum,Md5Utils.getMD5(moneryPassword+currentTime),currentTime);
                break;
            case R.id.tv_buy_cankaojia:
                break;
        }
    }

    @Override
    public void onResume() {
        GALogger.d(TAG,"onResume      ");
        super.onResume();
    }

    @Override
    public void putUpBuySuccess(BaseBean baseBean) {
        EventBus.getDefault().post(new MessageEvent(2));
        guaDanActivity.finish();
    }

    @Override
    public void setPresenter(EntrustBuyContract.Presenter presenter) {
        entrustBuyPresenter = (EntrustBuyPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }
}
