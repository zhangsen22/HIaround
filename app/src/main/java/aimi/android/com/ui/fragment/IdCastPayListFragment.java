package aimi.android.com.ui.fragment;

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
import aimi.android.com.BaseFragment;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.app.AccountManager;
import aimi.android.com.app.Constants;
import aimi.android.com.modle.BankPayeeItemModel;
import aimi.android.com.modle.BankPayeeItemModelPayee;
import aimi.android.com.modle.BankPayeeModel;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.PaySetupModelBank;
import aimi.android.com.presenter.IdCastPayListPresenter;
import aimi.android.com.presenter.contract.IdCastPayListContract;
import aimi.android.com.ui.activity.IdCastPayListActivity;
import aimi.android.com.ui.activity.PaySettingActivity;
import aimi.android.com.ui.adapter.IdCastPayListAdapter;
import aimi.android.com.ui.adapter.poweradapter.AdapterLoader;
import aimi.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import aimi.android.com.ui.adapter.poweradapter.OnLoadMoreListener;
import aimi.android.com.ui.adapter.poweradapter.PowerAdapter;
import aimi.android.com.ui.adapter.poweradapter.PowerHolder;
import aimi.android.com.util.ToastUtil;

public class IdCastPayListFragment extends BaseFragment implements IdCastPayListContract.View, OnLoadMoreListener, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, AdapterLoader.OnItemClickListener<BankPayeeItemModel>,IdCastPayListAdapter.OnIdCastCheckListener {
    private static final String TAG = IdCastPayListFragment.class.getSimpleName();
    private static IdCastPayListActivity idCastPayListActivity;
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.idcastpay_pull_refresh_recycler)
    PullToRefreshRecyclerView idcastpayPullRefreshRecycler;
    @BindView(R.id.tv_submit_forget_login)
    TextView tvSubmitForgetLogin;
    private IdCastPayListPresenter presenter;

    private RecyclerView mRecyclerView;
    private IdCastPayListAdapter idCastPayListAdapter;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private static final int DEFAULT_TIME = 0;
    public List<Long> idList;

    public static IdCastPayListFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        IdCastPayListFragment fragment = new IdCastPayListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idCastPayListActivity = (IdCastPayListActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.id_cast_pay_list_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("银行卡收款设置");
        idcastpayPullRefreshRecycler.setId(R.id.recycleView);
        idcastpayPullRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = idcastpayPullRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        idCastPayListAdapter = new IdCastPayListAdapter(MyApplication.appContext,this);
        idCastPayListAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        idcastpayPullRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        idCastPayListAdapter.setLoadMoreListener(this);
        idCastPayListAdapter.setEmptyClickListener(this);
        idCastPayListAdapter.setErrorClickListener(this);
        idCastPayListAdapter.setOnItemClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                presenter.paysetupBankRefresh(3);
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

    @OnClick({R.id.iv_back,R.id.tv_submit_forget_login})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                idCastPayListActivity.finish();
                break;
            case R.id.tv_submit_forget_login:
                PaySettingActivity.startThis(idCastPayListActivity, 3,Constants.REQUESTCODE_16);
                break;
        }
    }

    @Override
    public void paysetupBankRefreshSuccess(PaySetupModelBank paySetupModelBank) {
        BankPayeeModel bankPayeeObj = paySetupModelBank.getBankPayeeObj();
        if (bankPayeeObj != null) {
            long defalut = bankPayeeObj.getDefaultId();
            List<BankPayeeItemModel> list = bankPayeeObj.getPayee();
            if (list != null && list.size() > 0) {
//            buyFragmentAdapter.setTotalCount(totalSize);
                idCastPayListAdapter.setDefaultId(defalut);
                idCastPayListAdapter.setList(list);
                AccountManager.getInstance().setHaveBankPayee(true);
            } else {
                emptyAnderrorView();
                AccountManager.getInstance().setHaveBankPayee(false);
            }
            stopPulling();
        }else {
            emptyAnderrorView();
            AccountManager.getInstance().setHaveBankPayee(false);
        }
    }

    private void emptyAnderrorView() {
        idCastPayListAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        idCastPayListAdapter.showError(true);
    }

    @Override
    public void paysetupBankRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void paysetupBankLoadMoreSuccess(PaySetupModelBank paySetupModelBank) {}

    @Override
    public void paysetupBankLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void setDefaultPayIdCastSuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void detelePayBankSuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void setPresenter(IdCastPayListContract.Presenter presenter) {
        this.presenter = (IdCastPayListPresenter) presenter;
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
        if (idcastpayPullRefreshRecycler != null) {
            idcastpayPullRefreshRecycler.onRefreshComplete();
        }
    }

    @Override
    public void onEmptyClick(View view) {

    }

    @Override
    public void onErrorClick(View view) {

    }

    @Override
    public void onItemClick(@NonNull PowerHolder<BankPayeeItemModel> holder, @NonNull View itemView, int position, BankPayeeItemModel item) {
    }

    @Override
    public void onIdCastCheck(int position, BankPayeeItemModel bankPayeeItemModel) {
        BankPayeeItemModelPayee payee = bankPayeeItemModel.getPayee();
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
                                        presenter.setDefaultPayIdCast(3,payee.getId(), Md5Utils.getMD5(text+currentTime),currentTime);
                                    }else {
                                        ToastUtil.shortShow("请输入资金密码");
                                    }
                                }
                            })
                    .show();
        }
    }

    @Override
    public void onIdCastDelete(int position, BankPayeeItemModel bankPayeeItemModel) {
        BankPayeeItemModelPayee payee = bankPayeeItemModel.getPayee();
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
                                        presenter.detelePay(3,payee.getId(), Md5Utils.getMD5(text+currentTime),currentTime);
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
