package aimi.android.com.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.growalong.util.util.GALogger;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.handmark.pulltorefresh.library.internal.RecycleViewLoadingLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import aimi.android.com.BaseActivity;
import aimi.android.com.BaseFragment;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.app.Constants;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.MyEntrustinfoItem;
import aimi.android.com.modle.MyEntrustinfoResponse;
import aimi.android.com.modle.MySellOrBuyinfoItem;
import aimi.android.com.modle.MySellOrBuyinfoResponse;
import aimi.android.com.presenter.OrderItemDetailsPresenter;
import aimi.android.com.presenter.contract.OrderItemDetailsContract;
import aimi.android.com.presenter.modle.OrderItemDetailsModle;
import aimi.android.com.ui.activity.OrderDetailsActivity;
import aimi.android.com.ui.adapter.OrderBuyDetailsAdapter;
import aimi.android.com.ui.adapter.OrderEntrustDetailsAdapter;
import aimi.android.com.ui.adapter.OrderSellDetailsAdapter;
import aimi.android.com.ui.adapter.poweradapter.AdapterLoader;
import aimi.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import aimi.android.com.ui.adapter.poweradapter.OnLoadMoreListener;
import aimi.android.com.ui.adapter.poweradapter.PowerAdapter;
import aimi.android.com.ui.adapter.poweradapter.PowerHolder;

/**
 * 1:我的卖出    1:未完成  2:已完成  3:申诉中  4:已取消
 * 2:我的买入    1:未完成  2:已完成  3:申诉中  4:已取消
 * 3:我的委托单  1:购买委托  2:出售委托
 */
