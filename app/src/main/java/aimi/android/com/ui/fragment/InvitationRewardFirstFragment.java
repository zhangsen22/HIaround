package aimi.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import aimi.android.com.BaseFragment;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.app.AccountManager;
import aimi.android.com.app.Constants;
import aimi.android.com.modle.InvitationItem;
import aimi.android.com.modle.InvitationResponse;
import aimi.android.com.presenter.InvitationPresenter;
import aimi.android.com.presenter.contract.InvitationContract;
import aimi.android.com.ui.activity.InvitationRewardFirstActivity;
import aimi.android.com.ui.activity.InvitationRewardSecondActivity;
import aimi.android.com.ui.adapter.InvitationAdapter;
import aimi.android.com.ui.adapter.poweradapter.AdapterLoader;
import aimi.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import aimi.android.com.ui.adapter.poweradapter.PowerAdapter;
import aimi.android.com.ui.adapter.poweradapter.PowerHolder;

public class InvitationRewardFirstFragment extends BaseFragment implements InvitationContract.View, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, AdapterLoader.OnItemClickListener<InvitationItem> {
    private static final String TAG = InvitationRewardFirstFragment.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.invitation_first_refresh_recycler)
    PullToRefreshRecyclerView invitationFirstRefreshRecycler;
    private InvitationRewardFirstActivity invitationRewardFirstActivity;
    private InvitationPresenter presenter;
    private RecyclerView mRecyclerView;
    private InvitationAdapter invitationAdapter;
    private Runnable refreshAction;
    private static final int DEFAULT_TIME = 0;

    public static InvitationRewardFirstFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        InvitationRewardFirstFragment fragment = new InvitationRewardFirstFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GALogger.d(TAG, "onCreate");
        invitationRewardFirstActivity = (InvitationRewardFirstActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.invitation_reward_first_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("邀请奖励");
        invitationFirstRefreshRecycler.setId(R.id.recycleView);
        invitationFirstRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = invitationFirstRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        invitationAdapter = new InvitationAdapter(MyApplication.appContext);
        invitationAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        invitationFirstRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        invitationAdapter.setEmptyClickListener(this);
        invitationAdapter.setErrorClickListener(this);
        invitationAdapter.setOnItemClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                presenter.recommendReward(AccountManager.getInstance().getUserId());
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
        invitationRewardFirstActivity.finish();
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

    private void emptyAnderrorView() {
        invitationAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        invitationAdapter.showError(true);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    public void stopPulling() {
        if (invitationFirstRefreshRecycler != null) {
            invitationFirstRefreshRecycler.onRefreshComplete();
        }
    }

    @Override
    public void onEmptyClick(View view) {

    }

    @Override
    public void onErrorClick(View view) {

    }

    @Override
    public void onItemClick(@NonNull PowerHolder<InvitationItem> holder, @NonNull View itemView, int position, InvitationItem item) {
        if(item != null){
            long userId = item.getUserId();
            if(userId > 0) {
                InvitationRewardSecondActivity.startThis(invitationRewardFirstActivity, userId);
            }
        }
    }
}
