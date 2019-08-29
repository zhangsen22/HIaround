package aimi.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.handmark.pulltorefresh.library.internal.RecycleViewLoadingLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import aimi.android.com.BaseFragment;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.app.Constants;
import aimi.android.com.modle.MessageCenterItem;
import aimi.android.com.modle.MessageCenterResponse;
import aimi.android.com.presenter.MessageCenterPresenter;
import aimi.android.com.presenter.contract.MessageCenterContract;
import aimi.android.com.ui.activity.MessageCenterActivity;
import aimi.android.com.ui.adapter.MessageCenterAdapter;
import aimi.android.com.ui.adapter.poweradapter.AdapterLoader;
import aimi.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import aimi.android.com.ui.adapter.poweradapter.OnLoadMoreListener;
import aimi.android.com.ui.adapter.poweradapter.PowerAdapter;
import aimi.android.com.ui.adapter.poweradapter.PowerHolder;

public class MessageCenterFragment extends BaseFragment implements MessageCenterContract.View, OnLoadMoreListener, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, AdapterLoader.OnItemClickListener<MessageCenterItem> {
    private static final String TAG = MessageCenterFragment.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.message_center_refresh_recycler)
    PullToRefreshRecyclerView messageCenterRefreshRecycler;
    private MessageCenterActivity messageCenterActivity;
    private MessageCenterPresenter presenter;
    private RecyclerView mRecyclerView;
    private MessageCenterAdapter messageCenterAdapter;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private static final int DEFAULT_TIME = 0;
    public List<Long> idList;

    public static MessageCenterFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        MessageCenterFragment fragment = new MessageCenterFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GALogger.d(TAG, "onCreate");
        messageCenterActivity = (MessageCenterActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.message_center_fragment;
    }

    @Override
    protected void initView(View root) {
        tvTitle.setText("消息");
        messageCenterRefreshRecycler.setId(R.id.recycleView);
        messageCenterRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = messageCenterRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        messageCenterAdapter = new MessageCenterAdapter(MyApplication.appContext);
        messageCenterAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        messageCenterRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        messageCenterAdapter.setLoadMoreListener(this);
        messageCenterAdapter.setEmptyClickListener(this);
        messageCenterAdapter.setErrorClickListener(this);
        messageCenterAdapter.setOnItemClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                presenter.msgCenterRefresh(0);
            }
        };
        loadMoreAction = new Runnable() {
            @Override
            public void run() {
                if(idList != null && idList.size() > 0){
                    presenter.msgCenterLoadMore(idList.get(0));
                }
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
        messageCenterActivity.finish();
    }

    @Override
    public void msgCenterRefreshSuccess(MessageCenterResponse messageCenterResponse) {
        List<MessageCenterItem> msg = messageCenterResponse.getMsg();
        if (msg != null && msg.size() > 0) {
            reverseIdList(msg);
            if(msg.size() <= Constants.RECYCLEVIEW_TOTALCOUNT){
                messageCenterAdapter.setTotalCount(msg.size());
            }else {
                messageCenterAdapter.setTotalCount(Integer.MAX_VALUE);
            }
            messageCenterAdapter.setList(msg);
        } else {
            emptyAnderrorView();
        }
        stopPulling();
    }

    private void emptyAnderrorView() {
        messageCenterAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        messageCenterAdapter.showError(true);
    }

    @Override
    public void msgCenterRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void msgCenterLoadMoreSuccess(MessageCenterResponse messageCenterResponse) {
        List<MessageCenterItem> msg = messageCenterResponse.getMsg();
        if (msg != null && msg.size() > 0) {
            reverseIdList(msg);
            messageCenterAdapter.setTotalCount(Integer.MAX_VALUE);
            messageCenterAdapter.appendList(msg);
        }else {
            GALogger.d(TAG,"LoadMore  is  no");
            reverseIdList(msg);
            messageCenterAdapter.setTotalCount(messageCenterAdapter.getItemRealCount());
            messageCenterAdapter.notifyDataSetChanged();
        }
        isRun = false;
    }

    public void reverseIdList(List<MessageCenterItem> billInfo){
        if(idList == null){
            idList = new ArrayList<Long>();
        }
        idList.clear();
        if(billInfo == null){
            billInfo.clear();
        }
        for (MessageCenterItem buyItem: billInfo) {
            idList.add(buyItem.getId());
        }
        Collections.reverse(idList);
    }

    @Override
    public void msgCenterLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void setPresenter(MessageCenterContract.Presenter presenter) {
        this.presenter = (MessageCenterPresenter) presenter;
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
    public void onItemClick(@NonNull PowerHolder<MessageCenterItem> holder, @NonNull View itemView, int position, MessageCenterItem item) {

    }

    public void stopPulling() {
        if (messageCenterRefreshRecycler != null) {
            messageCenterRefreshRecycler.onRefreshComplete();
        }
    }
}
