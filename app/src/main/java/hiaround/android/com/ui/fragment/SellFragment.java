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
import hiaround.android.com.modle.BuyItem;
import hiaround.android.com.modle.BuyResponse;
import hiaround.android.com.presenter.SellPresenter;
import hiaround.android.com.presenter.contract.SellContract;
import hiaround.android.com.presenter.modle.BuyModle;
import hiaround.android.com.ui.adapter.SellFragmentAdapter;
import hiaround.android.com.ui.adapter.poweradapter.AdapterLoader;
import hiaround.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import hiaround.android.com.ui.adapter.poweradapter.OnLoadMoreListener;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;

public class SellFragment extends BaseFragment implements SellContract.View, OnLoadMoreListener, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, AdapterLoader.OnItemClickListener<BuyItem> {
    private static final String TAG = SellFragment.class.getSimpleName();
    @BindView(R.id.sell_pull_refresh_recycler)
    PullToRefreshRecyclerView sellPullRefreshRecycler;
    private RecyclerView mRecyclerView;
    private SellFragmentAdapter sellFragmentAdapter;
    private SellPresenter sellPresenter;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private static final int DEFAULT_TIME = 0;
    public List<Long> idList;
    private List<BuyItem> listTemp;

    public static SellFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        SellFragment fragment = new SellFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GALogger.d(TAG,"onCreate");
    }

    @Override
    protected int getRootView() {
        return R.layout.sell_ragment;
    }

    @Override
    protected void initView(View root) {
        sellPullRefreshRecycler.setId(R.id.recycleView);
        sellPullRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = sellPullRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        sellFragmentAdapter = new SellFragmentAdapter(MyApplication.appContext);
        sellFragmentAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        sellPullRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        sellFragmentAdapter.setLoadMoreListener(this);
        sellFragmentAdapter.setEmptyClickListener(this);
        sellFragmentAdapter.setErrorClickListener(this);
        sellFragmentAdapter.setOnItemClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                sellPresenter.getSellRefresh(0);
            }
        };
        loadMoreAction = new Runnable() {
            @Override
            public void run() {
                if(idList != null && idList.size() > 0) {
                    sellPresenter.getSellLoadMore(idList.get(0));
                }
            }
        };
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        GALogger.d(TAG,"SellFragment  is  lazyLoadData");
        setLoadDataWhenVisible();
        //初始化presenter
        new SellPresenter(this, new BuyModle());
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
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

    private void emptyAnderrorView() {
        sellFragmentAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        sellFragmentAdapter.showError(true);
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

    public void stopPulling() {
        if (sellPullRefreshRecycler != null) {
            sellPullRefreshRecycler.onRefreshComplete();
        }
    }

    @Override
    public void getSellRefreshSuccess(BuyResponse buyResponse) {
        List<BuyItem> billInfo = buyResponse.getBillInfo();
        if (billInfo != null && billInfo.size() > 0) {
            reverseIdList(billInfo);
            List<BuyItem> buyItems = removeDuplicate(billInfo);
            if(buyItems.size() <= Constants.RECYCLEVIEW_TOTALCOUNT){
                sellFragmentAdapter.setTotalCount(buyItems.size());
            }else {
                sellFragmentAdapter.setTotalCount(Integer.MAX_VALUE);
            }
            sellFragmentAdapter.setList(buyItems);
        } else {
            emptyAnderrorView();
        }
        stopPulling();
    }

    @Override
    public void getSellRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void getSellLoadMoreSuccess(BuyResponse buyResponse) {
        List<BuyItem> billInfo = buyResponse.getBillInfo();
        if (billInfo != null && billInfo.size() > 0) {
            reverseIdList(billInfo);
            sellFragmentAdapter.getList().addAll(billInfo);
            List<BuyItem> buyItems = removeDuplicate(sellFragmentAdapter.getList());
            sellFragmentAdapter.setTotalCount(Integer.MAX_VALUE);
            sellFragmentAdapter.getList().clear();
            sellFragmentAdapter.getOriginalDataList().clear();
            sellFragmentAdapter.appendList(buyItems);
        }else {
            GALogger.d(TAG,"LoadMore  is  no");
            reverseIdList(null);
            sellFragmentAdapter.setTotalCount(sellFragmentAdapter.getItemRealCount());
            sellFragmentAdapter.notifyDataSetChanged();
        }
        isRun = false;
    }

    public void reverseIdList(List<BuyItem> billInfo){
        if(idList == null){
            idList = new ArrayList<Long>();
        }
        idList.clear();
        if(billInfo == null){
            return;
        }
        BuyItem buyItem = billInfo.get(billInfo.size() - 1);
        if(buyItem != null && buyItem.getId() > 0) {
            idList.add(buyItem.getId());
        }
    }

    @Override
    public void getSellLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void onEmptyClick(View view) {

    }

    @Override
    public void onErrorClick(View view) {

    }

    @Override
    public void onItemClick(@NonNull PowerHolder<BuyItem> holder, @NonNull View itemView, int position, BuyItem item) {

    }

    /**
     * list集合对象去重
     * @param list
     * @return
     */
    public  List<BuyItem> removeDuplicate(List<BuyItem> list){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if(listTemp == null){
                listTemp = new LinkedList<>();
            }
            listTemp.clear();
            //使用流的distinct()方法进行去重
            list.stream().distinct().forEach(
                    new Consumer<BuyItem>(){
                        @Override
                        public void accept(BuyItem buyItem) {
                            if(buyItem != null){
                                listTemp.add(buyItem);
                            }
                        }
                    }
            );
        }else {
            //set集合保存的是引用不同地址的对象
            Set<BuyItem> ts = new HashSet<BuyItem>();
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
