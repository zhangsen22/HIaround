package aimi.android.com.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.growalong.util.util.Md5Utils;
import butterknife.BindView;
import butterknife.OnClick;
import aimi.android.com.BaseFragment;
import aimi.android.com.R;
import aimi.android.com.presenter.IdCastPresenter;
import aimi.android.com.presenter.contract.IdCastContract;
import aimi.android.com.ui.activity.BalancePassWordActivity;
import aimi.android.com.ui.activity.PaySettingActivity;
import aimi.android.com.util.ToastUtil;

public class IdCastPayEditFragment extends BaseFragment implements IdCastContract.View {
    private static final String TAG = IdCastPayEditFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_yinhang_name)
    EditText etYinhangName;
    @BindView(R.id.et_yinhang_zhiname)
    EditText etYinhangZhiname;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_cast_code)
    EditText etCastCode;
    @BindView(R.id.et_everyday_jine)
    EditText etEverydayJine;
    @BindView(R.id.et_forget_password)
    EditText etForgetPassword;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private PaySettingActivity paySettingActivity;
    private IdCastPresenter presenter;

    public static IdCastPayEditFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        IdCastPayEditFragment fragment = new IdCastPayEditFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paySettingActivity = (PaySettingActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.id_cast_pay_edit_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("银行卡设置");
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
    }

    @OnClick({R.id.iv_back, R.id.tv_forget_password, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                paySettingActivity.finish();
                break;
            case R.id.tv_forget_password:
                BalancePassWordActivity.startThis(paySettingActivity);
                break;
            case R.id.tv_submit:
                String yinhangName = etYinhangName.getText().toString().trim();
                if(TextUtils.isEmpty(yinhangName)){
                    ToastUtil.shortShow("请输入银行名称");
                    return;
                }

                String yinhangZhiname = etYinhangZhiname.getText().toString().trim();
                if(TextUtils.isEmpty(yinhangZhiname)){
                    ToastUtil.shortShow("请输入所在支行");
                    return;
                }

                String name = etName.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    ToastUtil.shortShow("请输入真实姓名");
                    return;
                }

                String castCode = etCastCode.getText().toString().trim();
                if(TextUtils.isEmpty(castCode)){
                    ToastUtil.shortShow("请输入银行卡号");
                    return;
                }

                String everydayJine = etEverydayJine.getText().toString().trim();
                if(TextUtils.isEmpty(everydayJine)){
                    ToastUtil.shortShow("请输入银行卡每日收款限额");
                    return;
                }

                String forgetPassword = etForgetPassword.getText().toString().trim();
                if(TextUtils.isEmpty(forgetPassword)){
                    ToastUtil.shortShow("请输入平台资金密码");
                    return;
                }

                double dailyLimit = Double.parseDouble(everydayJine);
                if(dailyLimit <= 0){
                    ToastUtil.shortShow("限额不能小于零");
                    return;
                }
                long currentTime = System.currentTimeMillis();
                presenter.bank(0,yinhangName, yinhangZhiname, name, castCode, dailyLimit, Md5Utils.getMD5(forgetPassword+currentTime),currentTime);
                break;
        }
    }

    @Override
    public void bankSuccess(String name, String account) {
        paySettingActivity.setResult(Activity.RESULT_OK);
        paySettingActivity.finish();
    }

    @Override
    public void setPresenter(IdCastContract.Presenter presenter) {
        this.presenter = (IdCastPresenter) presenter;
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
