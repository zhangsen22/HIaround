package hiaround.android.com.ui.fragment;

import android.os.Bundle;
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
import hiaround.android.com.modle.PaySetupModelWebChat;
import hiaround.android.com.modle.WeChatPayeeItemModel;
import hiaround.android.com.modle.WeChatPayeeItemModelPayee;
import hiaround.android.com.modle.WeChatPayeeModel;
import hiaround.android.com.modle.WebChatEditModle;
import hiaround.android.com.presenter.WebChatListPresenter;
import hiaround.android.com.presenter.contract.WebChatListContract;
import hiaround.android.com.ui.activity.PaySettingActivity;
import hiaround.android.com.ui.activity.WebChatListActivity;
import hiaround.android.com.ui.adapter.WebChatListAdapter;
import hiaround.android.com.ui.adapter.poweradapter.LoadMoreScrollListener;
import hiaround.android.com.ui.adapter.poweradapter.OnLoadMoreListener;
import hiaround.android.com.ui.adapter.poweradapter.PowerAdapter;
import hiaround.android.com.util.SharedPreferencesUtils;
import hiaround.android.com.util.ToastUtil;

public class WebChatListFragment extends BaseFragment implements WebChatListContract.View, OnLoadMoreListener, PowerAdapter.OnEmptyClickListener, PowerAdapter.OnErrorClickListener, WebChatListAdapter.OnWebChatCheckListener {
    private static final String TAG = IdCastPayListFragment.class.getSimpleName();
    private static WebChatListActivity webChatListActivity;
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.webchat_pull_refresh_recycler)
    PullToRefreshRecyclerView webchatPullRefreshRecycler;
    @BindView(R.id.tv_submit_forget_login)
    TextView tvSubmitForgetLogin;
    private WebChatListPresenter presenter;

    private RecyclerView mRecyclerView;
    private WebChatListAdapter webChatListAdapter;
    private Runnable refreshAction;
    private Runnable loadMoreAction;
    private boolean isRun;
    private static final int DEFAULT_TIME = 0;
    public List<Long> idList;

    public static WebChatListFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        WebChatListFragment fragment = new WebChatListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webChatListActivity = (WebChatListActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.web_chat_list_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("微信收款设置");
        webchatPullRefreshRecycler.setId(R.id.recycleView);
        webchatPullRefreshRecycler.setHeaderLayout(new RecycleViewLoadingLayout(MyApplication.appContext));
        mRecyclerView = webchatPullRefreshRecycler.getRefreshableView();
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        webChatListAdapter = new WebChatListAdapter(MyApplication.appContext,this);
        webChatListAdapter.attachRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new LoadMoreScrollListener(mRecyclerView));
        webchatPullRefreshRecycler.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        });
        webChatListAdapter.setLoadMoreListener(this);
        webChatListAdapter.setEmptyClickListener(this);
        webChatListAdapter.setErrorClickListener(this);

        refreshAction = new Runnable() {
            @Override
            public void run() {
                presenter.webChatListRefresh(2);
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

    @OnClick({R.id.iv_back, R.id.tv_submit_forget_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                webChatListActivity.finish();
                break;
            case R.id.tv_submit_forget_login:
                boolean wxPayLock = SharedPreferencesUtils.getBoolean(Constants.WXPAYLOCK, false);
                if(wxPayLock){
                    ToastUtil.shortShow(MyApplication.appContext.getResources().getString(R.string.text33));
                }else {
                    //id:0                //如果为新加,设为0,如果为修改,此处为修改的收款方式的id
                    PaySettingActivity.startThis(webChatListActivity,2,null,Constants.REQUESTCODE_17);
                }
                break;
        }
    }

    @Override
    public void webChatListRefreshSuccess(PaySetupModelWebChat paySetupModelWebChat) {
        WeChatPayeeModel wechatPayee = paySetupModelWebChat.getWechatPayee();
        if(wechatPayee != null){
            long defalut = wechatPayee.getDefaultId();
            List<WeChatPayeeItemModel> list = wechatPayee.getPayee();
            if (list != null && list.size() > 0) {
//            buyFragmentAdapter.setTotalCount(totalSize);
                webChatListAdapter.setDefaultId(defalut);
                webChatListAdapter.setList(list);
                AccountManager.getInstance().setHaveWechatPayee(true);
            } else {
                emptyAnderrorView();
                AccountManager.getInstance().setHaveWechatPayee(false);
            }
            stopPulling();
        }else {
            emptyAnderrorView();
            AccountManager.getInstance().setHaveWechatPayee(false);
        }
    }

    private void emptyAnderrorView() {
        webChatListAdapter.setErrorView(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.no_data_view, mRecyclerView, false));
        webChatListAdapter.showError(true);
    }

    @Override
    public void webChatListRefreshError() {
        stopPulling();
        emptyAnderrorView();
    }

    @Override
    public void webChatListLoadMoreSuccess(PaySetupModelWebChat paySetupModelWebChat) {
//        List<MessageCenterItem> msg = messageCenterResponse.getMsg();
//        if (msg != null && msg.size() > 0) {
//            if(idList == null){
//                idList = new ArrayList<Long>();
//            }
//            idList.clear();
//            for (MessageCenterItem messageCenterItem: msg) {
//                idList.add(messageCenterItem.getId());
//            }
//            Collections.reverse(idList);
////            buyFragmentAdapter.setTotalCount(totalSize);
//            idCastPayAdapter.appendList(msg);
//        }
//        isRun = false;
    }

    @Override
    public void webChatListLoadMoreError() {
        stopPulling();
        isRun = false;
    }

    @Override
    public void setDefaultPayWebChatSuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void deteleWebChatSuccess(BaseBean baseBean) {
        mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
    }

    @Override
    public void setPresenter(WebChatListContract.Presenter presenter) {
        this.presenter = (WebChatListPresenter) presenter;
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
        if (webchatPullRefreshRecycler != null) {
            webchatPullRefreshRecycler.onRefreshComplete();
        }
    }

    @Override
    public void onEmptyClick(View view) {

    }

    @Override
    public void onErrorClick(View view) {

    }

    @Override
    public void onWebChatCheck(int position, WeChatPayeeItemModelPayee payee) {
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
                                        presenter.setDefaultPayWebChat(2,payee.getId(), Md5Utils.getMD5(text+currentTime),currentTime);
                                    }else {
                                        ToastUtil.shortShow("请输入资金密码");
                                    }
                                }
                            })
                    .show();
    }

    @Override
    public void onWebChatDelete(int position, WeChatPayeeItemModelPayee payee) {
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
                                        presenter.detelePay(2,payee.getId(), Md5Utils.getMD5(text+currentTime),currentTime);
                                    }else {
                                        ToastUtil.shortShow("请输入资金密码");
                                    }
                                }
                            })
                    .show();
    }

    @Override
    public void onWebChatEdit(int position, WeChatPayeeItemModelPayee payee) {
        //id:0                //如果为新加,设为0,如果为修改,此处为修改的收款方式的id
        PaySettingActivity.startThis(webChatListActivity,2,payee,Constants.REQUESTCODE_17);
    }

    public void onActivityResultF() {
        MyApplication.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.postDelayed(refreshAction, DEFAULT_TIME);
            }
        },250);
    }


    @Override
    public void reWechatSuccess(WebChatEditModle webChatEditModle, WeChatPayeeItemModelPayee payee) {
//id:0                //如果为新加,设为0,如果为修改,此处为修改的收款方式的id
        PaySettingActivity.startThis(webChatListActivity,2,payee,Constants.REQUESTCODE_17);
    }

    @Override
    public void onWebChatReLogin(int position, WeChatPayeeItemModelPayee payee) {
        if(payee != null){
            presenter.reWechat(payee.getId(),payee);
        }
    }
}
