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
import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.modle.PaySetupModelYunShanFu;
import hiaround.android.com.modle.YunShanFuPayeeItemModel;
import hiaround.android.com.modle.YunShanFuPayeeItemModelPayee;
import hiaround.android.com.modle.YunShanFuPayeeModel;
import hiaround.android.com.presenter.YunShanFuListPresenter;
import hiaround.android.com.presenter.contract.YunShanFuListContract;
import hiaround.android.com.ui.activity.PaySettingActivity;
import hiaround.android.com.ui.activity.YunShanFuListActivity;
import hiaround.android.com.ui.activity.YunShanFuLoginActivity;
import hiaround.android.com.ui.adapter.YunShanFuListAdapter;
import hiaround.android.com.ui.adapter.poweradapter.AdapterLoader;
import hiaround.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import hiaround.android.com.ui.adapter.poweradapter.OnLoadMoreListener;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.ui.adapter.poweradapter.PowerHolder;
import hiaround.android.com.util.ToastUtil;

public class YunShanFuListFragment extends BaseFragment implements YunShanFuListContract.View, YunShanFuListAdapter.OnYunShanFuCheckListener, OnLoadMoreListener, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, AdapterLoader.OnItemClickListener<YunShanFuPayeeItemModel> {
    private static final String TAG = IdCastPayListFragment.class.getSimpleName();
    @BindView(R.id.yunshanfu_pull_refresh_recycler)
    PullToRefreshRecyclerView yunshanfuPullRefreshRecycler;
    private YunShanFuListActivity yunShanFuListActivity;
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_submit_forget_login)
    TextView tvSubmitForgetLogin;
    private YunShanFuListPresenter presenter;
    private RecyclerView mRecyclerView;
    private YunShanFuListAdapter yunShanFuListAdapter;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private static final int DEFAULT_TIME = 0;

    public static YunShanFuListFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        YunShanFuListFragment fragment = new YunShanFuListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yunShanFuListActivity = (YunShanFuListActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.yunshanfu_list_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("云闪付收款设置");
        yunshanfuPullRefreshRecycler.setId(R.id.recycleView);
        yunshanfuPullRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = yunshanfuPullRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        yunShanFuListAdapter = new YunShanFuListAdapter(MyApplication.appContext,this);
        yunShanFuListAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        yunshanfuPullRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        yunShanFuListAdapter.setLoadMoreListener(this);
        yunShanFuListAdapter.setEmptyClickListener(this);
        yunShanFuListAdapter.setErrorClickListener(this);
        yunShanFuListAdapter.setOnItemClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                presenter.yunShanFuListRefresh(4);
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
                yunShanFuListActivity.finish();
                break;
            case R.id.tv_submit_forget_login:
                //id:0                //如果为新加,设为0,如果为修改,此处为修改的收款方式的id
                PaySettingActivity.startThisYunShanFu(yunShanFuListActivity, 4, null, Constants.REQUESTCODE_19);
                break;
        }
    }

    @Override
    public void yunShanFuListRefreshSuccess(PaySetupModelYunShanFu paySetupModelYunShanFu) {
        YunShanFuPayeeModel yunShanFuPayeeModel = paySetupModelYunShanFu.getCloudPayeeObj();
        if(yunShanFuPayeeModel != null){
            long defalut = yunShanFuPayeeModel.getDefaultId();
            List<YunShanFuPayeeItemModel> list = yunShanFuPayeeModel.getPayee();
            if (list != null && list.size() > 0) {
//            buyFragmentAdapter.setTotalCount(totalSize);
                yunShanFuListAdapter.setDefaultId(defalut);
                yunShanFuListAdapter.setList(list);
                AccountManager.getInstance().setHaveCloudPayee(true);
            } else {
                emptyAnderrorView();
                AccountManager.getInstance().setHaveCloudPayee(false);
            }
            stopPulling();
        }else {
            emptyAnderrorView();
            AccountManager.getInstance().setHaveCloudPayee(false);
        }
    }

    @Override
    public void yunShanFuListRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void yunShanFuListLoadMoreSuccess(PaySetupModelYunShanFu paySetupModelYunShanFu) {

    }

    @Override
    public void yunShanFuListLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void setDefaultPayYunShanFuSuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void deteleYunShanFuSuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void setPresenter(YunShanFuListContract.Presenter presenter) {
        this.presenter = (YunShanFuListPresenter) presenter;
    }

    private void emptyAnderrorView() {
        yunShanFuListAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        yunShanFuListAdapter.showError(true);
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
        if (yunshanfuPullRefreshRecycler != null) {
            yunshanfuPullRefreshRecycler.onRefreshComplete();
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
    public void onYunShanFuCheck(int position, YunShanFuPayeeItemModelPayee yunShanFuPayeeItemModelPayee) {
        if(yunShanFuPayeeItemModelPayee != null) {
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
                                        presenter.setDefaultPayyunShanFu(4,yunShanFuPayeeItemModelPayee.getId(), Md5Utils.getMD5(text+currentTime),currentTime);
                                    }else {
                                        ToastUtil.shortShow("请输入资金密码");
                                    }
                                }
                            })
                    .show();
        }
    }

    @Override
    public void onYunShanFuDelete(int position, YunShanFuPayeeItemModelPayee yunShanFuPayeeItemModelPayee) {
        if(yunShanFuPayeeItemModelPayee != null) {
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
                                        presenter.detelePay(4,yunShanFuPayeeItemModelPayee.getId(), Md5Utils.getMD5(text+currentTime),currentTime);
                                    }else {
                                        ToastUtil.shortShow("请输入资金密码");
                                    }
                                }
                            })
                    .show();
        }
    }

    @Override
    public void onWebChatYunShanFuEdit(int position, YunShanFuPayeeItemModelPayee yunShanFuPayeeItemModelPayee) {
        YunShanFuLoginActivity.launchVerifyCodeForResult(yunShanFuListActivity, Constants.YUNSHANFUURL,yunShanFuPayeeItemModelPayee.getId(), Constants.REQUESTCODE_19);
    }

    @Override
    public void onItemClick(@NonNull PowerHolder<YunShanFuPayeeItemModel> holder, @NonNull View itemView, int position, YunShanFuPayeeItemModel item) {
        YunShanFuPayeeItemModelPayee payee = item.getPayee();
        if(payee != null) {
            //id:0                //如果为新加,设为0,如果为修改,此处为修改的收款方式的id
            PaySettingActivity.startThisYunShanFu(yunShanFuListActivity, 4, payee, Constants.REQUESTCODE_19);
        }
    }
}
