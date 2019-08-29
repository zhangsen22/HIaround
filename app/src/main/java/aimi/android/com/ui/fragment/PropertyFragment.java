package aimi.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.GALogger;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import aimi.android.com.BaseFragment;
import aimi.android.com.R;
import aimi.android.com.modle.RewardLogResponse;
import aimi.android.com.presenter.AwardDetailsPresenter;
import aimi.android.com.presenter.contract.AwardDetailsContract;
import aimi.android.com.presenter.modle.AwardDetailsModle;
import aimi.android.com.ui.activity.MainActivity;
import aimi.android.com.ui.activity.RewardDetailActivity;

public class PropertyFragment extends BaseFragment implements AwardDetailsContract.View {
    private static final String TAG = PropertyFragment.class.getSimpleName();
    @BindView(R.id.ll_property_bg)
    LinearLayout flTitleComtent;
    @BindView(R.id.tv_account_money1)
    TextView tvAccountMoney1;
    private MainActivity awardDetailsActivity;
    @BindView(R.id.tv_business_reward)
    TextView tvBusinessReward;
    @BindView(R.id.tv_yesterday_earnings1)
    TextView tvYesterdayEarnings1;
    @BindView(R.id.ll_business_reward)
    LinearLayout llBusinessReward;
    @BindView(R.id.tv_tuiguang_reward)
    TextView tvTuiguangReward;
    @BindView(R.id.tv_yesterday_earnings3)
    TextView tvYesterdayEarnings3;
    @BindView(R.id.ll_tuigunang_reward)
    LinearLayout llTuigunangReward;
    @BindView(R.id.tv_daili_reward)
    TextView tvDailiReward;
    @BindView(R.id.tv_yesterday_earnings4)
    TextView tvYesterdayEarnings4;
    @BindView(R.id.ll_daili_reward)
    LinearLayout llDailiReward;
    private AwardDetailsPresenter awardDetailsPresenter;
    private RewardLogResponse rewardLogResponse;

    public static PropertyFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        PropertyFragment fragment = new PropertyFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        awardDetailsActivity = (MainActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.property_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        GALogger.d(TAG, "TradingAccountFragment   is    lazyLoadData");
        setLoadDataWhenVisible();
        //初始化presenter
        new AwardDetailsPresenter(this, new AwardDetailsModle());
        awardDetailsPresenter.rewardLog();
    }

    @Override
    public void rewardLogSuccess(RewardLogResponse rewardLogResponse) {
        if (rewardLogResponse != null) {
            this.rewardLogResponse = rewardLogResponse;
            tvAccountMoney1.setText(new BigDecimal(rewardLogResponse.getTotTradeReward()+rewardLogResponse.getTotTGReward()+rewardLogResponse.getTotAgentReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
            tvBusinessReward.setText(new BigDecimal(rewardLogResponse.getTotTradeReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
            tvYesterdayEarnings1.setText(new BigDecimal(rewardLogResponse.getLastTradeReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
            tvTuiguangReward.setText(new BigDecimal(rewardLogResponse.getTotTGReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
            tvYesterdayEarnings3.setText(new BigDecimal(rewardLogResponse.getLastTGReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
            tvDailiReward.setText(new BigDecimal(rewardLogResponse.getTotAgentReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
            tvYesterdayEarnings4.setText(new BigDecimal(rewardLogResponse.getLastAgentReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
        }
    }

    @Override
    public void setPresenter(AwardDetailsContract.Presenter presenter) {
        this.awardDetailsPresenter = (AwardDetailsPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @OnClick({R.id.ll_business_reward, R.id.ll_tuigunang_reward, R.id.ll_daili_reward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_business_reward:
                if (rewardLogResponse != null) {
                    RewardDetailActivity.startThis(awardDetailsActivity, 1, rewardLogResponse);
                }
                break;
//            case R.id.ll_guadan_reward:
//                if (rewardLogResponse != null) {
//                    RewardDetailActivity.startThis(awardDetailsActivity, 4, rewardLogResponse);
//                }
//                break;
            case R.id.ll_tuigunang_reward:
                if (rewardLogResponse != null) {
                    RewardDetailActivity.startThis(awardDetailsActivity, 2, rewardLogResponse);
                }
                break;
            case R.id.ll_daili_reward:
                if (rewardLogResponse != null) {
                    RewardDetailActivity.startThis(awardDetailsActivity, 3, rewardLogResponse);
                }
                break;
        }
    }
}
