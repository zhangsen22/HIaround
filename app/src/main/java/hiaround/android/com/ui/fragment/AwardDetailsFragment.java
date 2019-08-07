package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import java.text.DecimalFormat;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.R;
import hiaround.android.com.modle.RewardLogResponse;
import hiaround.android.com.presenter.AwardDetailsPresenter;
import hiaround.android.com.presenter.contract.AwardDetailsContract;
import hiaround.android.com.ui.activity.AwardDetailsActivity;
import hiaround.android.com.ui.activity.RewardDetailActivity;

public class AwardDetailsFragment extends BaseFragment implements AwardDetailsContract.View {
    private static final String TAG = AwardDetailsFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private AwardDetailsActivity awardDetailsActivity;
    @BindView(R.id.tv_business_reward)
    TextView tvBusinessReward;
    @BindView(R.id.tv_yesterday_earnings1)
    TextView tvYesterdayEarnings1;
    @BindView(R.id.ll_business_reward)
    LinearLayout llBusinessReward;
    @BindView(R.id.tv_guadan_reward)
    TextView tvGuadanReward;
    @BindView(R.id.tv_yesterday_earnings2)
    TextView tvYesterdayEarnings2;
    @BindView(R.id.ll_guadan_reward)
    LinearLayout llGuadanReward;
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

    public static AwardDetailsFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        AwardDetailsFragment fragment = new AwardDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        awardDetailsActivity = (AwardDetailsActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.award_details_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("奖励明细");
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        GALogger.d(TAG, "TradingAccountFragment   is    lazyLoadData");
        awardDetailsPresenter.rewardLog();
    }

    @Override
    public void rewardLogSuccess(RewardLogResponse rewardLogResponse) {
        if (rewardLogResponse != null) {
            this.rewardLogResponse = rewardLogResponse;
            tvBusinessReward.setText(new DecimalFormat("0.00").format(rewardLogResponse.getTotTradeReward()));
            tvYesterdayEarnings1.setText(new DecimalFormat("0.00").format(rewardLogResponse.getLastTradeReward()));
            tvGuadanReward.setText(new DecimalFormat("0.00").format(rewardLogResponse.getTotBillReward()));
            tvYesterdayEarnings2.setText(new DecimalFormat("0.00").format(rewardLogResponse.getLastBillReward()));
            tvTuiguangReward.setText(new DecimalFormat("0.00").format(rewardLogResponse.getTotTGReward()));
            tvYesterdayEarnings3.setText(new DecimalFormat("0.00").format(rewardLogResponse.getLastTGReward()));
            tvDailiReward.setText(new DecimalFormat("0.00").format(rewardLogResponse.getTotAgentReward()));
            tvYesterdayEarnings4.setText(new DecimalFormat("0.00").format(rewardLogResponse.getLastAgentReward()));
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

    @OnClick({R.id.iv_back,R.id.ll_business_reward, R.id.ll_guadan_reward, R.id.ll_tuigunang_reward, R.id.ll_daili_reward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                awardDetailsActivity.finish();
                break;
            case R.id.ll_business_reward:
                if (rewardLogResponse != null) {
                    RewardDetailActivity.startThis(awardDetailsActivity, 1, rewardLogResponse);
                }
                break;
            case R.id.ll_guadan_reward:
                if (rewardLogResponse != null) {
                    RewardDetailActivity.startThis(awardDetailsActivity, 4, rewardLogResponse);
                }
                break;
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
