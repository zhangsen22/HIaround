package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.Md5Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.handmark.pulltorefresh.library.internal.RecycleViewLoadingLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.AccountManager;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.AliPayPayeeItemModel;
import hiaround.android.com.modle.AliPayPayeeItemModelPayee;
import hiaround.android.com.modle.AliPayPayeeModel;
import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.modle.PaySetupModelAliPay;
import hiaround.android.com.presenter.AliPayListPresenter;
import hiaround.android.com.presenter.contract.AliPayListContract;
import hiaround.android.com.ui.activity.AliPayListActivity;
import hiaround.android.com.ui.activity.PaySettingActivity;
import hiaround.android.com.ui.adapter.AliPayListAdapter;
import hiaround.android.com.ui.adapter.poweradapter.AdapterLoader;
import hiaround.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import hiaround.android.com.ui.adapter.poweradapter.OnLoadMoreListener;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;
import hiaround.android.com.util.ToastUtil;

public class AliPayListFragment extends BaseFragment implements AliPayListContract.View, OnLoadMoreListener, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, AdapterLoader.OnItemClickListener<AliPayPayeeItemModel>,AliPayListAdapter.OnAliPayCheckListener {
    private static final String TAG = IdCastPayListFragment.class.getSimpleName();
    private static AliPayListActivity aliPayListActivity;
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.alipay_pull_refresh_recycler)
    PullToRefreshRecyclerView alipayPullRefreshRecycler;
    @BindView(R.id.tv_submit_forget_login)
    TextView tvSubmitForgetLogin;
    private AliPayListPresenter presenter;
    private RecyclerView mRecyclerView;
    private AliPayListAdapter aliPayListAdapter;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private static final int DEFAULT_TIME = 0;
    public List<Long> idList;

    public static AliPayListFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        AliPayListFragment fragment = new AliPayListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aliPayListActivity = (AliPayListActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.ali_pay_list_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("支付宝收款设置");
        alipayPullRefreshRecycler.setId(R.id.recycleView);
        alipayPullRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = alipayPullRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        aliPayListAdapter = new AliPayListAdapter(MyApplication.appContext,this);
        aliPayListAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        alipayPullRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        aliPayListAdapter.setLoadMoreListener(this);
        aliPayListAdapter.setEmptyClickListener(this);
        aliPayListAdapter.setErrorClickListener(this);
        aliPayListAdapter.setOnItemClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                presenter.aliPayListRefresh(1);
            }
        };
        loadMoreAction = new Runnable() {
            @Override
            public void run() {
                if (idList != null && idList.size() > 0) {
//                    presenter.paysetupBankLoadMore(idList.get(0));
                }
            }
        };
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void aliPayListRefreshSuccess(PaySetupModelAliPay paySetupModelAliPay) {
        AliPayPayeeModel aliPayeeObj = paySetupModelAliPay.getAliPayeeObj();
        if(aliPayeeObj != null){
            long defalut = aliPayeeObj.getDefaultId();
            List<AliPayPayeeItemModel> list = aliPayeeObj.getPayee();
            if (list != null && list.size() > 0) {
//            buyFragmentAdapter.setTotalCount(totalSize);
                aliPayListAdapter.setDefaultId(defalut);
                aliPayListAdapter.setList(list);
                AccountManager.getInstance().setHaveAliPayee(true);
            } else {
                emptyAnderrorView();
                AccountManager.getInstance().setHaveAliPayee(false);
            }
            stopPulling();
        }else {
            emptyAnderrorView();
            AccountManager.getInstance().setHaveAliPayee(false);
        }
    }

    private void emptyAnderrorView() {
        aliPayListAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        aliPayListAdapter.showError(true);
    }

    @Override
    public void aliPayListRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void aliPayListLoadMoreSuccess(PaySetupModelAliPay paySetupModelAliPay) {
    }

    @Override
    public void aliPayListLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void setDefaultPayAliPaySuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void detelePayAliPaySuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void setPresenter(AliPayListContract.Presenter presenter) {
        this.presenter = (AliPayListPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @OnClick({R.id.iv_back, R.id.tv_submit_forget_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                aliPayListActivity.finish();
                break;
            case R.id.tv_submit_forget_login:
                PaySettingActivity.startThis(aliPayListActivity,1,Constants.REQUESTCODE_15);
                break;
        }
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
        if (alipayPullRefreshRecycler != null) {
            alipayPullRefreshRecycler.onRefreshComplete();
        }
    }

    @Override
    public void onEmptyClick(View view) {

    }

    @Override
    public void onErrorClick(View view) {

    }

    @Override
    public void onItemClick(@NonNull PowerHolder<AliPayPayeeItemModel> holder, @NonNull View itemView, int position, AliPayPayeeItemModel item) {
    }

    @Override
    public void onAliPayCheck(int position, AliPayPayeeItemModel aliPayPayeeItemModel) {
        AliPayPayeeItemModelPayee payee = aliPayPayeeItemModel.getPayee();
        if(payee != null) {
            new XPopup.Builder(getContext())
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .autoOpenSoftInput(true)
//                        .moveUpToKeyboard(false) //是否移动到软键盘上面，默认为true
                    .asInputConfirm("请输入资金密码", "", "资金密码",true,
                            new OnInputConfirmListener() {
                                @Override
                                public void onConfirm(String text) {
                                    if(!TextUtils.isEmpty(text)){
                                        long currentTime = System.currentTimeMillis();
                                        presenter.setDefaultPayAliPay(1,payee.getId(), Md5Utils.getMD5(text+currentTime),currentTime);
                                    }else {
                                        ToastUtil.shortShow("请输入资金密码");
                                    }
                                }
                            })
                    .show();
        }
    }

    @Override
    public void onAliPayDelete(int position, AliPayPayeeItemModel aliPayPayeeItemModel) {
        AliPayPayeeItemModelPayee payee = aliPayPayeeItemModel.getPayee();
        if(payee != null) {
            new XPopup.Builder(getContext())
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .autoOpenSoftInput(true)
//                        .moveUpToKeyboard(false) //是否移动到软键盘上面，默认为true
                    .asInputConfirm("请输入资金密码", "", "资金密码",true,
                            new OnInputConfirmListener() {
                                @Override
                                public void onConfirm(String text) {
                                    if(!TextUtils.isEmpty(text)){
                                        long currentTime = System.currentTimeMillis();
                                        presenter.detelePay(1,payee.getId(), Md5Utils.getMD5(text+currentTime),currentTime);
                                    }else {
                                        ToastUtil.shortShow("请输入资金密码");
                                    }
                                }
                            })
                    .show();
        }
    }

    public void onActivityResultF() {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }
}
