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
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import aimi.android.com.BaseFragment;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.app.Constants;
import aimi.android.com.modle.FinanceLogItem;
import aimi.android.com.modle.FinanceLogResponse;
import aimi.android.com.presenter.FinancialRecordsPresenter;
import aimi.android.com.presenter.contract.FinancialRecordsContract;
import aimi.android.com.ui.activity.FinancialRecordsActivity;
import aimi.android.com.ui.adapter.FinancialRecordsAdapter;
import aimi.android.com.ui.adapter.poweradapter.AdapterLoader;
import aimi.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import aimi.android.com.ui.adapter.poweradapter.OnLoadMoreListener;
import aimi.android.com.ui.adapter.poweradapter.PowerAdapter;
import aimi.android.com.ui.adapter.poweradapter.PowerHolder;

public class FinancialRecordsFragment extends BaseFragment implements FinancialRecordsContract.View, OnLoadMoreListener, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, AdapterLoader.OnItemClickListener<FinanceLogItem> {
    private static final String TAG = FinancialRecordsFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private FinancialRecordsActivity financialRecordsActivity;
    @BindView(R.id.walletaccount_pull_refresh_recycler)
    PullToRefreshRecyclerView walletaccountPullRefreshRecycler;
    private FinancialRecordsPresenter financialRecordsPresenter;
    private RecyclerView mRecyclerView;
    private FinancialRecordsAdapter financialRecordsAdapter;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private static final int DEFAULT_TIME = 0;
    public List<Long> idList;

    public static FinancialRecordsFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        FinancialRecordsFragment fragment = new FinancialRecordsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financialRecordsActivity = (FinancialRecordsActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.financial_records_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText(MyApplication.appContext.getResources().getString(R.string.text6));
        walletaccountPullRefreshRecycler.setId(R.id.recycleView);
        walletaccountPullRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = walletaccountPullRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        financialRecordsAdapter = new FinancialRecordsAdapter(financialRecordsActivity);
        financialRecordsAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        walletaccountPullRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        financialRecordsAdapter.setLoadMoreListener(this);
        financialRecordsAdapter.setEmptyClickListener(this);
        financialRecordsAdapter.setErrorClickListener(this);
        financialRecordsAdapter.setOnItemClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                financialRecordsPresenter.financeLogRefresh(0);
            }
        };
        loadMoreAction = new Runnable() {
            @Override
            public void run() {
                if (idList != null && idList.size() > 0) {
                    financialRecordsPresenter.financeLogLoadMore(idList.get(0));
                }
            }
        };
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        GALogger.d(TAG, "WalletAccountFragment   is    lazyLoadData");

        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void financeLogRefreshSuccess(FinanceLogResponse financeLogResponse) {
        List<FinanceLogItem> financeLog = financeLogResponse.getFinanceLog();
        if (financeLog != null && financeLog.size() > 0) {
            reverseIdList(financeLog);
            if (financeLog.size() <= Constants.RECYCLEVIEW_TOTALCOUNT) {
                financialRecordsAdapter.setTotalCount(financeLog.size());
            } else {
                financialRecordsAdapter.setTotalCount(Integer.MAX_VALUE);
            }
            financialRecordsAdapter.setList(financeLog);
        } else {
            emptyAnderrorView();
        }
        stopPulling();
    }

    @Override
    public void financeLogRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void financeLogLoadMoreSuccess(FinanceLogResponse financeLogResponse) {
        List<FinanceLogItem> financeLog = financeLogResponse.getFinanceLog();
        if (financeLog != null && financeLog.size() > 0) {
            reverseIdList(financeLog);
            financialRecordsAdapter.setTotalCount(Integer.MAX_VALUE);
            financialRecordsAdapter.appendList(financeLog);
        } else {
            GALogger.d(TAG, "LoadMore  is  no");
            reverseIdList(null);
            financialRecordsAdapter.setTotalCount(financialRecordsAdapter.getItemRealCount());
            financialRecordsAdapter.notifyDataSetChanged();
        }
        isRun = false;
    }

    public void reverseIdList(List<FinanceLogItem> billInfo) {
        if (idList == null) {
            idList = new ArrayList<Long>();
        }
        idList.clear();
        if (billInfo == null) {
            return;
        }
        FinanceLogItem buyItem = billInfo.get(billInfo.size() - 1);
        if(buyItem != null && buyItem.getId() > 0) {
            idList.add(buyItem.getId());
        }
    }

    @Override
    public void financeLogLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void setPresenter(FinancialRecordsContract.Presenter presenter) {
        financialRecordsPresenter = (FinancialRecordsPresenter) presenter;
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
    public void onItemClick(@NonNull PowerHolder<FinanceLogItem> holder, @NonNull View itemView, int position, FinanceLogItem item) {

    }

    private void emptyAnderrorView() {
        financialRecordsAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        financialRecordsAdapter.showError(true);
    }

    public void stopPulling() {
        if (walletaccountPullRefreshRecycler != null) {
            walletaccountPullRefreshRecycler.onRefreshComplete();
        }
    }

    @Override
    public void onResume() {
        GALogger.d(TAG, "WalletAccountFragment   is    onResume");
        super.onResume();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        financialRecordsActivity.finish();
    }
}
