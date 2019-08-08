package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import butterknife.BindView;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.R;
import hiaround.android.com.modle.BuyResponse;
import hiaround.android.com.presenter.SellPresenter;
import hiaround.android.com.presenter.contract.SellContract;
import hiaround.android.com.presenter.modle.BuyModle;

public class SellFragment extends BaseFragment implements SellContract.View {
    private static final String TAG = SellFragment.class.getSimpleName();
    @BindView(R.id.et_sell_num)
    EditText etSellNum;
    @BindView(R.id.go_sell)
    TextView goSell;
    private SellPresenter sellPresenter;

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
    }

    @Override
    protected int getRootView() {
        return R.layout.sell_ragment;
    }

    @Override
    protected void initView(View root) {
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

    @Override
    public void getSellRefreshSuccess(BuyResponse buyResponse) {
    }

    @Override
    public void getSellRefreshError() {
    }

    @Override
    public void getSellLoadMoreSuccess(BuyResponse buyResponse) {
    }

    @Override
    public void getSellLoadMoreError() {
    }
}
