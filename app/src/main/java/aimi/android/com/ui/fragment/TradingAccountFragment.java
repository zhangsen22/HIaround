package aimi.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.growalong.util.util.GALogger;

import java.text.DecimalFormat;

import butterknife.BindView;
import aimi.android.com.BaseFragment;
import aimi.android.com.R;
import aimi.android.com.modle.UsdtPriceResponse;
import aimi.android.com.modle.WalletResponse;

public class TradingAccountFragment extends BaseFragment{
    private static final String TAG = TradingAccountFragment.class.getSimpleName();
    @BindView(R.id.tv_usdt_usered)
    TextView tvUsdtUsered;
    @BindView(R.id.tv_usdt_freeze)
    TextView tvUsdtFreeze;
    @BindView(R.id.tv_nbc_usered)
    TextView tvNbcUsered;
    @BindView(R.id.tv_nbc_freeze)
    TextView tvNbcFreeze;
    public static TradingAccountFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        TradingAccountFragment fragment = new TradingAccountFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected int getRootView() {
        return R.layout.wallet_account_fragment;
    }

    @Override
    protected void initView(View root) {

    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        GALogger.d(TAG, "TradingAccountFragment   is    lazyLoadData");
    }

    @Override
    public void lazyLoadData_now(UsdtPriceResponse usdtPriceResponse, WalletResponse mWalletResponse) {
        if (mWalletResponse != null && usdtPriceResponse != null) {
            double hotNum = mWalletResponse.getHotNum();
            double hotFreezeNum = mWalletResponse.getHotFreezeNum();
            tvNbcUsered.setText(new DecimalFormat("0.00000000").format(hotNum));
            tvNbcFreeze.setText(new DecimalFormat("0.00000000").format(hotFreezeNum));
            tvUsdtUsered.setText(new DecimalFormat("0.00000000").format(0));
            tvUsdtFreeze.setText(new DecimalFormat("0.00000000").format(0));
        }
    }
}
