package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.handmark.pulltorefresh.library.internal.RecycleViewLoadingLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.RewardDetailItem;
import hiaround.android.com.modle.RewardDetailResponse;
import hiaround.android.com.modle.RewardLogResponse;
import hiaround.android.com.presenter.RewardDetailPresenter;
import hiaround.android.com.presenter.contract.RewardDetailContract;
import hiaround.android.com.ui.activity.RewardDetailActivity;
import hiaround.android.com.ui.adapter.RewardDetailAdapter;
import hiaround.android.com.ui.adapter.poweradapter.AdapterLoader;
import hiaround.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import hiaround.android.com.ui.adapter.poweradapter.OnLoadMoreListener;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;

/**
 * 1为交易奖励，2推广分红，3代理奖励，4挂单奖励
 */
public class RewardDetailFragment extends BaseFragment implements RewardDetailContract.View, OnLoadMoreListener, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, AdapterLoader.OnItemClickListener<RewardDetailItem> {
    private static final String TAG = RewardDetailFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.tv_all_jiangli)
    TextView tvAllJiangli;
    @BindView(R.id.tv_last_jiangli)
    TextView tvLastJiangli;
    @BindView(R.id.reward_detailspull_refresh_recycler)
    PullToRefreshRecyclerView rewardDetailspullRefreshRecycler;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_reward_bottom_type1)
    TextView tvRewardBottomType1;
    @BindView(R.id.tv_reward_bottom_num1)
    TextView tvRewardBottomNum1;
    @BindView(R.id.tv_reward_bottom_type2)
    TextView tvRewardBottomType2;
    @BindView(R.id.tv_reward_bottom_num2)
    TextView tvRewardBottomNum2;
    @BindView(R.id.ll_reward_bottom_details)
    LinearLayout llRewardBottomDetails;
    private RecyclerView mRecyclerView;
    private RewardDetailActivity rewardDetailActivity;
    private int fromType;
    private RewardLogResponse mRewardLogResponse;
    private RewardDetailPresenter presenter;
    private RewardDetailAdapter rewardDetailAdapter;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private static final int DEFAULT_TIME = 0;
    public List<Long> idList;

    public static RewardDetailFragment newInstance(int fromType, RewardLogResponse rewardLogResponse) {
        Bundle arguments = new Bundle();
        arguments.putInt("fromType", fromType);
        arguments.putParcelable("rewardLogResponse", rewardLogResponse);
        RewardDetailFragment fragment = new RewardDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rewardDetailActivity = (RewardDetailActivity) getActivity();
        fromType = getArguments().getInt("fromType");
        mRewardLogResponse = getArguments().getParcelable("rewardLogResponse");
    }

    @Override
    protected int getRootView() {
        return R.layout.reward_detail_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        rewardDetailspullRefreshRecycler.setId(R.id.recycleView);
        rewardDetailspullRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = rewardDetailspullRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        rewardDetailAdapter = new RewardDetailAdapter(MyApplication.appContext);
        rewardDetailAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        rewardDetailspullRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        rewardDetailAdapter.setLoadMoreListener(this);
        rewardDetailAdapter.setEmptyClickListener(this);
        rewardDetailAdapter.setErrorClickListener(this);
        rewardDetailAdapter.setOnItemClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                presenter.rewardDetailRefresh(fromType, 0);
            }
        };
        loadMoreAction = new Runnable() {
            @Override
            public void run() {
                if (idList != null && idList.size() > 0) {
                    presenter.rewardDetailLoadMore(fromType, idList.get(0));
                }
            }
        };
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
        if (fromType > 0) {
            if (fromType == 1) {
                tvAllJiangli.setText(new BigDecimal(mRewardLogResponse.getTotTradeReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
                tvLastJiangli.setText(new BigDecimal(mRewardLogResponse.getLastTradeReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
                tvTitle.setText("交易奖励详细");
                llRewardBottomDetails.setVisibility(View.GONE);
            } else if (fromType == 2) {
                tvAllJiangli.setText(new BigDecimal(mRewardLogResponse.getTotTGReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
                tvLastJiangli.setText(new BigDecimal(mRewardLogResponse.getLastTGReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
                tvTitle.setText("推广分红详细");
                llRewardBottomDetails.setVisibility(View.VISIBLE);
                tvRewardBottomType1.setText(MyApplication.appContext.getResources().getString(R.string.text1));
                tvRewardBottomType2.setText(MyApplication.appContext.getResources().getString(R.string.text2));
                tvRewardBottomNum1.setText(mRewardLogResponse.getFirstTG()+"");
                tvRewardBottomNum2.setText(mRewardLogResponse.getSecondTG()+"");
            } else if (fromType == 3) {
                tvAllJiangli.setText(new BigDecimal(mRewardLogResponse.getTotAgentReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
                tvLastJiangli.setText(new BigDecimal(mRewardLogResponse.getLastAgentReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
                tvTitle.setText("代理奖励详细");
                llRewardBottomDetails.setVisibility(View.VISIBLE);
                tvRewardBottomType1.setText(MyApplication.appContext.getResources().getString(R.string.text3));
                tvRewardBottomType2.setText(MyApplication.appContext.getResources().getString(R.string.text4));
                tvRewardBottomNum1.setText(mRewardLogResponse.getFirstAgentTG()+"");
                tvRewardBottomNum2.setText(mRewardLogResponse.getSecondAgentTG()+"");
            } else if (fromType == 4) {
                tvAllJiangli.setText(new BigDecimal(mRewardLogResponse.getTotBillReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
                tvLastJiangli.setText(new BigDecimal(mRewardLogResponse.getLastBillReward()).setScale(2,BigDecimal.ROUND_DOWN).toString());
                tvTitle.setText("挂单奖励详细");
                llRewardBottomDetails.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        rewardDetailActivity.finish();
    }

    @Override
    public void rewardDetailRefreshSuccess(RewardDetailResponse rewardDetailResponse) {
        List<RewardDetailItem> details = rewardDetailResponse.getDetails();
        if (details != null && details.size() > 0) {
            reverseIdList(details);
            if(details.size() <= Constants.RECYCLEVIEW_TOTALCOUNT){
                rewardDetailAdapter.setTotalCount(details.size());
            }else {
                rewardDetailAdapter.setTotalCount(Integer.MAX_VALUE);
            }
            rewardDetailAdapter.setList(details);
        } else {
            emptyAnderrorView();
        }
        stopPulling();
    }

    @Override
    public void rewardDetailRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void rewardDetailLoadMoreSuccess(RewardDetailResponse rewardDetailResponse) {
        List<RewardDetailItem> details = rewardDetailResponse.getDetails();
        if (details != null && details.size() > 0) {
            reverseIdList(details);
            rewardDetailAdapter.setTotalCount(Integer.MAX_VALUE);
            rewardDetailAdapter.appendList(details);
        } else {
            GALogger.d(TAG, "LoadMore  is  no");
            reverseIdList(null);
            rewardDetailAdapter.setTotalCount(rewardDetailAdapter.getItemRealCount());
            rewardDetailAdapter.notifyDataSetChanged();
        }
        isRun = false;
    }

    public void reverseIdList(List<RewardDetailItem> billInfo) {
        if (idList == null) {
            idList = new ArrayList<Long>();
        }
        idList.clear();
        if (billInfo == null) {
            return;
        }
        RewardDetailItem buyItem = billInfo.get(billInfo.size() - 1);
        if(buyItem != null && buyItem.getId() > 0) {
            idList.add(buyItem.getId());
        }
    }

    @Override
    public void rewardDetailLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void setPresenter(RewardDetailContract.Presenter presenter) {
        this.presenter = (RewardDetailPresenter) presenter;
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
    public void onLoadMore() {
        if (isRun) {
            GALogger.d(TAG, "onLoadMore:正在执行，直接返回。。。 ");
            return;
        }
        GALogger.d(TAG, "onLoadMore: ");
        isRun = true;
        mRecyclerView.postDelayed(loadMoreAction, DEFAULT_TIME);
    }

    @Override
    public void onEmptyClick(View view) {

    }

    @Override
    public void onErrorClick(View view) {

    }

    @Override
    public void onItemClick(@NonNull PowerHolder<RewardDetailItem> holder, @NonNull View itemView, int position, RewardDetailItem item) {

    }

    private void emptyAnderrorView() {
        rewardDetailAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        rewardDetailAdapter.showError(true);
    }

    public void stopPulling() {
        if (rewardDetailspullRefreshRecycler != null) {
            rewardDetailspullRefreshRecycler.onRefreshComplete();
        }
    }
}
