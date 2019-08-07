package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.R;
import hiaround.android.com.app.AccountInfo;
import hiaround.android.com.modle.DomainModel;
import hiaround.android.com.presenter.LoginPresenter;
import hiaround.android.com.presenter.contract.LoginContract;
import hiaround.android.com.ui.activity.ForgetPwdActivity;
import hiaround.android.com.ui.activity.LoginActivity;
import hiaround.android.com.ui.activity.MainActivity;
import hiaround.android.com.ui.activity.RegistActivity;
import hiaround.android.com.util.ToastUtil;

public class LoginFragment extends BaseFragment implements LoginContract.View {
    private static final String TAG = LoginFragment.class.getSimpleName();
    @BindView(R.id.go_regist)
    TextView goRegist;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_eye)
    ImageView ivEye;
    @BindView(R.id.go_login)
    TextView goLogin;
    @BindView(R.id.fl_login_head)
    FrameLayout flLoginHead;
    @BindView(R.id.ll_login_bottom)
    LinearLayout llLoginBottom;
    private LoginActivity loginActivity;
    private LoginPresenter loginPresenter;
    private boolean isShowPassward = false;

    public static LoginFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
//        arguments.putString(ARGUMENT_TASK_ID, taskId);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = (LoginActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.login_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flLoginHead);
        setRootViewPaddingTop(llLoginBottom);
    }

    @OnClick({R.id.iv_eye, R.id.go_login, R.id.go_regist, R.id.go_forget_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_eye:
                if (isShowPassward) {
                    ivEye.setImageResource(R.mipmap.yincang);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().length());
                    isShowPassward = false;
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPassward = true;
                    etPassword.setSelection(etPassword.getText().length());
                    ivEye.setImageResource(R.mipmap.br);
                }
                break;
            case R.id.go_login:
                String phone = etPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.shortShow("请输入手机号");
                    return;
                }
                String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.shortShow("请输入密码");
                    return;
                }
                long currentTime = System.currentTimeMillis();
                loginPresenter.login(phone, password, currentTime, true);
                break;
            case R.id.go_regist:
                RegistActivity.startThis(loginActivity);
                break;
            case R.id.go_forget_pass:
                ForgetPwdActivity.startThis(loginActivity);
                break;
        }
    }

    @Override
    public void loginSuccess(AccountInfo accountInfo) {
        MainActivity.startThis(loginActivity);
        loginActivity.finish();
    }

    @Override
    public void loginError() {

    }

    @Override
    public void getDomainNameSuccess(DomainModel domainModel) {

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        loginPresenter = (LoginPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }
}
