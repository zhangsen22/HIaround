package hiaround.android.com.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
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
import hiaround.android.com.app.AccountInfo;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.ImageCodeResponse;
import hiaround.android.com.modle.SmsCodeResponse;
import hiaround.android.com.presenter.RegistPresenter;
import hiaround.android.com.presenter.contract.RegistContract;
import hiaround.android.com.ui.activity.MainActivity;
import hiaround.android.com.ui.activity.RegistActivity;
import hiaround.android.com.ui.activity.WebViewActivity;
import hiaround.android.com.util.RSAUtil;
import hiaround.android.com.util.ToastUtil;

public class RegistFragment extends BaseFragment implements RegistContract.View {
    private static final String TAG = RegistFragment.class.getSimpleName();
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_yaoqing_number)
    EditText etYaoqingNumber;
    @BindView(R.id.et_image_number)
    EditText etImageNumber;
    @BindView(R.id.iv_image_code)
    ImageView ivImageCode;
    @BindView(R.id.et_sms_number)
    EditText etSmsNumber;
    @BindView(R.id.tv_getsms_code)
    TextView tvGetsmsCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_regist)
    TextView tvRegist;
    @BindView(R.id.ll_image_code)
    LinearLayout llImageCode;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.tv_yonghuxieyi)
    TextView tvYonghuxieyi;
    @BindView(R.id.fl_regist_head)
    FrameLayout flRegistHead;
    @BindView(R.id.ll_regist_bottom)
    LinearLayout llRegistBottom;

    private RegistActivity registActivity;
    private RegistPresenter registPresenter;
    private String publicKey = "";
    /**
     * 倒计时60秒，一次1秒
     */
    private CountDownTimer timer;

    public static RegistFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        RegistFragment fragment = new RegistFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registActivity = (RegistActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.regist_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flRegistHead);
        setRootViewPaddingTop(llRegistBottom);
    }

    @Override
    public void getImageCodeSuccess(ImageCodeResponse imageCodeResponse) {
        llImageCode.setVisibility(View.VISIBLE);
        ivImageCode.setImageBitmap(BitmapUtils.base64ToBitmap(imageCodeResponse.getImage()));
    }

    @Override
    public void senSenSmsCodeSuccess(SmsCodeResponse smsCodeResponse) {
        if (smsCodeResponse != null) {
            publicKey = smsCodeResponse.getKey();
            //获取验证码成功
            timer = new CountDownTimer(60 * 1000 + 500, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //如果是Fragment 就判断getActivity() 是否为NULL
                    //如果是Activity 就判断!activity.isFinishing() 是否为NULL
                    if (registActivity != null) {
                        int left = (int) ((millisUntilFinished - 1000) / 1000);
                        GALogger.d(TAG, "left       " + left);
                        if (left > 0) {
                            tvGetsmsCode.setText(left + "秒");
                            tvGetsmsCode.setClickable(false);
                            tvGetsmsCode.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_737373));
                        } else {
                            tvGetsmsCode.setClickable(true);
                            tvGetsmsCode.setText("获取验证码");
                            tvGetsmsCode.setTextColor(MyApplication.appContext.getResources().getColor(R.color.color_3e95e0));
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
    public void registerAndLoginSuccess(AccountInfo accountInfo) {
        MainActivity.startThis(registActivity);
        registActivity.finish();
    }

    @Override
    public void setPresenter(RegistContract.Presenter presenter) {
        registPresenter = (RegistPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @OnClick({R.id.tv_getsms_code, R.id.tv_regist, R.id.iv_image_code, R.id.tv_yonghuxieyi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_image_code:
                String phone0 = etPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone0)) {
                    ToastUtil.shortShow("请输入手机号");
                    return;
                }
                registPresenter.getImageCode(phone0);
                break;
            case R.id.tv_getsms_code:
                String phone = etPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.shortShow("请输入手机号");
                    return;
                }
                String yaoqingNumber = etYaoqingNumber.getText().toString().trim();
                if (TextUtils.isEmpty(yaoqingNumber)) {
                    ToastUtil.shortShow("请输入邀请码");
                    return;
                }
                if (llImageCode.getVisibility() == View.GONE) {
                    registPresenter.getImageCode(phone);
                } else {
                    String imageNumber = etImageNumber.getText().toString().trim();
                    if (TextUtils.isEmpty(imageNumber)) {
                        ToastUtil.shortShow("请输入图片验证码");
                        return;
                    }
                    registPresenter.senSenSmsCode(phone);
                }
                break;
            case R.id.tv_regist:
                String phone1 = etPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone1)) {
                    ToastUtil.shortShow("请输入手机号");
                    return;
                }
                String yaoqingNumber1 = etYaoqingNumber.getText().toString().trim();
                if (TextUtils.isEmpty(yaoqingNumber1)) {
                    ToastUtil.shortShow("请输入邀请码");
                    return;
                }
                String smsNumber = etSmsNumber.getText().toString().trim();
                if (TextUtils.isEmpty(smsNumber)) {
                    ToastUtil.shortShow("请输入短信验证码");
                    return;
                }
                String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.shortShow("请输入密码");
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

                if (!cbAgree.isChecked()) {
                    ToastUtil.shortShow("是否同意用户协议");
                    return;
                }

                try {
                    GALogger.d(TAG, " 参数   " + phone1 + "     " + yaoqingNumber1 + "         " + imageNumber1 + "         " + smsNumber + "         " + Base64.encodeBase64String(RSAUtil.encryptByPublicKey(password, publicKey)) + "         " + System.currentTimeMillis() + "    " + publicKey);
                    registPresenter.registerAndLogin(phone1, yaoqingNumber1, imageNumber1, smsNumber, Base64.encodeBase64String(RSAUtil.encryptByPublicKey(password, publicKey)), System.currentTimeMillis(), password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_yonghuxieyi:
                WebViewActivity.launchVerifyCode(MyApplication.appContext, Constants.USERXIEYI, true);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
