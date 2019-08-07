package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.PackageUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.AccountManager;
import hiaround.android.com.app.Constants;
import hiaround.android.com.presenter.CenterPresenter;
import hiaround.android.com.presenter.contract.CenterContract;
import hiaround.android.com.presenter.modle.CenterModle;
import hiaround.android.com.ui.activity.AddMakeStyleActivity;
import hiaround.android.com.ui.activity.IdentityActivity;
import hiaround.android.com.ui.activity.LoginActivity;
import hiaround.android.com.ui.activity.MainActivity;
import hiaround.android.com.ui.activity.MessageCenterActivity;
import hiaround.android.com.ui.activity.RecommendToFriendsActivity;
import hiaround.android.com.ui.activity.WebViewActivity;
import hiaround.android.com.util.ToastUtil;

public class CenterFragment extends BaseFragment implements CenterContract.View {
    private static final String TAG = CenterFragment.class.getSimpleName();
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.ll_shenfencard)
    LinearLayout tvShenfencard;
    @BindView(R.id.ll_add_sk_type)
    LinearLayout llAddSkType;
    @BindView(R.id.ll_tj_friend)
    LinearLayout llTjFriend;
    @BindView(R.id.ll_lx_kf)
    LinearLayout llLxKf;
    @BindView(R.id.ll_center_message)
    LinearLayout llCenterMessage;
    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.ll_center_bg)
    FrameLayout llCenterBg;
    @BindView(R.id.ll_center_bg1)
    FrameLayout llCenterBg1;
    @BindView(R.id.iv_roletype)
    ImageView ivRoletype;
    @BindView(R.id.iv_apitype)
    ImageView ivApitype;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.tv_shenfencard_status)
    TextView tvShenfencardStatus;
    private MainActivity mainActivity;
    private CenterPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    public static CenterFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        CenterFragment fragment = new CenterFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected int getRootView() {
        return R.layout.center_fragment;
    }

    @Override
    protected void initView(View root) {
        GALogger.d(TAG, "CenterFragment   is    initView");
        setRootViewPaddingTop(llCenterBg);
        setRootViewPaddingTop(llCenterBg1);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        setLoadDataWhenVisible();
        //初始化presenter
        new CenterPresenter(this, new CenterModle());
        initUserInfo();
    }

    @OnClick({R.id.iv_edit, R.id.ll_shenfencard, R.id.ll_add_sk_type, R.id.ll_tj_friend, R.id.ll_lx_kf, R.id.ll_center_message, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_edit:
                new XPopup.Builder(getContext())
                        .dismissOnBackPressed(false)
                        .dismissOnTouchOutside(false)
                        .autoOpenSoftInput(true)
//                        .moveUpToKeyboard(false) //是否移动到软键盘上面，默认为true
                        .asInputConfirm("请输入要修改的昵称", "", "昵称", false,
                                new OnInputConfirmListener() {
                                    @Override
                                    public void onConfirm(String text) {
                                        if (!TextUtils.isEmpty(text)) {
                                            presenter.changeNickname(text);
                                        } else {
                                            ToastUtil.shortShow("请输入要修改的昵称");
                                        }
                                    }
                                })
                        .show();
                break;
            case R.id.ll_shenfencard:
                int iDstatus = AccountManager.getInstance().getIDstatus();//0未验证，1等待人工审核 2 已验证 99 验证失败
                if (iDstatus == 0 || iDstatus == 99) {
                    IdentityActivity.startThis(mainActivity, Constants.REQUESTCODE_10);
                }
                break;
            case R.id.ll_add_sk_type:
                AddMakeStyleActivity.startThis(mainActivity);
                break;
            case R.id.ll_tj_friend:
                RecommendToFriendsActivity.startThis(mainActivity);
                break;
            case R.id.ll_lx_kf:
                WebViewActivity.launchVerifyCode(MyApplication.appContext, Constants.KEFUANDHELP, true);
                break;
            case R.id.ll_center_message:
                MessageCenterActivity.startThis(mainActivity);
                break;
            case R.id.tv_logout:
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
                        }).asConfirm("你确定要退出此账号吗?", "",
                        "取消", "确定",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                LoginActivity.startThis(mainActivity);
                                mainActivity.finish();
                            }
                        }, null, false)
                        .show();
                break;
        }
    }

    @Override
    public void changeNicknameSuccess(String nickname) {
        AccountManager.getInstance().setNickname(nickname);
        tvUsername.setText(AccountManager.getInstance().getNickname());
    }

    @Override
    public void setPresenter(CenterContract.Presenter presenter) {
        this.presenter = (CenterPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    public void onActivityResultCenter(int requestCode) {
        initUserInfo();
    }

    private void initUserInfo() {
        tvUsername.setText(AccountManager.getInstance().getNickname());
        tvAccount.setText(AccountManager.getInstance().getPhoneNumber());
        tvVersionCode.setText(PackageUtil.getAppVersionName(MyApplication.appContext));
        if (AccountManager.getInstance().getRoleType() == 2) {
            ivRoletype.setImageResource(R.mipmap.bs);
            ivRoletype.setVisibility(View.VISIBLE);
        } else if (AccountManager.getInstance().getRoleType() == 1) {
            ivRoletype.setImageResource(R.mipmap.st);
            ivRoletype.setVisibility(View.VISIBLE);
        } else {
            ivRoletype.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }

        if (AccountManager.getInstance().getApiType() == 1) {
            ivApitype.setImageResource(R.mipmap.st);
            ivApitype.setVisibility(View.VISIBLE);
        } else {
            ivApitype.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }

        int iDstatus = AccountManager.getInstance().getIDstatus();//0未验证，1等待人工审核 2 已验证 99 验证失败
        if(iDstatus == 0){
            tvShenfencardStatus.setText("未验证");
        }else if(iDstatus == 1){
            tvShenfencardStatus.setText("等待人工审核");
        }else if(iDstatus == 2){
            tvShenfencardStatus.setText("已验证");
        }else if(iDstatus == 99){
            tvShenfencardStatus.setText("验证失败");
        }
    }
}
