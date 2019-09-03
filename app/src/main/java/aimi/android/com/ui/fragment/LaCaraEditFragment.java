package aimi.android.com.ui.fragment;

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
import com.growalong.util.util.AppPublicUtils;
import com.growalong.util.util.GALogger;
import com.growalong.util.util.Md5Utils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnSelectWebChatListener;

import java.util.List;

import aimi.android.com.BaseFragment;
import aimi.android.com.MyApplication;
import aimi.android.com.R;
import aimi.android.com.modle.LaCaraEditModle;
import aimi.android.com.modle.LaCaraPayeeItemModelPayee;
import aimi.android.com.modle.LaCaraWenChatListItem;
import aimi.android.com.modle.LaCaraWenChatListModle;
import aimi.android.com.presenter.LaCaraEditPresenter;
import aimi.android.com.presenter.contract.LaCaraEditContract;
import aimi.android.com.ui.activity.BalancePassWordActivity;
import aimi.android.com.ui.activity.PaySettingActivity;
import aimi.android.com.ui.widget.CustomPartShadowPopupView;
import aimi.android.com.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class LaCaraEditFragment extends BaseFragment implements LaCaraEditContract.View {
    private static final String TAG = LaCaraEditFragment.class.getSimpleName();
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
    private LaCaraEditPresenter presenter;
    private LaCaraPayeeItemModelPayee laCaraPayeeItemModelPayee = null;
    private String sIdcardFront;
    private Bitmap qrImage;
    private Bitmap bitmap;
    private long id = 0;
    private long wechatPaymentId = 0;
    private BasePopupView show;

    public static LaCaraEditFragment newInstance(LaCaraPayeeItemModelPayee laCaraPayeeItemModelPayee) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("laCaraPayeeItemModelPayee", laCaraPayeeItemModelPayee);
        LaCaraEditFragment fragment = new LaCaraEditFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paySettingActivity = (PaySettingActivity) getActivity();
        laCaraPayeeItemModelPayee = getArguments().getParcelable("laCaraPayeeItemModelPayee");
    }

    @Override
    protected int getRootView() {
        return R.layout.lacala_edit_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("拉卡拉设置");
        bitmap = BitmapFactory.decodeResource(MyApplication.appContext.getResources(), R.drawable.ic_launcher_round);
        AppPublicUtils.setEditTextEnable(etWenchatName, false);
        if (laCaraPayeeItemModelPayee == null) {
            id = 0;
        } else {
            id = laCaraPayeeItemModelPayee.getId();
            String account = laCaraPayeeItemModelPayee.getAccount();
            if (!TextUtils.isEmpty(account)) {
                etWebchatCode.setText(account);
                AppPublicUtils.setEditTextEnable(etWebchatCode, false);
            }
            etWenchatName.setText(laCaraPayeeItemModelPayee.getName());
            etWenchatName.setClickable(false);
            String base64Img = laCaraPayeeItemModelPayee.getBase64Img();
            creatCode(base64Img);
        }
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
    }

    @OnClick({R.id.iv_back, R.id.iv_webchat_image, R.id.tv_forget_password, R.id.tv_submit,R.id.et_wenchat_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                paySettingActivity.finish();
                break;
            case R.id.iv_webchat_image:
                if (ContextCompat.checkSelfPermission(paySettingActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    goScanner();
                } else {
                    ActivityCompat.requestPermissions(paySettingActivity, new String[]{Manifest.permission.CAMERA}, 130);
                }
                break;
            case R.id.tv_forget_password:
                BalancePassWordActivity.startThis(paySettingActivity);
                break;
            case R.id.tv_submit:
                if (laCaraPayeeItemModelPayee == null) {
                    String wenchatName = etWenchatName.getText().toString().trim();
                    if (TextUtils.isEmpty(wenchatName)) {
                        ToastUtil.shortShow("请选择该拉卡拉所绑定公众号的微信昵称");
                        return;
                    }

                    if (wechatPaymentId <= 0) {
                        ToastUtil.shortShow("请选择该拉卡拉所绑定公众号的微信昵称");
                        return;
                    }

                    String webchatCode = etWebchatCode.getText().toString().trim();
                    if (TextUtils.isEmpty(webchatCode)) {
                        ToastUtil.shortShow("请输入拉卡拉对应二维码的店铺名");
                        return;
                    }

                    if (TextUtils.isEmpty(sIdcardFront)) {
                        ToastUtil.shortShow("请上传拉卡拉收款二维码");
                        return;
                    }

                    String forgetPassword = etForgetPassword.getText().toString().trim();
                    if (TextUtils.isEmpty(forgetPassword)) {
                        ToastUtil.shortShow("请输入资金密码");
                        return;
                    }
                    long currentTime = System.currentTimeMillis();
                    presenter.laCara(id, wechatPaymentId, webchatCode, sIdcardFront, Md5Utils.getMD5(forgetPassword + currentTime), currentTime);
                } else {
                    if (TextUtils.isEmpty(sIdcardFront)) {
                        ToastUtil.shortShow("请上传拉卡拉收款二维码");
                        return;
                    }

                    String forgetPassword = etForgetPassword.getText().toString().trim();
                    if (TextUtils.isEmpty(forgetPassword)) {
                        ToastUtil.shortShow("请输入资金密码");
                        return;
                    }
                    long currentTime = System.currentTimeMillis();
                    presenter.lakalaImgSetUp(id, sIdcardFront, Md5Utils.getMD5(forgetPassword + currentTime), currentTime);
                }
                break;
            case R.id.et_wenchat_name:
                presenter.getWechatList();
                break;
        }
    }

    @Override
    public void setPresenter(LaCaraEditContract.Presenter presenter) {
        this.presenter = (LaCaraEditPresenter) presenter;
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
        paySettingActivity.startActivityForResult(intent, 103);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 130: {
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
        if (show != null) {
            if (show.isShow()) {
                show.dismiss();
            }
            show = null;
        }
        super.onDestroyView();
    }


    @Override
    public void laCaraSuccess(LaCaraEditModle laCaraEditModle) {
        if(laCaraEditModle != null){
            long paymentId = laCaraEditModle.getPaymentId();
            if(paymentId > 0) {
                paySettingActivity.setResult(Activity.RESULT_OK);
                paySettingActivity.finish();
            }
        }
    }

    @Override
    public void lakalaImgSetUpSuccess(LaCaraEditModle laCaraEditModle) {
        if(laCaraEditModle != null){
            long paymentId = laCaraEditModle.getPaymentId();
            if(paymentId > 0) {
                paySettingActivity.setResult(Activity.RESULT_OK);
                paySettingActivity.finish();
            }
        }
    }

    @Override
    public void getWechatListSuccess(LaCaraWenChatListModle laCaraWenChatListModle) {
        if(laCaraWenChatListModle != null){
            List<LaCaraWenChatListItem> list = laCaraWenChatListModle.getList();
            if(list != null && list.size() > 0){
                showPartShadow(list);
            }
        }
    }

    private void showPartShadow(List<LaCaraWenChatListItem> list){
        if(show != null && show.isShow()){
            return;
        }
        show = new XPopup.Builder(getContext())
                .atView(etWenchatName)
//                .dismissOnTouchOutside(false)
                .asCustom(new CustomPartShadowPopupView(getContext(), list, new OnSelectWebChatListener() {
                    @Override
                    public void onSelect(int position, long paymentId, String account) {
                        wechatPaymentId = paymentId;
                        etWenchatName.setText(account);
                    }
                },paySettingActivity)).show();
    }
}
