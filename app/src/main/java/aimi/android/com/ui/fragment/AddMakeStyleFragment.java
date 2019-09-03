package aimi.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.XPopupCallback;

import aimi.android.com.ui.activity.LaCaraListActivity;
import butterknife.BindView;
import butterknife.OnClick;
import aimi.android.com.BaseFragment;
import aimi.android.com.R;
import aimi.android.com.app.AccountManager;
import aimi.android.com.ui.activity.AddMakeStyleActivity;
import aimi.android.com.ui.activity.AliPayListActivity;
import aimi.android.com.ui.activity.IdCastPayListActivity;
import aimi.android.com.ui.activity.WebChatListActivity;
import aimi.android.com.ui.activity.YunShanFuListActivity;

public class AddMakeStyleFragment extends BaseFragment {
    private static final String TAG = AddMakeStyleFragment.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_alipay)
    TextView tvAlipay;
    @BindView(R.id.ll_alipay_click)
    LinearLayout llAlipayClick;
    @BindView(R.id.tv_ylcard)
    TextView tvYlcard;
    @BindView(R.id.ll_ylcard_click)
    LinearLayout llYlcardClick;
    @BindView(R.id.tv_webchat)
    TextView tvWebchat;
    @BindView(R.id.ll_webchat_click)
    LinearLayout llWebchatClick;
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.tv_yunshanfu)
    TextView tvYunshanfu;
    @BindView(R.id.ll_yunshanfu_click)
    LinearLayout llYunshanfuClick;
    @BindView(R.id.tv_juhema)
    TextView tvJuhema;
    @BindView(R.id.ll_juhema_click)
    LinearLayout llJuhemaClick;
    @BindView(R.id.tv_lacara)
    TextView tvLacara;
    @BindView(R.id.ll_lacara_click)
    LinearLayout llLacaraClick;
    private AddMakeStyleActivity addMakeStyleActivity;

    public static AddMakeStyleFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        AddMakeStyleFragment fragment = new AddMakeStyleFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMakeStyleActivity = (AddMakeStyleActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.add_make_style_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        GALogger.d(TAG, "lazyLoadData  ........");
    }

    @OnClick({R.id.iv_back, R.id.ll_alipay_click, R.id.ll_ylcard_click, R.id.ll_webchat_click,R.id.ll_yunshanfu_click, R.id.ll_juhema_click,R.id.ll_lacara_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                addMakeStyleActivity.finish();
                break;
            case R.id.ll_alipay_click:
                AliPayListActivity.startThis(addMakeStyleActivity);
                break;
            case R.id.ll_ylcard_click:
                if (!AccountManager.getInstance().isHaveWechatPayee()) {
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
                            }).asConfirm("请先绑定微信收款方式", "",
                            "取消", "确定",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    WebChatListActivity.startThis(addMakeStyleActivity);
                                }
                            }, null, false)
                            .show();
                }else {
                    IdCastPayListActivity.startThis(addMakeStyleActivity);
                }
                break;
            case R.id.ll_webchat_click:
                WebChatListActivity.startThis(addMakeStyleActivity);
                break;
            case R.id.ll_yunshanfu_click:
                YunShanFuListActivity.startThis(addMakeStyleActivity);
                break;
            case R.id.ll_juhema_click:
                break;
            case R.id.ll_lacara_click:
                if (!AccountManager.getInstance().isHaveWechatPayee()) {
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
                            }).asConfirm("请先绑定微信收款方式", "",
                            "取消", "确定",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    WebChatListActivity.startThis(addMakeStyleActivity);
                                }
                            }, null, false)
                            .show();
                }else {
                    LaCaraListActivity.startThis(addMakeStyleActivity);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tvTitle.setText("添加收款方式");
        if (AccountManager.getInstance().isHaveAliPayee()) {
            tvAlipay.setText("已绑定");
        } else {
            tvAlipay.setText("未绑定");
        }
        if (AccountManager.getInstance().isHaveBankPayee()) {
            tvYlcard.setText("已绑定");
        } else {
            tvYlcard.setText("未绑定");
        }
        if (AccountManager.getInstance().isHaveWechatPayee()) {
            tvWebchat.setText("已绑定");
        } else {
            tvWebchat.setText("未绑定");
        }

        if (AccountManager.getInstance().isHaveCloudPayee()) {
            tvYunshanfu.setText("已绑定");
        } else {
            tvYunshanfu.setText("未绑定");
        }

        if (AccountManager.getInstance().isHaveLakalaPayee()) {
            tvLacara.setText("已绑定");
        } else {
            tvLacara.setText("未绑定");
        }
        GALogger.d(TAG, "onResume  ........");
    }
}
