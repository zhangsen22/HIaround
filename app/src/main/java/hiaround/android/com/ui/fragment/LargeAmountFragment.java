package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.growalong.util.util.GALogger;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.handmark.pulltorefresh.library.internal.RecycleViewLoadingLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import butterknife.BindView;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.LargeAmountItem;
import hiaround.android.com.modle.LargeAmountResponse;
import hiaround.android.com.presenter.LargeAmountPresenter;
import hiaround.android.com.presenter.contract.LargeAmountContract;
import hiaround.android.com.presenter.modle.BuyModle;
import hiaround.android.com.ui.adapter.LargeAmountAdapter;
import hiaround.android.com.ui.adapter.poweradapter.AdapterLoader;
import hiaround.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import hiaround.android.com.ui.adapter.poweradapter.OnLoadMoreListener;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;

public class LargeAmountFragment extends BaseFragment implements LargeAmountContract.View, OnLoadMoreListener, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, AdapterLoader.OnItemClickListener<LargeAmountItem> {
    private static final String TAG = LargeAmountFragment.class.getSimpleName();
    @BindView(R.id.largeamount_pull_refresh_recycler)
    PullToRefreshRecyclerView largeamountPullRefreshRecycler;
    private LargeAmountPresenter presenter;
    private RecyclerView mRecyclerView;
    private LargeAmountAdapter largeAmountAdapter;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private static final int DEFAULT_TIME = 0;
    public List<Long> idList;
    private List<LargeAmountItem> listTemp;

    public static LargeAmountFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        LargeAmountFragment fragment = new LargeAmountFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected int getRootView() {
        return R.layout.large_amount_fragment;
    }

    @Override
    protected void initView(View root) {
        largeamountPullRefreshRecycler.setId(R.id.recycleView);
        largeamountPullRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = largeamountPullRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        largeAmountAdapter = new LargeAmountAdapter(MyApplication.appContext);
        largeAmountAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        largeamountPullRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        largeAmountAdapter.setLoadMoreListener(this);
        largeAmountAdapter.setEmptyClickListener(this);
        largeAmountAdapter.setErrorClickListener(this);
        largeAmountAdapter.setOnItemClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                presenter.getHugeBillinfoRefresh(0);
            }
        };
        loadMoreAction = new Runnable() {
            @Override
            public void run() {
                if(idList != null && idList.size() > 0){
                    presenter.getHugeBillinfoLoadMore(idList.get(0));
                }
            }
        };
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        setLoadDataWhenVisible();
        //初始化presenter
        new LargeAmountPresenter(this, new BuyModle());
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void getHugeBillinfoRefreshSuccess(LargeAmountResponse largeAmountResponse) {
        List<LargeAmountItem> billInfo = largeAmountResponse.getBillInfo();
        if (billInfo != null && billInfo.size() > 0) {
            GALogger.d(TAG,"billInfo.size()    "+billInfo.size());
            reverseIdList(billInfo);
            List<LargeAmountItem> buyItems = removeDuplicate(billInfo);
            if(buyItems.size() <= Constants.RECYCLEVIEW_TOTALCOUNT){
                largeAmountAdapter.setTotalCount(buyItems.size());
            }else {
                largeAmountAdapter.setTotalCount(Integer.MAX_VALUE);
            }
            largeAmountAdapter.setList(buyItems);
        } else {
            emptyAnderrorView();
        }
        stopPulling();
    }

    private void emptyAnderrorView() {
        largeAmountAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        largeAmountAdapter.showError(true);
    }

    @Override
    public void getHugeBillinfoRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void getHugeBillinfoLoadMoreSuccess(LargeAmountResponse largeAmountResponse) {
        List<LargeAmountItem> billInfo = largeAmountResponse.getBillInfo();
        if (billInfo != null && billInfo.size() > 0) {
            reverseIdList(billInfo);
            largeAmountAdapter.getList().addAll(billInfo);
            List<LargeAmountItem> buyItems = removeDuplicate(largeAmountAdapter.getList());
            largeAmountAdapter.setTotalCount(Integer.MAX_VALUE);
            largeAmountAdapter.getList().clear();
            largeAmountAdapter.getOriginalDataList().clear();
            largeAmountAdapter.appendList(buyItems);
        }else {
            GALogger.d(TAG,"LoadMore  is  no");
            reverseIdList(null);
            largeAmountAdapter.setTotalCount(largeAmountAdapter.getItemRealCount());
            largeAmountAdapter.notifyDataSetChanged();
        }
        isRun = false;
    }

    public void reverseIdList(List<LargeAmountItem> billInfo){
        if(idList == null){
            idList = new ArrayList<Long>();
        }
        idList.clear();
        if(billInfo == null){
            return;
        }
        LargeAmountItem buyItem = billInfo.get(billInfo.size() - 1);
        if(buyItem != null && buyItem.getId() > 0) {
            idList.add(buyItem.getId());
        }
    }

    @Override
    public void getHugeBillinfoLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void setPresenter(LargeAmountContract.Presenter presenter) {
        this.presenter = (LargeAmountPresenter) presenter;
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

    public void stopPulling() {
        if (largeamountPullRefreshRecycler != null) {
            largeamountPullRefreshRecycler.onRefreshComplete();
        }
    }

    @Override
    public void onEmptyClick(View view) {

    }

    @Override
    public void onErrorClick(View view) {

    }

    @Override
    public void onItemClick(@NonNull PowerHolder<LargeAmountItem> holder, @NonNull View itemView, int position, LargeAmountItem item) {

    }

    /**
     * list集合对象去重
     * @param list
     * @return
     */
    public  List<LargeAmountItem> removeDuplicate(List<LargeAmountItem> list){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if(listTemp == null){
                listTemp = new LinkedList<>();
            }
            listTemp.clear();
            //使用流的distinct()方法进行去重
            list.stream().distinct().forEach(
                    new Consumer<LargeAmountItem>(){
                        @Override
                        public void accept(LargeAmountItem buyItem) {
                            if(buyItem != null){
                                listTemp.add(buyItem);
                            }
                        }
                    }
            );
        }else {
            //set集合保存的是引用不同地址的对象
            Set<LargeAmountItem> ts = new HashSet<LargeAmountItem>();
            ts.addAll(list);
            if(listTemp == null){
                listTemp = new LinkedList<>(ts);
            }else {
                listTemp.clear();
                listTemp = null;
                listTemp = new LinkedList<>(ts);
            }
        }
        return listTemp;
    }
}
