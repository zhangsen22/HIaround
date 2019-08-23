package hiaround.android.com.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.qrcode.Constant;
import com.example.qrcode.ScannerActivity;
import com.example.qrcode.utils.QRCodeUtil;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.Md5Utils;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.YnShanFuEditModle;
import hiaround.android.com.modle.YunShanFuPayeeItemModelPayee;
import hiaround.android.com.presenter.YunShanFuEditPresenter;
import hiaround.android.com.presenter.contract.YunShanFuEditContract;
import hiaround.android.com.ui.activity.BalancePassWordActivity;
import hiaround.android.com.ui.activity.PaySettingActivity;
import hiaround.android.com.ui.activity.YunShanFuLoginActivity;
import hiaround.android.com.util.ToastUtil;

public class YunShanFunEditFragment extends BaseFragment implements YunShanFuEditContract.View {
    private static final String TAG = YunShanFunEditFragment.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.et_wenchat_name)
    EditText etWenchatName;
    @BindView(R.id.et_webchat_code)
    EditText etWebchatCode;
    @BindView(R.id.iv_webchat_image)
    ImageView ivWebchatImage;
    @BindView(R.id.et_forget_password)
    EditText etForgetPassword;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.ll_forget_password)
    LinearLayout llForgetPassword;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private PaySettingActivity paySettingActivity;
    private YunShanFuEditPresenter presenter;
    private YunShanFuPayeeItemModelPayee yunShanFuPayeeItemModelPayee = null;
    private String sIdcardFront;
    private Bitmap qrImage;
    private Bitmap bitmap;
    private long id = 0;
    private boolean isRefreshData = false;

    public static YunShanFunEditFragment newInstance(YunShanFuPayeeItemModelPayee yunShanFuPayeeItemModelPayee) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("yunShanFuPayeeItemModelPayee", yunShanFuPayeeItemModelPayee);
        YunShanFunEditFragment fragment = new YunShanFunEditFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paySettingActivity = (PaySettingActivity) getActivity();
        yunShanFuPayeeItemModelPayee = getArguments().getParcelable("yunShanFuPayeeItemModelPayee");
    }

    @Override
    protected int getRootView() {
        return R.layout.yunshanfu_edit_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("云闪付设置");
        bitmap = BitmapFactory.decodeResource(MyApplication.appContext.getResources(), R.drawable.ic_launcher_round);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
    }

    @OnClick({R.id.iv_back, R.id.iv_webchat_image, R.id.tv_forget_password, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if(isRefreshData){
                    paySettingActivity.setResult(Activity.RESULT_OK);
                }
                paySettingActivity.finish();
                break;
            case R.id.iv_webchat_image:
                if (ContextCompat.checkSelfPermission(paySettingActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    goScanner();
                } else {
                    ActivityCompat.requestPermissions(paySettingActivity, new String[]{Manifest.permission.CAMERA}, 120);
                }
                break;
            case R.id.tv_forget_password:
                BalancePassWordActivity.startThis(paySettingActivity);
                break;
            case R.id.tv_submit:
                String wenchatName = etWenchatName.getText().toString().trim();
                if (TextUtils.isEmpty(wenchatName)) {
                    ToastUtil.shortShow("请输入真实姓名");
                    return;
                }

                String webchatCode = etWebchatCode.getText().toString().trim();
                if (TextUtils.isEmpty(webchatCode)) {
                    ToastUtil.shortShow("请输入云闪付账号");
                    return;
                }

                if (TextUtils.isEmpty(sIdcardFront)) {
                    ToastUtil.shortShow("请上传云闪付商户固码");
                    return;
                }

                String forgetPassword = etForgetPassword.getText().toString().trim();
                if (TextUtils.isEmpty(forgetPassword)) {
                    ToastUtil.shortShow("请输入资金密码");
                    return;
                }
                long currentTime = System.currentTimeMillis();
                presenter.yunshanfu(id, wenchatName, webchatCode, sIdcardFront, Md5Utils.getMD5(forgetPassword + currentTime), currentTime);
                break;
        }
    }

    @Override
    public void yunShanFuSuccess(YnShanFuEditModle ynShanFuEditModle) {
        if(ynShanFuEditModle != null){
            long paymentId = ynShanFuEditModle.getPaymentId();
            if(paymentId > 0) {
                YunShanFuLoginActivity.launchVerifyCodeForResult(paySettingActivity, Constants.YUNSHANFUURL,paymentId,Constants.REQUESTCODE_20);
                isRefreshData = true;
            }
        }
    }

    @Override
    public void setPresenter(YunShanFuEditContract.Presenter presenter) {
        this.presenter = (YunShanFuEditPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    private void goScanner() {
        Intent intent = new Intent(paySettingActivity, ScannerActivity.class);
        //这里可以用intent传递一些参数，比如扫码聚焦框尺寸大小，支持的扫码类型。
//        //设置扫码框的宽
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, 400);
//        //设置扫码框的高
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, 400);
//        //设置扫码框距顶部的位置
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_TOP_PADDING, 400);
//        //设置是否启用从相册获取二维码。
        intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true);
//        Bundle bundle = new Bundle();
//        //设置支持的扫码类型
//        bundle.putSerializable(Constant.EXTRA_SCAN_CODE_TYPE, mHashMap);
//        intent.putExtras(bundle);
        paySettingActivity.startActivityForResult(intent, 102);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 120: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScanner();
                }
                return;
            }
        }
    }

    public void onActivityResultF(int requestCode, int resultCode, Intent data) {
        String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
        String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);
        GALogger.d(TAG, "codeType:" + type + "-----content:" + content);
        creatCode(content);
    }

    private void creatCode(String content) {
        if (!TextUtils.isEmpty(content)) {
            qrImage = QRCodeUtil.createQRCodeBitmap(content, 650, 650, "UTF-8",
                    "H", "1", Color.BLACK, Color.WHITE, bitmap, 0.2F, null);
            sIdcardFront = content;
            ivWebchatImage.setImageBitmap(qrImage);
            ivWebchatImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @Override
    public void onDestroyView() {
        if (qrImage != null && !qrImage.isRecycled()) {
            qrImage.recycle();
            qrImage = null;
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        super.onDestroyView();
    }

    public void onActivityBack(int requestCode, int resultCode, Intent data) {
        paySettingActivity.setResult(Activity.RESULT_OK);
        paySettingActivity.finish();
    }

    public void onkeyDown() {
        if(isRefreshData){
            paySettingActivity.setResult(Activity.RESULT_OK);
        }
        paySettingActivity.finish();
    }
}
