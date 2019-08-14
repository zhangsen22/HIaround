package hiaround.android.com.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import hiaround.android.com.ui.activity.InvitationRewardFirstActivity;
import hiaround.android.com.ui.activity.RecommendToFriendsActivity;
import hiaround.android.com.util.ToastUtil;

public class RecommendToFriendsFragment extends BaseFragment {
    private static final String TAG = RecommendToFriendsFragment.class.getSimpleName();
    @BindView(R.id.fl_title_comtent)
    FrameLayout flTitleComtent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_yqm)
    TextView tvYqm;
    @BindView(R.id.tv_zing_img)
    ImageView tvZingImg;
    @BindView(R.id.tv_download_url)
    TextView tvDownloadUrl;
    @BindView(R.id.tv_duplicate)
    TextView tvDuplicate;
    @BindView(R.id.tv_yaoqinglianjie)
    TextView tvYaoqinglianjie;
    @BindView(R.id.tuijianjiangli)
    TextView tuijianjiangli;
    private RecommendToFriendsActivity recommendToFriendsActivity;
    private Bitmap qrImage;
    private Bitmap bitmap;

    public static RecommendToFriendsFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        RecommendToFriendsFragment fragment = new RecommendToFriendsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recommendToFriendsActivity = (RecommendToFriendsActivity) getActivity();
    }

    @Override
    protected int getRootView() {
        return R.layout.recommend_to_friends_fragment;
    }

    @Override
    protected void initView(View root) {
        setRootViewPaddingTop(flTitleComtent);
        tvTitle.setText("推荐给好友");
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        tvYqm.setText(AccountManager.getInstance().getInvitedCode());
        String downloadUrl = AccountManager.getInstance().getDownloadUrl();
        bitmap = BitmapFactory.decodeResource(MyApplication.appContext.getResources(), R.drawable.ic_launcher_round);
        qrImage = QRCodeUtil.createQRCodeBitmap(downloadUrl, 650, 650, "UTF-8",
                "H", "1", Color.BLACK, Color.WHITE, bitmap, 0.3F, null);
        tvZingImg.setImageBitmap(qrImage);
        tvDownloadUrl.setText(downloadUrl);
    }

    @OnClick({R.id.iv_back, R.id.tv_duplicate,R.id.tv_yaoqinglianjie, R.id.tuijianjiangli})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                recommendToFriendsActivity.finish();
                break;
            case R.id.tv_duplicate:
            case R.id.tv_yaoqinglianjie:
            case R.id.tuijianjiangli:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) recommendToFriendsActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", tvDownloadUrl.getText().toString().trim());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.shortShow("已复制到剪贴板");

//                复制详情
//                1.普通字符型
//                ClipData mClipData =ClipData.newPlainText("Label", "Content");
//                //‘Label’这是任意文字标签
//
//                2.url型
//                ClipData.newRawUri("Label",Uri.parse("http://www.baidu.com"));
//
//                3.intent型
//                ClipData.newIntent("Label", intent);
//
//                获取剪切板数据
//                ClipboardManager.getPrimaryClip();
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
        super.onDestroyView();
    }
}
