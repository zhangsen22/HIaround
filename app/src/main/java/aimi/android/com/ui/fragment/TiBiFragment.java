package aimi.android.com.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.qrcode.Constant;
import com.example.qrcode.ScannerActivity;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.Md5Utils;
import com.growalong.util.util.TextWatcherUtils;
import java.text.DecimalFormat;
import butterknife.BindView;
import butterknife.OnClick;
import aimi.android.com.BaseFragment;
import aimi.android.com.R;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.WalletResponse;
import aimi.android.com.presenter.TiBiPresenter;
import aimi.android.com.presenter.contract.TiBiContract;
import aimi.android.com.ui.activity.BalancePassWordActivity;
import aimi.android.com.ui.activity.TiBiActivity;
import aimi.android.com.util.ToastUtil;

public class TiBiFragment extends BaseFragment implements TiBiContract.View {
    private static final String TAG = TiBiFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_bi_type)
    TextView tvBiType;
    @BindView(R.id.et_bi_address)
    EditText etBiAddress;
    @BindView(R.id.iv_saoyisao)
    ImageView ivSaoyisao;
    @BindView(R.id.et_bi_num)
    EditText etBiNum;
    @BindView(R.id.tv_bi_all)
    TextView tvBiAll;
    @BindView(R.id.et_bi_keyongum)
    TextView etBiKeyongum;
    @BindView(R.id.et_bi_shouxufei)
    TextView etBiShouxufei;
    @BindView(R.id.et_bineass_password)
    EditText etBineassPassword;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.et_bi_daozhangnum)
    TextView etBiDaozhangnum;
    @BindView(R.id.tv_queding)
    TextView tvQueding;
    private TiBiActivity tiBiActivity;
    private TiBiPresenter presenter;
    private double walletNum;

    public static TiBiFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        TiBiFragment fragment = new TiBiFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tiBiActivity = (TiBiActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.ti_bi_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("提币");
        tvBiType.setText("USDT");
        etBiNum.addTextChangedListener(new TextWatcherUtils(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String sNum = s.toString();
                if (!TextUtils.isEmpty(sNum)) {
                    double d_num = Double.parseDouble(sNum);
                    if (d_num <= 2) {
                        etBiDaozhangnum.setText("");
                        ToastUtil.shortShow("提币数量不能小于2");
                        return;
                    }

                    if (d_num > walletNum) {
                        etBiDaozhangnum.setText("");
                        ToastUtil.shortShow("提币数量不足");
                        return;
                    }

                    etBiDaozhangnum.setText(new DecimalFormat("0.00").format(d_num - 2));
                }
            }
        });
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        presenter.getInfo();
    }

    @OnClick({R.id.iv_back, R.id.iv_saoyisao, R.id.tv_forget_password, R.id.tv_queding,R.id.tv_bi_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                tiBiActivity.finish();
                break;
            case R.id.iv_saoyisao:
                if (ContextCompat.checkSelfPermission(tiBiActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    goScanner();
                } else {
                    ActivityCompat.requestPermissions(tiBiActivity, new String[]{Manifest.permission.CAMERA},TiBiActivity.REQUEST_PERMISION_CODE_CAMARE);
                }
                break;
            case R.id.tv_forget_password:
                BalancePassWordActivity.startThis(tiBiActivity);
                break;
            case R.id.tv_queding:
                String address = etBiAddress.getText().toString().trim();
                if(TextUtils.isEmpty(address)){
                    ToastUtil.shortShow("请输入提币地址");
                    return;
                }
                String num = etBiNum.getText().toString().trim();
                if(TextUtils.isEmpty(num)){
                    ToastUtil.shortShow("请输入提币数量");
                    return;
                }

                double d_num = Double.parseDouble(num);
                if(d_num <= 2){
                    ToastUtil.shortShow("提币数量不能小于2");
                    return;
                }

                if(d_num > walletNum){
                    ToastUtil.shortShow("提币数量不足");
                    return;
                }

                String password = etBineassPassword.getText().toString().trim();
                if(TextUtils.isEmpty(password)){
                    ToastUtil.shortShow("请输入资金密码");
                    return;
                }

                long currentTime = System.currentTimeMillis();
                presenter.withdraw(address,d_num,Md5Utils.getMD5(password+currentTime),currentTime);
                break;
            case R.id.tv_bi_all:
                etBiNum.setText(walletNum+"");
                break;
        }
    }

    private void goScanner() {
        Intent intent = new Intent(tiBiActivity, ScannerActivity.class);
        //这里可以用intent传递一些参数，比如扫码聚焦框尺寸大小，支持的扫码类型。
//        //设置扫码框的宽
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, 400);
//        //设置扫码框的高
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, 400);
//        //设置扫码框距顶部的位置
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_TOP_PADDING, 400);
//        //设置是否启用从相册获取二维码。
        intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC,true);
//        Bundle bundle = new Bundle();
//        //设置支持的扫码类型
//        bundle.putSerializable(Constant.EXTRA_SCAN_CODE_TYPE, mHashMap);
//        intent.putExtras(bundle);
        tiBiActivity.startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case TiBiActivity.REQUEST_PERMISION_CODE_CAMARE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScanner();
                }
                return;
            }
        }
    }

    @Override
    public void withdrawSuccess(BaseBean baseBean) {
        tiBiActivity.finish();
    }

    @Override
    public void getInfoSuccess(WalletResponse walletResponse) {
        if(walletResponse != null){
            walletNum = walletResponse.getWalletNum();
            etBiKeyongum.setText(new DecimalFormat("0.00").format(walletNum));
            etBiShouxufei.setText("2");
        }
    }

    @Override
    public void setPresenter(TiBiContract.Presenter presenter) {
        this.presenter = (TiBiPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    public void onActivityResultF(int requestCode, int resultCode, Intent data) {
        String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
        String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);
        GALogger.d(TAG,"codeType:" + type + "-----content:" + content);
        etBiAddress.setText(content);
    }
}
