package hiaround.android.com.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrcode.utils.QRCodeUtil;

import butterknife.BindView;
import butterknife.OnClick;
import hiaround.android.com.BaseFragment;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.app.AccountManager;
import hiaround.android.com.app.Constants;
import hiaround.android.com.ui.activity.ChongBiActivity;
import hiaround.android.com.util.FileUtils;
import hiaround.android.com.util.ToastUtil;

public class ChongBiFragment extends BaseFragment {
    private static final String TAG = ChongBiFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_bi_type)
    TextView tvBiType;
    @BindView(R.id.tv_bizing_img)
    ImageView tvBizingImg;
    @BindView(R.id.tv_save_zing_image)
    TextView tvSaveZingImage;
    @BindView(R.id.tv_bi_address)
    TextView tvBiAddress;
    @BindView(R.id.tv_copybi_type)
    TextView tvCopybiType;
    private ChongBiActivity chongBiActivity;
    private Bitmap qrImage;
    private Bitmap bitmap;

    public static ChongBiFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        ChongBiFragment fragment = new ChongBiFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chongBiActivity = (ChongBiActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.chong_bi_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("充币");
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        tvBiType.setText("USDT");
        String walletAddr = AccountManager.getInstance().getWalletAddr();
        if(TextUtils.isEmpty(walletAddr)){
            ToastUtil.shortShow("钱包地址为空");
            chongBiActivity.finish();
        }
        bitmap = BitmapFactory.decodeResource(MyApplication.appContext.getResources(), R.drawable.ic_launcher_round);
        qrImage = QRCodeUtil.createQRCodeBitmap(walletAddr, 650, 650, "UTF-8",
                "H", "1", Color.BLACK, Color.WHITE, bitmap, 0.2F, null);
        tvBizingImg.setImageBitmap(qrImage);
        tvBiAddress.setText(walletAddr);
    }

    @OnClick({R.id.iv_back, R.id.tv_save_zing_image, R.id.tv_copybi_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                chongBiActivity.finish();
                break;
            case R.id.tv_save_zing_image:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtils.saveBitmapToGallery(qrImage, Constants.FILTERCHONGBI_IMAGE_PATH);
                    }
                }).start();
                break;
            case R.id.tv_copybi_type:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) chongBiActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", tvBiAddress.getText().toString().trim());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.shortShow("已复制到剪贴板");
                break;
        }
    }

    @Override
    public void onDestroyView() {
        if(qrImage != null && !qrImage.isRecycled()){
            qrImage.recycle();
            qrImage = null;
        }
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        super.onDestroyView();
    }
}
