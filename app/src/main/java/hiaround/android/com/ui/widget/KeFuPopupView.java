package hiaround.android.com.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import com.example.qrcode.utils.QRCodeUtil;
import com.lxj.xpopup.core.CenterPopupView;
import hiaround.android.com.R;

/**
 * Description: 微信收款设置弹窗
 */
public class KeFuPopupView extends CenterPopupView {

    ImageView ivWebchatImageCode;
    ImageView ivTgImageCode;

    public KeFuPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.kefu_mapopupview;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ivWebchatImageCode = findViewById(R.id.iv_webchat_image_code);
        ivTgImageCode = findViewById(R.id.iv_tg_image_code);
        Bitmap qrImage = QRCodeUtil.createQRCodeBitmap("微信客服", 650, 650, "UTF-8",
                "H", "1", Color.BLACK, Color.WHITE, null, 0.2F, null);
        ivWebchatImageCode.setImageBitmap(qrImage);

        Bitmap qrImage1 = QRCodeUtil.createQRCodeBitmap("客服telegram", 650, 650, "UTF-8",
                "H", "1", Color.BLACK, Color.WHITE, null, 0.2F, null);
        ivTgImageCode.setImageBitmap(qrImage1);
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }
}