public class OrderItemDetailsFragment extends BaseFragment implements OnLoadMoreListener, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, OrderItemDetailsContract.View, OrderEntrustDetailsAdapter.OrderEntrustClickListenering, OrderSellDetailsAdapter.OrderSellClickListenering, OrderBuyDetailsAdapter.OrderBuyClickListenering {
    private static final String TAG = OrderItemDetailsFragment.class.getSimpleName();
    @BindView(R.id.order_pull_refresh_recycler)
    PullToRefreshRecyclerView orderPullRefreshRecycler;
    private OrderSellDetailsAdapter orderSellDetailsAdapter;
    private OrderBuyDetailsAdapter orderBuyDetailsAdapter;
    private OrderEntrustDetailsAdapter orderEntrustDetailsAdapter;
    private BaseActivity mContext;
    private OrderItemDetailsPresenter presenter;
    private RecyclerView mRecyclerView;
    private int parentType;
    private int childType;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private static final int DEFAULT_TIME = 0;
    public List<Long> sellIdList;
    public List<Long> buyIdList;
    public List<Long> entrustIdList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (BaseActivity) activity;
    }

    public static OrderItemDetailsFragment newInstance(int parentType,int childType) {
        Bundle arguments = new Bundle();
        arguments.putInt("parentType", parentType);
        arguments.putInt("childType", childType);
        OrderItemDetailsFragment fragment = new OrderItemDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentType = getArguments().getInt("parentType");
        childType = getArguments().getInt("childType");
        GALogger.d(TAG, "parentType    " + parentType+"    childType    "+childType);
    }

    @Override
    protected int getRootView() {
        return R.layout.order_item_details_fragment;
    }

    @Override
    protected void initView(View root) {
        orderPullRefreshRecycler.setId(R.id.recycleView);
        orderPullRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = orderPullRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        if(parentType == 1) {
            orderSellDetailsAdapter = new OrderSellDetailsAdapter(mContext,childType,this);
            orderSellDetailsAdapter.attachRecyclerView(mRecyclerView);
            orderSellDetailsAdapter.setLoadMoreListener(this);
            orderSellDetailsAdapter.setEmptyClickListener(this);
            orderSellDetailsAdapter.setErrorClickListener(this);
            orderSellDetailsAdapter.setOnItemClickListener(new AdapterLoader.OnItemClickListener<MySellOrBuyinfoItem>() {
                @Override
                public void onItemClick(@NonNull PowerHolder<MySellOrBuyinfoItem> holder, @NonNull View itemView, int position, MySellOrBuyinfoItem item) {
                    if(item != null){
                        OrderDetailsActivity.startThis(mContext,item);
                    }
                }
            });
        }else if(parentType == 2){
            orderBuyDetailsAdapter = new OrderBuyDetailsAdapter(mContext,childType,this);
            orderBuyDetailsAdapter.attachRecyclerView(mRecyclerView);
            orderBuyDetailsAdapter.setLoadMoreListener(this);
            orderBuyDetailsAdapter.setEmptyClickListener(this);
            orderBuyDetailsAdapter.setErrorClickListener(this);
            orderBuyDetailsAdapter.setOnItemClickListener(new AdapterLoader.OnItemClickListener<MySellOrBuyinfoItem>() {
                @Override
                public void onItemClick(@NonNull PowerHolder<MySellOrBuyinfoItem> holder, @NonNull View itemView, int position, MySellOrBuyinfoItem item) {
                    if(item != null){
                        OrderDetailsActivity.startThis(mContext,item);
                    }
                }
            });
        }else if(parentType == 3){
            orderEntrustDetailsAdapter = new OrderEntrustDetailsAdapter(MyApplication.appContext,childType,this);
            orderEntrustDetailsAdapter.attachRecyclerView(mRecyclerView);
            orderEntrustDetailsAdapter.setLoadMoreListener(this);
            orderEntrustDetailsAdapter.setEmptyClickListener(this);
            orderEntrustDetailsAdapter.setErrorClickListener(this);
        }
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        orderPullRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });

        refreshAction = new Runnable() {
            @Override
            public void run() {
                if(parentType == 1) {
                    presenter.mySellinfoRefresh(childType, 0);
                }else if(parentType == 2){
                    presenter.myBuyinfoRefresh(childType, 0);
                }else if(parentType == 3){
                    presenter.myBillInfoRefresh(childType-1, 0);
                }
            }
        };
        loadMoreAction = new Runnable() {
            @Override
            public void run() {
                if(parentType == 1) {
                    if(sellIdList != null && sellIdList.size() > 0){
                        presenter.mySellinfoLoadMore(childType, sellIdList.get(0));
                    }
                }else if(parentType == 2){
                    if(buyIdList != null && buyIdList.size() > 0){
                        presenter.myBuyinfoLoadMore(childType, buyIdList.get(0));
                    }
                }else if(parentType == 3){
                    if(entrustIdList != null && entrustIdList.size() > 0){
                        presenter.myBillInfoLoadMore(childType-1, entrustIdList.get(0));
                    }
                }
            }
        };
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        GALogger.d(TAG,"lazyLoadData");
        setLoadDataWhenVisible();
        //初始化presenter
        new OrderItemDetailsPresenter(this, new OrderItemDetailsModle());
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

    @Override
    public void onEmptyClick(View view) {

    }

    @Override
    public void onErrorClick(View view) {

    }

    @Override
    public void mySellinfoRefreshSuccess(MySellOrBuyinfoResponse mySellOrBuyinfoResponse) {
        List<MySellOrBuyinfoItem> info = mySellOrBuyinfoResponse.getInfo();
        GALogger.d(TAG,"info.size()    "+info.size());
        if (info != null && info.size() > 0) {
            GALogger.d(TAG,"info.size()    "+info.get(0).toString());
            reverseIdsellIdList(info);
            if(info.size() <= Constants.RECYCLEVIEW_TOTALCOUNT){
                orderSellDetailsAdapter.setTotalCount(info.size());
            }else {
                orderSellDetailsAdapter.setTotalCount(Integer.MAX_VALUE);
            }
            orderSellDetailsAdapter.setList(info);
        } else {
            emptyAnderrorView();
        }
        stopPulling();
    }

    @Override
    public void mySellinfoRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void mySellinfoLoadMoreSuccess(MySellOrBuyinfoResponse mySellOrBuyinfoResponse) {
        List<MySellOrBuyinfoItem> info = mySellOrBuyinfoResponse.getInfo();
        if (info != null && info.size() > 0) {
            reverseIdsellIdList(info);
            orderSellDetailsAdapter.setTotalCount(Integer.MAX_VALUE);
            orderSellDetailsAdapter.appendList(info);
        }else {
            GALogger.d(TAG,"LoadMore  is  no");
            reverseIdsellIdList(null);
            orderSellDetailsAdapter.setTotalCount(orderSellDetailsAdapter.getItemRealCount());
            orderSellDetailsAdapter.notifyDataSetChanged();
        }
        isRun = false;
    }

    public void reverseIdsellIdList(List<MySellOrBuyinfoItem> billInfo){
        if(sellIdList == null){
            sellIdList = new ArrayList<Long>();
        }
        sellIdList.clear();
        if(billInfo == null){
            return;
        }
        MySellOrBuyinfoItem buyItem = billInfo.get(billInfo.size() - 1);
        if(buyItem != null && buyItem.getId() > 0) {
            sellIdList.add(buyItem.getId());
        }
    }

    @Override
    public void mySellinfoLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void myBuyinfoRefreshSuccess(MySellOrBuyinfoResponse myBuyinfoResponse) {
        List<MySellOrBuyinfoItem> info = myBuyinfoResponse.getInfo();
        GALogger.d(TAG,"info.size()    "+info.size());
        if (info != null && info.size() > 0) {
            reverseIdbuyIdList(info);
            if(info.size() <= Constants.RECYCLEVIEW_TOTALCOUNT){
                orderBuyDetailsAdapter.setTotalCount(info.size());
            }else {
                orderBuyDetailsAdapter.setTotalCount(Integer.MAX_VALUE);
            }
            orderBuyDetailsAdapter.setList(info);
        } else {
            emptyAnderrorView();
        }
        stopPulling();
    }

    @Override
    public void myBuyinfoRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void myBuyinfoLoadMoreSuccess(MySellOrBuyinfoResponse myBuyinfoResponse) {
        List<MySellOrBuyinfoItem> info = myBuyinfoResponse.getInfo();
        if (info != null && info.size() > 0) {
            reverseIdbuyIdList(info);
            orderBuyDetailsAdapter.setTotalCount(Integer.MAX_VALUE);
            orderBuyDetailsAdapter.appendList(info);
        }else {
            GALogger.d(TAG,"LoadMore  is  no");
            reverseIdbuyIdList(null);
            orderBuyDetailsAdapter.setTotalCount(orderBuyDetailsAdapter.getItemRealCount());
            orderBuyDetailsAdapter.notifyDataSetChanged();
        }
        isRun = false;
    }

    public void reverseIdbuyIdList(List<MySellOrBuyinfoItem> billInfo){
        if(buyIdList == null){
            buyIdList = new ArrayList<Long>();
        }
        buyIdList.clear();
        if(billInfo == null){
            return;
        }
        MySellOrBuyinfoItem buyItem = billInfo.get(billInfo.size() - 1);
        if(buyItem != null && buyItem.getId() > 0) {
            buyIdList.add(buyItem.getId());
        }
    }

    @Override
    public void myBuyinfoLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void myBillInfoRefreshSuccess(MyEntrustinfoResponse myEntrustinfoResponse) {
        List<MyEntrustinfoItem> billInfo = myEntrustinfoResponse.getBillInfo();
        GALogger.d(TAG,"info.size()    "+billInfo.size());
        if (billInfo != null && billInfo.size() > 0) {
            reverseIdentrustIdList(billInfo);
            if(billInfo.size() <= Constants.RECYCLEVIEW_TOTALCOUNT){
                orderEntrustDetailsAdapter.setTotalCount(billInfo.size());
            }else {
                orderEntrustDetailsAdapter.setTotalCount(Integer.MAX_VALUE);
            }
            orderEntrustDetailsAdapter.setList(billInfo);
        } else {
            emptyAnderrorView();
        }
        stopPulling();
    }

    @Override
    public void myBillInfoRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void myBillInfoLoadMoreSuccess(MyEntrustinfoResponse myEntrustinfoResponse) {
        List<MyEntrustinfoItem> billInfo = myEntrustinfoResponse.getBillInfo();
        if (billInfo != null && billInfo.size() > 0) {
            reverseIdentrustIdList(billInfo);
            orderEntrustDetailsAdapter.setTotalCount(Integer.MAX_VALUE);
            orderEntrustDetailsAdapter.appendList(billInfo);
        }else {
            GALogger.d(TAG,"LoadMore  is  no");
            reverseIdentrustIdList(null);
            orderEntrustDetailsAdapter.setTotalCount(orderEntrustDetailsAdapter.getItemRealCount());
            orderEntrustDetailsAdapter.notifyDataSetChanged();
        }
        isRun = false;
    }

    public void reverseIdentrustIdList(List<MyEntrustinfoItem> billInfo){
        if(entrustIdList == null){
            entrustIdList = new ArrayList<Long>();
        }
        entrustIdList.clear();
        if(billInfo == null){
            return;
        }
        MyEntrustinfoItem buyItem = billInfo.get(billInfo.size() - 1);
        if(buyItem != null && buyItem.getId() > 0) {
            entrustIdList.add(buyItem.getId());
        }
    }

    @Override
    public void myBillInfoLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void cancelSuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void appealSuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void ordercancelSuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void setPresenter(OrderItemDetailsContract.Presenter presenter) {
        this.presenter = (OrderItemDetailsPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    private void emptyAnderrorView() {
        if(parentType == 1) {
            orderSellDetailsAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
            orderSellDetailsAdapter.showError(true);
        }else if(parentType == 2){
            orderBuyDetailsAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
            orderBuyDetailsAdapter.showError(true);
        }else if(parentType == 3){
            orderEntrustDetailsAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
            orderEntrustDetailsAdapter.showError(true);
        }
    }

    public void stopPulling() {
        if (orderPullRefreshRecycler != null) {
            orderPullRefreshRecycler.onRefreshComplete();
        }
    }

    @Override
    public void vv(final long id, final int type) {
        //带确认和取消按钮的弹窗
        new XPopup.Builder(getContext())
//                         .dismissOnTouchOutside(false)
                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                .setPopupCallback(new XPopupCallback() {
                    @Override
                    public void onShow() {
                        Log.e("tag", "onShow");
                    }
                    @Override
                    public void onDismiss() {
                        Log.e("tag", "onDismiss");
                    }
                }).asConfirm("撤销", "你确定要撤销吗?",
                "取消", "确定",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        presenter.cancel(id,type);
                    }
                }, null, false)
                .show();
    }

    @Override
    public void orderSellClick(final String tradeId) {
        //带确认和取消按钮的弹窗
        new XPopup.Builder(getContext())
//                         .dismissOnTouchOutside(false)
                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                .setPopupCallback(new XPopupCallback() {
                    @Override
                    public void onShow() {
                        Log.e("tag", "onShow");
                    }
                    @Override
                    public void onDismiss() {
                        Log.e("tag", "onDismiss");
                    }
                }).asConfirm("申诉", "你确定要申诉吗?",
                "取消", "确定",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        presenter.appeal(tradeId);
                    }
                }, null, false)
                .show();
    }

    @Override
    public void orderBuyClick(int type, final String tradeId) {//1:申诉  2:取消订单
        if(type == 1){
            //带确认和取消按钮的弹窗
            new XPopup.Builder(getContext())
//                         .dismissOnTouchOutside(false)
                    // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                    .setPopupCallback(new XPopupCallback() {
                        @Override
                        public void onShow() {
                            Log.e("tag", "onShow");
                        }
                        @Override
                        public void onDismiss() {
                            Log.e("tag", "onDismiss");
                        }
                    }).asConfirm("申诉", "你确定要申诉吗?",
                    "取消", "确定",
                    new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            presenter.appeal(tradeId);
                        }
                    }, null, false)
                    .show();
        }else if(type == 2){
            //带确认和取消按钮的弹窗
            new XPopup.Builder(getContext())
//                         .dismissOnTouchOutside(false)
                    // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                    .setPopupCallback(new XPopupCallback() {
                        @Override
                        public void onShow() {
                            Log.e("tag", "onShow");
                        }
                        @Override
                        public void onDismiss() {
                            Log.e("tag", "onDismiss");
                        }
                    }).asConfirm("订单取消", "你确定要取消此订单吗?",
                    "取消", "确定",
                    new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            presenter.ordercancel(tradeId);
                        }
                    }, null, false)
                    .show();
        }
    }

    public void onActivityResultOrderItemDetails(int requestCode) {
        if(mRecyclerView != null && presenter != null) {
            mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
        }
    }
}
