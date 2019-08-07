package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.handmark.pulltorefresh.library.internal.RecycleViewLoadingLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.InvitationItem;
import hiaround.android.com.modle.InvitationResponse;
import hiaround.android.com.presenter.InvitationPresenter;
import hiaround.android.com.presenter.contract.InvitationContract;
import hiaround.android.com.ui.activity.InvitationRewardSecondActivity;
import hiaround.android.com.ui.adapter.InvitationAdapter;
import hiaround.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;

public class InvitationRewardSecondFragment extends BaseFragment implements InvitationContract.View, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener {
    private static final String TAG = InvitationRewardSecondFragment.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.invitation_second_refresh_recycler)
    PullToRefreshRecyclerView invitationSecondRefreshRecycler;
    private InvitationRewardSecondActivity invitationRewardSecondActivity;
    private InvitationPresenter presenter;
    private long userId;
    private RecyclerView mRecyclerView;
    private InvitationAdapter invitationAdapter;
    private Runnable refreshAction;
    private static final int DEFAULT_TIME = 0;

    public static InvitationRewardSecondFragment newInstance(@Nullable long userId) {
        Bundle arguments = new Bundle();
        arguments.putLong("userId",userId);
        InvitationRewardSecondFragment fragment = new InvitationRewardSecondFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GALogger.d(TAG, "onCreate");
        invitationRewardSecondActivity = (InvitationRewardSecondActivity) getActivity();
        userId = getArguments().getLong("userId", 0);
    }

    @Override
    protected int getRootView() {
        return R.layout.invitation_reward_second_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("邀请奖励");
        invitationSecondRefreshRecycler.setId(R.id.recycleView);
        invitationSecondRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = invitationSecondRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        invitationAdapter = new InvitationAdapter(MyApplication.appContext);
        invitationAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        invitationSecondRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        invitationAdapter.setEmptyClickListener(this);
        invitationAdapter.setErrorClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                presenter.recommendReward(userId);
            }
        };
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        invitationRewardSecondActivity.finish();
    }

    @Override
    public void recommendRewardSuccess(InvitationResponse invitationResponse) {
        List<InvitationItem> msg = invitationResponse.getList();
        if (msg != null && msg.size() > 0) {
            if(msg.size() <= Constants.RECYCLEVIEW_TOTALCOUNT){
                invitationAdapter.setTotalCount(msg.size());
            }else {
                invitationAdapter.setTotalCount(Integer.MAX_VALUE);
            }
            invitationAdapter.setList(msg);
        } else {
            emptyAnderrorView();
        }
        stopPulling();
    }

    @Override
    public void recommendRewardError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void setPresenter(InvitationContract.Presenter presenter) {
        this.presenter = (InvitationPresenter) presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private void emptyAnderrorView() {
        invitationAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        invitationAdapter.showError(true);
    }

    public void stopPulling() {
        if (invitationSecondRefreshRecycler != null) {
            invitationSecondRefreshRecycler.onRefreshComplete();
        }
    }

    @Override
    public void onEmptyClick(View view) {

    }

    @Override
    public void onErrorClick(View view) {

    }
}
