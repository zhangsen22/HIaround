package aimi.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.growalong.util.util.GALogger;
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

    @OnClick({R.id.iv_back, R.id.ll_alipay_click, R.id.ll_ylcard_click, R.id.ll_webchat_click,R.id.ll_yunshanfu_click, R.id.ll_juhema_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                addMakeStyleActivity.finish();
                break;
            case R.id.ll_alipay_click:
                AliPayListActivity.startThis(addMakeStyleActivity);
                break;
            case R.id.ll_ylcard_click:
                IdCastPayListActivity.startThis(addMakeStyleActivity);
                break;
            case R.id.ll_webchat_click:
                WebChatListActivity.startThis(addMakeStyleActivity);
                break;
            case R.id.ll_yunshanfu_click:
                YunShanFuListActivity.startThis(addMakeStyleActivity);
                break;
            case R.id.ll_juhema_click:
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
        GALogger.d(TAG, "onResume  ........");
    }
}
