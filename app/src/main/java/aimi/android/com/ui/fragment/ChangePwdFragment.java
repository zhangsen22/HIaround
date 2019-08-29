package aimi.android.com.ui.fragment;

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
import aimi.android.com.BaseFragment;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.app.AccountManager;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.SmsCodeResponse;
import aimi.android.com.presenter.ChangePwdPresenter;
import aimi.android.com.presenter.contract.ChangePwdContract;
import aimi.android.com.presenter.modle.ChangePwdModle;
import aimi.android.com.ui.activity.ChangePwdActivity;
import aimi.android.com.util.RSAUtil;
import aimi.android.com.util.ToastUtil;

public class ChangePwdFragment extends BaseFragment implements ChangePwdContract.View {
    private static final String TAG = ChangePwdFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_change_login_password)
    EditText etChangeLoginPassword;
    @BindView(R.id.etre_change_login_password)
    EditText etreChangeLoginPassword;
    @BindView(R.id.tv_change_login_phonenumber)
    TextView tvChangeLoginPhonenumber;
    @BindView(R.id.et_change_login_smscode)
    EditText etChangeLoginSmscode;
    @BindView(R.id.tv_get_change_login_smscode)
    TextView tvGetChangeLoginSmscode;
    @BindView(R.id.tv_submit_change_login)
    TextView tvSubmitChangeLogin;
    private String phoneNumber;
    private ChangePwdActivity changePwdActivity;
    private ChangePwdPresenter presenter;
    private String publicKey = "";

    /**
     * 倒计时60秒，一次1秒
     */
    private CountDownTimer timer;

    public static ChangePwdFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        ChangePwdFragment fragment = new ChangePwdFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changePwdActivity = (ChangePwdActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.change_pwd_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("设置登录密码");
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        phoneNumber = AccountManager.getInstance().getPhoneNumber();
        if(!TextUtils.isEmpty(phoneNumber)){
            tvChangeLoginPhonenumber.setText(phoneNumber);
        }
        //初始化presenter
        new ChangePwdPresenter(this, new ChangePwdModle());
    }

    @OnClick({R.id.iv_back, R.id.tv_get_change_login_smscode, R.id.tv_submit_change_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                changePwdActivity.finish();
                break;
            case R.id.tv_get_change_login_smscode:
                String balancePassword1 = etChangeLoginPassword.getText().toString().trim();
                if(TextUtils.isEmpty(balancePassword1)){
                    ToastUtil.shortShow("请输入登录密码");
                    return;
                }

                String rebalancePassword1 = etreChangeLoginPassword.getText().toString().trim();
                if(TextUtils.isEmpty(rebalancePassword1)){
                    ToastUtil.shortShow("请输入确认登录密码");
                    return;
                }
                if(!rebalancePassword1.equals(balancePassword1)){
                    ToastUtil.shortShow("两次密码输入不一致");
                    return;
                }
                presenter.senSenSmsCode(phoneNumber);
                break;
            case R.id.tv_submit_change_login:
                String balancePassword = etChangeLoginPassword.getText().toString().trim();
                if(TextUtils.isEmpty(balancePassword)){
                    ToastUtil.shortShow("请输入登录密码");
                    return;
                }

                String rebalancePassword = etreChangeLoginPassword.getText().toString().trim();
                if(TextUtils.isEmpty(rebalancePassword)){
                    ToastUtil.shortShow("请输入确认登录密码");
                    return;
                }

                if(!rebalancePassword.equals(balancePassword)){
                    ToastUtil.shortShow("两次密码不一致");
                    return;
                }

                String balanceSmscode = etChangeLoginSmscode.getText().toString().trim();
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
                    presenter.changePwd(s,balanceSmscode);
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
                    if (changePwdActivity != null) {
                        int left = (int) ((millisUntilFinished - 1000) / 1000);
                        GALogger.d(TAG, "left       " + left);
                        if (left > 0) {
                            tvGetChangeLoginSmscode.setText(left + "秒");
                            tvGetChangeLoginSmscode.setClickable(false);
                            tvGetChangeLoginSmscode.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_737373));
                        } else {
                            tvGetChangeLoginSmscode.setClickable(true);
                            tvGetChangeLoginSmscode.setText("获取验证码");
                            tvGetChangeLoginSmscode.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_3e95e0));
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
    public void changePwdSuccess(BaseBean baseBean) {
        changePwdActivity.finish();
    }

    @Override
    public void setPresenter(ChangePwdContract.Presenter presenter) {
        this.presenter = (ChangePwdPresenter) presenter;
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
