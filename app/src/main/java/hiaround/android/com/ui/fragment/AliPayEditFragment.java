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
import android.widget.TextView;
import com.example.qrcode.Constant;
import com.example.qrcode.ScannerActivity;
import com.example.qrcode.utils.QRCodeUtil;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.GsonUtil;
import com.growalong.util.util.Md5Utils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.Constants;
import hiaround.android.com.modle.AliPayee;
import hiaround.android.com.presenter.AliPayEditPresenter;
import hiaround.android.com.presenter.contract.AliPayEditContract;
import hiaround.android.com.ui.activity.BalancePassWordActivity;
import hiaround.android.com.ui.activity.PaySettingActivity;
import hiaround.android.com.ui.activity.WebViewActivity;
import hiaround.android.com.ui.widget.CenterErWeiMaPopupView;
import hiaround.android.com.util.ToastUtil;

public class AliPayEditFragment extends BaseFragment implements AliPayEditContract.View {
    private static final String TAG = AliPayEditFragment.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_alipay_name)
    EditText etAlipayName;
    @BindView(R.id.et_alipay_account)
    EditText etAlipayAccount;
    @BindView(R.id.et_alipay_id)
    EditText etAlipayId;
    @BindView(R.id.tv_alipay_getid)
    TextView tvAlipayGetid;
    @BindView(R.id.tv_alipay_scewm)
    TextView tvAlipayScewm;
    @BindView(R.id.iv_alipay_image)
    ImageView ivAlipayImage;
    @BindView(R.id.et_alipay_password)
    EditText etAlipayPassword;
    @BindView(R.id.tv_forget_alipay_password)
    TextView tvForgetAlipayPassword;
    @BindView(R.id.tv_alipay_submit)
    TextView tvAlipaySubmit;
    private PaySettingActivity paySettingActivity;
    private AliPayEditPresenter presenter;
    private Bitmap bitmapLog;
    private Bitmap qrImage1;
    private String sIdcardFront;
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;

    public static AliPayEditFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        AliPayEditFragment fragment = new AliPayEditFragment();
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
        return R.layout.ali_pay_edit_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("支付宝设置");
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        bitmapLog = BitmapFactory.decodeResource(MyApplication.appContext.getResources(), R.drawable.ic_launcher_round);
    }

    @Override
    public void aliSuccess(String name, String account, String base64Img) {
        paySettingActivity.setResult(Activity.RESULT_OK);
        paySettingActivity.finish();
    }

    @Override
    public void setPresenter(AliPayEditContract.Presenter presenter) {
        this.presenter = (AliPayEditPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @OnClick({R.id.iv_back, R.id.tv_alipay_getid, R.id.tv_alipay_scewm, R.id.tv_forget_alipay_password, R.id.tv_alipay_submit,R.id.iv_alipay_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                paySettingActivity.finish();
                break;
            case R.id.tv_alipay_getid:
                WebViewActivity.launchVerifyCode(MyApplication.appContext, Constants.HOWGETALIPAYID,true);
                break;
            case R.id.tv_alipay_scewm:
                String alipayName = etAlipayName.getText().toString().trim();
                if(TextUtils.isEmpty(alipayName)){
                    ToastUtil.shortShow("请输入您的真实姓名");
                    return;
                }
                String alipayAccount = etAlipayAccount.getText().toString().trim();
                if(TextUtils.isEmpty(alipayAccount)){
                    ToastUtil.shortShow("请输入支付宝账号");
                    return;
                }
                String alipayId = etAlipayId.getText().toString().trim();
                if(TextUtils.isEmpty(alipayId)){
                    ToastUtil.shortShow("请输入支付宝用户ID");
                    return;
                }
                String alipayImage = "alipays://platformapi/startapp?appId=09999988&actionType=toAccount&goBack=YES&amount=1.00&userId="+alipayId+"&memo=3990";
                new XPopup.Builder(getContext())
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .setPopupCallback(new XPopupCallback() {
                            @Override
                            public void onShow() {

                            }

                            @Override
                            public void onDismiss() {
                            }
                        })
                        .asCustom(new CenterErWeiMaPopupView(getContext(),1,GsonUtil.getInstance().objTojson(new AliPayee(alipayAccount, alipayImage))))
                        .show();
                break;
            case R.id.tv_forget_alipay_password:
                BalancePassWordActivity.startThis(paySettingActivity);
                break;
            case R.id.tv_alipay_submit:
                String alipayName1 = etAlipayName.getText().toString().trim();
                if(TextUtils.isEmpty(alipayName1)){
                    ToastUtil.shortShow("请输入您的真实姓名");
                    return;
                }
                String alipayAccount1 = etAlipayAccount.getText().toString().trim();
                if(TextUtils.isEmpty(alipayAccount1)){
                    ToastUtil.shortShow("请输入支付宝账号");
                    return;
                }
                String alipayId1 = etAlipayId.getText().toString().trim();
                if(TextUtils.isEmpty(alipayId1)){
                    ToastUtil.shortShow("请输入支付宝用户ID");
                    return;
                }
                if(TextUtils.isEmpty(sIdcardFront)){
                    ToastUtil.shortShow("请上传支付宝收款码");
                    return;
                }
                String forgetPassword = etAlipayPassword.getText().toString().trim();
                if(TextUtils.isEmpty(forgetPassword)){
                    ToastUtil.shortShow("请输入资金密码");
                    return;
                }

                long currentTime = System.currentTimeMillis();
                presenter.ali(0,alipayName1,alipayAccount1,sIdcardFront,Md5Utils.getMD5(forgetPassword+currentTime),currentTime);
                break;
            case R.id.iv_alipay_image:
                if (ContextCompat.checkSelfPermission(paySettingActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    goScanner();
                } else {
                    ActivityCompat.requestPermissions(paySettingActivity, new String[]{Manifest.permission.CAMERA},98);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        if(bitmapLog != null && !bitmapLog.isRecycled()){
            bitmapLog.recycle();
            bitmapLog = null;
        }

        if(qrImage1 != null && !qrImage1.isRecycled()){
            qrImage1.recycle();
            qrImage1 = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 98: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScanner();
                }
                return;
            }
        }
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
        intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC,true);
//        Bundle bundle = new Bundle();
//        //设置支持的扫码类型
//        bundle.putSerializable(Constant.EXTRA_SCAN_CODE_TYPE, mHashMap);
//        intent.putExtras(bundle);
        paySettingActivity.startActivityForResult(intent, 101);
    }

    public void onActivityResultF(int requestCode, int resultCode, Intent data) {
        String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
        String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);
        GALogger.d(TAG,"codeType:" + type + "-----content:" + content);
        if(!TextUtils.isEmpty(content)) {
            qrImage1 = QRCodeUtil.createQRCodeBitmap(content, 650, 650, "UTF-8",
                    "H", "1", Color.BLACK, Color.WHITE, bitmapLog, 0.2F, null);
            sIdcardFront = content;
            ivAlipayImage.setImageBitmap(qrImage1);
            ivAlipayImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

}
