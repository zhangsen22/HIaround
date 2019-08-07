package hiaround.android.com.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.growalong.util.util.AppPublicUtils;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.Md5Utils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.modle.WeChatPayeeItemModelPayee;
import hiaround.android.com.modle.WebChatEditModle;
import hiaround.android.com.modle.WechatLoginModle;
import hiaround.android.com.presenter.WebChatEditPresenter;
import hiaround.android.com.presenter.contract.WebChatEditContract;
import hiaround.android.com.ui.activity.BalancePassWordActivity;
import hiaround.android.com.ui.activity.PaySettingActivity;
import hiaround.android.com.ui.widget.WenChatBindingPopupView;
import hiaround.android.com.ui.widget.WenChatSaoPopupView;
import hiaround.android.com.util.ToastUtil;

public class WebChatEditFragment extends BaseFragment implements WebChatEditContract.View {
    private static final String TAG = WebChatEditFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
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
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.ll_forget_password)
    LinearLayout llForgetPassword;
    private PaySettingActivity paySettingActivity;
    private WebChatEditPresenter presenter;
    private String sIdcardFront;
    private Bitmap qrImage;
    private Bitmap bitmap;
    private WeChatPayeeItemModelPayee weChatPayeeItemModelPayee = null;
    private long id = 0;
    private Handler mHandler;
    private BasePopupView show = null;
    private BasePopupView showSaoYiSao = null;
    private int reconnectionCount = 0;

    public static WebChatEditFragment newInstance(@Nullable WeChatPayeeItemModelPayee weChatPayeeItemModelPayee) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("weChatPayeeItemModelPayee", weChatPayeeItemModelPayee);
        WebChatEditFragment fragment = new WebChatEditFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paySettingActivity = (PaySettingActivity) getActivity();
        weChatPayeeItemModelPayee = getArguments().getParcelable("weChatPayeeItemModelPayee");
    }

    @Override
    protected int getRootView() {
        return R.layout.web_chat_edit_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("微信设置");
        mHandler = new Handler(Looper.getMainLooper());
        bitmap = BitmapFactory.decodeResource(MyApplication.appContext.getResources(), R.drawable.ic_launcher_round);
        if (weChatPayeeItemModelPayee == null) {
            id = 0;
        } else {
            llForgetPassword.setVisibility(View.GONE);
            tvSubmit.setVisibility(View.GONE);
            id = weChatPayeeItemModelPayee.getId();
            String name = weChatPayeeItemModelPayee.getName();
            if (!TextUtils.isEmpty(name)) {
                etWenchatName.setText(name);
                AppPublicUtils.setEditTextEnable(etWenchatName, false);
            }
            String account = weChatPayeeItemModelPayee.getAccount();
            if (!TextUtils.isEmpty(account)) {
                etWebchatCode.setText(account);
                AppPublicUtils.setEditTextEnable(etWebchatCode, false);
            }
            String base64Img = weChatPayeeItemModelPayee.getBase64Img();
            creatCode(base64Img);
            webChatGoLogin();
        }
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
    }

    @Override
    public void wechatSuccess(WebChatEditModle webChatEditModle) {
        if (webChatEditModle != null) {
            id = webChatEditModle.getPaymentId();
            webChatGoLogin();
        }
    }

    @Override
    public void wechatLoginSuccess(WechatLoginModle wechatLoginModle) {
        mHandler.removeCallbacksAndMessages(null);
        if (show != null) {
            if (show.isShow()) {
                show.dismiss();
            }
            show = null;
        }
        showSaoYiSao = new XPopup.Builder(getContext())
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .hasStatusBarShadow(true) //启用状态栏阴影
                .asCustom(new WenChatSaoPopupView(getContext(), wechatLoginModle.getLoginCode(), new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        paySettingActivity.setResult(Activity.RESULT_OK);
                        paySettingActivity.finish();
                    }
                })).show();
    }

    @Override
    public void wechatLoginError() {
        if (reconnectionCount >= 5) {
            paySettingActivity.setResult(Activity.RESULT_OK);
            paySettingActivity.finish();
        } else {
            webChatGoLogin();
        }
    }

    @Override
    public void setPresenter(WebChatEditContract.Presenter presenter) {
        this.presenter = (WebChatEditPresenter) presenter;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @OnClick({R.id.iv_back, R.id.iv_webchat_image, R.id.tv_forget_password, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                paySettingActivity.finish();
                break;
            case R.id.iv_webchat_image:
                if (ContextCompat.checkSelfPermission(paySettingActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    goScanner();
                } else {
                    ActivityCompat.requestPermissions(paySettingActivity, new String[]{Manifest.permission.CAMERA}, 99);
                }
                break;
            case R.id.tv_forget_password:
                BalancePassWordActivity.startThis(paySettingActivity);
                break;
            case R.id.tv_submit:
                String wenchatName = etWenchatName.getText().toString().trim();
                if (TextUtils.isEmpty(wenchatName)) {
                    ToastUtil.shortShow("请输入微信名");
                    return;
                }

                String webchatCode = etWebchatCode.getText().toString().trim();
                if (TextUtils.isEmpty(webchatCode)) {
                    ToastUtil.shortShow("请输入微信号");
                    return;
                }

                if (TextUtils.isEmpty(sIdcardFront)) {
                    ToastUtil.shortShow("请上传微信收款码");
                    return;
                }

                String forgetPassword = etForgetPassword.getText().toString().trim();
                if (TextUtils.isEmpty(forgetPassword)) {
                    ToastUtil.shortShow("请输入资金密码");
                    return;
                }
                long currentTime = System.currentTimeMillis();
                presenter.wechat(id, wenchatName, webchatCode, sIdcardFront, "", Md5Utils.getMD5(forgetPassword + currentTime), currentTime);
                break;
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
        mHandler.removeCallbacksAndMessages(null);
        if (show != null) {
            if (show.isShow()) {
                show.dismiss();
            }
            show = null;
        }
        if (showSaoYiSao != null) {
            if (showSaoYiSao.isShow()) {
                showSaoYiSao.dismiss();
            }
            showSaoYiSao = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 99: {
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
        intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true);
//        Bundle bundle = new Bundle();
//        //设置支持的扫码类型
//        bundle.putSerializable(Constant.EXTRA_SCAN_CODE_TYPE, mHashMap);
//        intent.putExtras(bundle);
        paySettingActivity.startActivityForResult(intent, 100);
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

    private void webChatGoLogin() {
        mHandler.removeCallbacksAndMessages(null);
        if (id <= 0) {
            return;
        }
        if (show == null) {
            show = new XPopup.Builder(getContext())
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .hasStatusBarShadow(true) //启用状态栏阴影
                    .asCustom(new WenChatBindingPopupView(getContext()));
        }
        if (!show.isShow()) {
            show.show();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reconnectionCount += 1;
                presenter.wechatLogin(id);
            }
        }, 2000);
    }
}
