package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.commons.codec.binary.Base64;
import com.growalong.util.util.GALogger;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.AccountManager;
import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.modle.SmsCodeResponse;
import hiaround.android.com.presenter.BalancePassWordPresenter;
import hiaround.android.com.presenter.contract.BalancePassWordContract;
import hiaround.android.com.presenter.modle.BalancePassWordModle;
import hiaround.android.com.ui.activity.BalancePassWordActivity;
import hiaround.android.com.util.RSAUtil;
import hiaround.android.com.util.ToastUtil;

public class BalancePassWordFragment extends BaseFragment implements BalancePassWordContract.View {
    private static final String TAG = BalancePassWordFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_balance_password)
    EditText etBalancePassword;
    @BindView(R.id.etre_balance_password)
    EditText etreBalancePassword;
    @BindView(R.id.tv_balance_phonenumber)
    TextView tvBalancePhonenumber;
    @BindView(R.id.et_balance_smscode)
    EditText etBalanceSmscode;
    @BindView(R.id.tv_get_balance_smscode)
    TextView tvGetBalanceSmscode;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private BalancePassWordActivity balancePassWordActivity;
    private BalancePassWordPresenter balancePassWordPresenter;
    private String publicKey = "";
    private String phoneNumber;

    /**
     * 倒计时60秒，一次1秒
     */
    private CountDownTimer timer;

    public static BalancePassWordFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        BalancePassWordFragment fragment = new BalancePassWordFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        balancePassWordActivity = (BalancePassWordActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.balance_password_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("设置资金密码");
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        phoneNumber = AccountManager.getInstance().getPhoneNumber();
        if(!TextUtils.isEmpty(phoneNumber)){
            tvBalancePhonenumber.setText(phoneNumber);
        }
        //初始化presenter
        new BalancePassWordPresenter(this, new BalancePassWordModle());
    }

    @OnClick({R.id.iv_back, R.id.tv_get_balance_smscode, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                balancePassWordActivity.finish();
                break;
            case R.id.tv_get_balance_smscode:
                String balancePassword1 = etBalancePassword.getText().toString().trim();
                if(TextUtils.isEmpty(balancePassword1)){
                    ToastUtil.shortShow("请输入资金密码");
                    return;
                }

                String rebalancePassword1 = etreBalancePassword.getText().toString().trim();
                if(TextUtils.isEmpty(rebalancePassword1)){
                    ToastUtil.shortShow("请输入确认资金密码");
                    return;
                }
                if(!rebalancePassword1.equals(balancePassword1)){
                    ToastUtil.shortShow("两次密码不一致");
                    return;
                }
                balancePassWordPresenter.senSenSmsCode(phoneNumber);
                break;
            case R.id.tv_submit:
                String balancePassword = etBalancePassword.getText().toString().trim();
                if(TextUtils.isEmpty(balancePassword)){
                    ToastUtil.shortShow("请输入资金密码");
                    return;
                }

                String rebalancePassword = etreBalancePassword.getText().toString().trim();
                if(TextUtils.isEmpty(rebalancePassword)){
                    ToastUtil.shortShow("请输入确认资金密码");
                    return;
                }

                if(!rebalancePassword.equals(balancePassword)){
                    ToastUtil.shortShow("两次密码不一致");
                    return;
                }

                String balanceSmscode = etBalanceSmscode.getText().toString().trim();
                if(TextUtils.isEmpty(balanceSmscode)){
                    ToastUtil.shortShow("输入短信验证码");
                    return;
                }

                if (TextUtils.isEmpty(publicKey)) {
                    ToastUtil.shortShow("请获取验证码");
                    return;
                }
                try {
                    String s = Base64.encodeBase64String(RSAUtil.encryptByPublicKey(rebalancePassword, publicKey));
                    balancePassWordPresenter.changeFinancePwd(s,balanceSmscode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void senSenSmsCodeSuccess(SmsCodeResponse smsCodeResponse) {
        if(smsCodeResponse != null){
            publicKey = smsCodeResponse.getKey();
            //获取验证码成功
            timer = new CountDownTimer(61 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //如果是Fragment 就判断getActivity() 是否为NULL
                    //如果是Activity 就判断!activity.isFinishing() 是否为NULL
                    if (balancePassWordActivity != null) {
                        int left = (int) ((millisUntilFinished - 1000) / 1000);
                        GALogger.d(TAG, "left       " + left);
                        if (left > 0) {
                            tvGetBalanceSmscode.setText(left + "秒");
                            tvGetBalanceSmscode.setClickable(false);
                            tvGetBalanceSmscode.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_737373));
                        } else {
                            tvGetBalanceSmscode.setClickable(true);
                            tvGetBalanceSmscode.setText("获取验证码");
                            tvGetBalanceSmscode.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_3e95e0));
                        }
                    }
                }

                @Override
                public void onFinish() {
                }
            };
            timer.start();
        }
    }

    @Override
    public void changeFinancePwdSuccess(BaseBean baseBean) {
        balancePassWordActivity.finish();
    }

    @Override
    public void setPresenter(BalancePassWordContract.Presenter presenter) {
        balancePassWordPresenter = (BalancePassWordPresenter) presenter;
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
    public void onDestroyView() {
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        super.onDestroyView();
    }
}
