package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.commons.codec.binary.Base64;
import com.growalong.util.util.BitmapUtils;
import com.growalong.util.util.GALogger;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.modle.ImageCodeResponse;
import hiaround.android.com.modle.SmsCodeResponse;
import hiaround.android.com.presenter.ForgetPwdPresenter;
import hiaround.android.com.presenter.contract.ForgetPwdContract;
import hiaround.android.com.ui.activity.ForgetPwdActivity;
import hiaround.android.com.util.RSAUtil;
import hiaround.android.com.util.ToastUtil;

public class ForgetPwdFragment extends BaseFragment implements ForgetPwdContract.View {
    private static final String TAG = ForgetPwdFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_forget_login_password)
    EditText etForgetLoginPassword;
    @BindView(R.id.etre_forget_login_password)
    EditText etreForgetLoginPassword;
    @BindView(R.id.et_input_phonenumber)
    EditText etInputPhonenumber;
    @BindView(R.id.et_image_number)
    EditText etImageNumber;
    @BindView(R.id.iv_image_code)
    ImageView ivImageCode;
    @BindView(R.id.ll_image_code)
    LinearLayout llImageCode;
    @BindView(R.id.et_forget_login_smscode)
    EditText etForgetLoginSmscode;
    @BindView(R.id.tv_get_forget_login_smscode)
    TextView tvGetForgetLoginSmscode;
    @BindView(R.id.tv_submit_forget_login)
    TextView tvSubmitForgetLogin;

    private ForgetPwdActivity forgetPwdActivity;
    private ForgetPwdPresenter presenter;
    private String publicKey = "";

    /**
     * 倒计时60秒，一次1秒
     */
    private CountDownTimer timer;

    public static ForgetPwdFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        ForgetPwdFragment fragment = new ForgetPwdFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgetPwdActivity = (ForgetPwdActivity) getActivity();
    }


    @Override
    protected int getRootView() {
        return R.layout.forget_pwd_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("忘记密码");
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
    }


    @Override
    public void getImageCodeSuccess(ImageCodeResponse imageCodeResponse) {
        llImageCode.setVisibility(View.VISIBLE);
        ivImageCode.setImageBitmap(BitmapUtils.base64ToBitmap(imageCodeResponse.getImage()));
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
                    if (forgetPwdActivity != null) {
                        int left = (int) ((millisUntilFinished - 1000) / 1000);
                        GALogger.d(TAG, "left       " + left);
                        if (left > 0) {
                            tvGetForgetLoginSmscode.setText(left + "秒");
                            tvGetForgetLoginSmscode.setClickable(false);
                            tvGetForgetLoginSmscode.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_737373));
                        } else {
                            tvGetForgetLoginSmscode.setClickable(true);
                            tvGetForgetLoginSmscode.setText("获取验证码");
                            tvGetForgetLoginSmscode.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_3e95e0));
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
    public void forgetPwdSuccess(BaseBean baseBean) {
        forgetPwdActivity.finish();
    }

    @Override
    public void setPresenter(ForgetPwdContract.Presenter presenter) {
        this.presenter = (ForgetPwdPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @OnClick({R.id.iv_back, R.id.iv_image_code, R.id.tv_get_forget_login_smscode, R.id.tv_submit_forget_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                forgetPwdActivity.finish();
                break;
            case R.id.iv_image_code:
                String phone0 = etForgetLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phone0)) {
                    ToastUtil.shortShow("请输入密码");
                    return;
                }
                String phone1 = etreForgetLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phone1)) {
                    ToastUtil.shortShow("请输入确认密码");
                    return;
                }
                if(!phone0.equals(phone1)){
                    ToastUtil.shortShow("两次密码输入不一致");
                    return;
                }
                String phone2 = etInputPhonenumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone2)) {
                    ToastUtil.shortShow("请输入手机号");
                    return;
                }
                presenter.getImageCode(phone2);
                break;
            case R.id.tv_get_forget_login_smscode:
                String phone3 = etForgetLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phone3)) {
                    ToastUtil.shortShow("请输入密码");
                    return;
                }
                String phone4 = etreForgetLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phone4)) {
                    ToastUtil.shortShow("请输入确认密码");
                    return;
                }
                if(!phone4.equals(phone3)){
                    ToastUtil.shortShow("两次密码输入不一致");
                    return;
                }
                String phone5 = etInputPhonenumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone5)) {
                    ToastUtil.shortShow("请输入手机号");
                    return;
                }
                if(llImageCode.getVisibility() == View.GONE){
                    presenter.getImageCode(phone5);
                }else {
                    String imageNumber = etImageNumber.getText().toString().trim();
                    if (TextUtils.isEmpty(imageNumber)) {
                        ToastUtil.shortShow("请输入图片验证码");
                        return;
                    }
                    presenter.senSenSmsCode(phone5);
                }
                break;
            case R.id.tv_submit_forget_login:
                String phone6 = etForgetLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phone6)) {
                    ToastUtil.shortShow("请输入密码");
                    return;
                }
                String phone7 = etreForgetLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phone7)) {
                    ToastUtil.shortShow("请输入确认密码");
                    return;
                }
                if(!phone7.equals(phone6)){
                    ToastUtil.shortShow("两次密码输入不一致");
                    return;
                }
                String phone8 = etInputPhonenumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone8)) {
                    ToastUtil.shortShow("请输入手机号");
                    return;
                }

                String phone9 = etForgetLoginSmscode.getText().toString().trim();
                if (TextUtils.isEmpty(phone9)) {
                    ToastUtil.shortShow("请输入短信验证码");
                    return;
                }

                if (TextUtils.isEmpty(publicKey)) {
                    ToastUtil.shortShow("请获取验证码");
                    return;
                }

                    String imageNumber1 = etImageNumber.getText().toString().trim();
                    if (TextUtils.isEmpty(imageNumber1)) {
                        ToastUtil.shortShow("请输入图片验证码");
                        return;
                    }
                    try {
                        presenter.forgetPwd(Base64.encodeBase64String(RSAUtil.encryptByPublicKey(phone7,publicKey)),phone8,imageNumber1,phone9);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                break;
        }
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
