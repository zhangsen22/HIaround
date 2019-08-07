package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import butterknife.BindView;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.R;
import hiaround.android.com.modle.UsdtPriceResponse;
import hiaround.android.com.modle.WalletResponse;
import java.text.DecimalFormat;

public class WalletAccountFragment extends BaseFragment {

    private static final String TAG = WalletAccountFragment.class.getSimpleName();
    @BindView(R.id.tv_usdt_usered)
    TextView tvUsdtUsered;
    @BindView(R.id.tv_usdt_freeze)
    TextView tvUsdtFreeze;
    @BindView(R.id.tv_nbc_usered)
    TextView tvNbcUsered;
    @BindView(R.id.tv_nbc_freeze)
    TextView tvNbcFreeze;

    public static WalletAccountFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        WalletAccountFragment fragment = new WalletAccountFragment();
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
        GALogger.d(TAG, "WalletAccountFragment   is    lazyLoadData");
    }

    @Override
    public void lazyLoadData_now(UsdtPriceResponse usdtPriceResponse, WalletResponse mWalletResponse) {
        if (mWalletResponse != null && usdtPriceResponse != null) {
                double walletNum = mWalletResponse.getWalletNum();
                double walletFreezeNum = mWalletResponse.getWalletFreezeNum();
                tvUsdtUsered.setText(new DecimalFormat("0.00000000").format(walletNum));
                tvUsdtFreeze.setText(new DecimalFormat("0.00000000").format(walletFreezeNum));
                tvNbcUsered.setText(new DecimalFormat("0.00000000").format(0));
                tvNbcFreeze.setText(new DecimalFormat("0.00000000").format(0));
        }
    }
}
