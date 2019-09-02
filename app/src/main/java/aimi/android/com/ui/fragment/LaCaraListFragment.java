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

import aimi.android.com.BaseFragment;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.app.AccountManager;
import aimi.android.com.app.Constants;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.LaCaraPayeeItemModel;
import aimi.android.com.modle.LaCaraPayeeItemModelPayee;
import aimi.android.com.modle.LaCaraPayeeModel;
import aimi.android.com.modle.PaySetupModelLaCara;
import aimi.android.com.presenter.LaCaraListPresenter;
import aimi.android.com.presenter.contract.LaCaraListContract;
import aimi.android.com.ui.activity.LaCaraListActivity;
import aimi.android.com.ui.activity.PaySettingActivity;
import aimi.android.com.ui.adapter.LaCaraListAdapter;
import aimi.android.com.ui.adapter.poweradapter.AdapterLoader;
import aimi.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import aimi.android.com.ui.adapter.poweradapter.OnLoadMoreListener;
import aimi.android.com.ui.adapter.poweradapter.PowerAdapter;
import aimi.android.com.ui.adapter.poweradapter.PowerHolder;
import aimi.android.com.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class LaCaraListFragment extends BaseFragment implements LaCaraListContract.View, LaCaraListAdapter.OnLaCaraCheckListener, OnLoadMoreListener, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, AdapterLoader.OnItemClickListener<LaCaraPayeeItemModel> {
    private static final String TAG = IdCastPayListFragment.class.getSimpleName();
    @BindView(R.id.lacara_pull_refresh_recycler)
    PullToRefreshRecyclerView lacaraPullRefreshRecycler;
    private LaCaraListActivity laCaraListActivity;
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_submit_forget_login)
    TextView tvSubmitForgetLogin;
    private LaCaraListPresenter presenter;
    private RecyclerView mRecyclerView;
    private LaCaraListAdapter laCaraListAdapter;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private static final int DEFAULT_TIME = 0;

    public static LaCaraListFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        LaCaraListFragment fragment = new LaCaraListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        laCaraListActivity = (LaCaraListActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.lacara_list_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("拉卡拉收款设置");
        lacaraPullRefreshRecycler.setId(R.id.recycleView);
        lacaraPullRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = lacaraPullRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        laCaraListAdapter = new LaCaraListAdapter(MyApplication.appContext,this);
        laCaraListAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        lacaraPullRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        laCaraListAdapter.setLoadMoreListener(this);
        laCaraListAdapter.setEmptyClickListener(this);
        laCaraListAdapter.setErrorClickListener(this);
        laCaraListAdapter.setOnItemClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                presenter.laCaraListRefresh(5);
            }
        };
        loadMoreAction = new Runnable() {
            @Override
            public void run() {
            }
        };
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @OnClick({R.id.iv_back, R.id.tv_submit_forget_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                laCaraListActivity.finish();
                break;
            case R.id.tv_submit_forget_login:
                //id:0                //如果为新加,设为0,如果为修改,此处为修改的收款方式的id
                PaySettingActivity.startThisLaCara(laCaraListActivity, 5, null, Constants.REQUESTCODE_21);
                break;
        }
    }

    @Override
    public void laCaraListRefreshSuccess(PaySetupModelLaCara paySetupModelLaCara) {
        LaCaraPayeeModel laCaraPayeeModel = paySetupModelLaCara.getLakalaPayeeObj();
        if(laCaraPayeeModel != null){
            long defalut = laCaraPayeeModel.getDefaultId();
            List<LaCaraPayeeItemModel> list = laCaraPayeeModel.getPayee();
            if (list != null && list.size() > 0) {
//            buyFragmentAdapter.setTotalCount(totalSize);
                laCaraListAdapter.setDefaultId(defalut);
                laCaraListAdapter.setList(list);
                AccountManager.getInstance().setHaveLakalaPayee(true);
            } else {
                emptyAnderrorView();
                AccountManager.getInstance().setHaveLakalaPayee(false);
            }
            stopPulling();
        }else {
            emptyAnderrorView();
            AccountManager.getInstance().setHaveLakalaPayee(false);
        }
    }

    @Override
    public void laCaraListRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void laCaraListLoadMoreSuccess(PaySetupModelLaCara paySetupModelLaCara) {

    }

    @Override
    public void laCaraListLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void setDefaultPayLaCaraSuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void deteleLaCaraSuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void setPresenter(LaCaraListContract.Presenter presenter) {
        this.presenter = (LaCaraListPresenter) presenter;
    }

    private void emptyAnderrorView() {
        laCaraListAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        laCaraListAdapter.showError(true);
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
        if (lacaraPullRefreshRecycler != null) {
            lacaraPullRefreshRecycler.onRefreshComplete();
        }
    }

    @Override
    public void onEmptyClick(View view) {

    }

    @Override
    public void onErrorClick(View view) {

    }

    public void onActivityResultF() {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void onLaCaraCheck(int position, LaCaraPayeeItemModelPayee laCaraPayeeItemModelPayee) {
        if(laCaraPayeeItemModelPayee != null) {
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
                                        presenter.setDefaultPaylaCara(5,laCaraPayeeItemModelPayee.getId(), Md5Utils.getMD5(text+currentTime),currentTime);
                                    }else {
                                        ToastUtil.shortShow("请输入资金密码");
                                    }
                                }
                            })
                    .show();
        }
    }

    @Override
    public void onLaCaraDelete(int position, LaCaraPayeeItemModelPayee laCaraPayeeItemModelPayee) {
        if(laCaraPayeeItemModelPayee != null) {
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
                                        presenter.detelePay(5,laCaraPayeeItemModelPayee.getId(), Md5Utils.getMD5(text+currentTime),currentTime);
                                    }else {
                                        ToastUtil.shortShow("请输入资金密码");
                                    }
                                }
                            })
                    .show();
        }
    }

    @Override
    public void onWebChatLaCaraEdit(int position, LaCaraPayeeItemModelPayee laCaraPayeeItemModelPayee) {

    }

    @Override
    public void onItemClick(@NonNull PowerHolder<LaCaraPayeeItemModel> holder, @NonNull View itemView, int position, LaCaraPayeeItemModel item) {
        LaCaraPayeeItemModelPayee payee = item.getPayee();
        if(payee != null) {
            //id:0                //如果为新加,设为0,如果为修改,此处为修改的收款方式的id
            PaySettingActivity.startThisLaCara(laCaraListActivity, 5, payee, Constants.REQUESTCODE_21);
        }
    }
}
