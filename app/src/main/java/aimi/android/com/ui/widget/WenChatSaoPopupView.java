package aimi.android.com.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrcode.utils.QRCodeUtil;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import aimi.android.com.R;

/**
 * Description: 微信收款设置弹窗
 */
public class WenChatSaoPopupView extends CenterPopupView {

    private ImageView ivImageCode;
    private TextView tvOkSao;
    private String mLoginCode;
    OnConfirmListener confirmListener;

    public WenChatSaoPopupView(@NonNull Context context, String loginCode,OnConfirmListener confirmListener) {
        super(context);
        this.mLoginCode = loginCode;
        this.confirmListener = confirmListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.webchat_sao_mapopupview;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ivImageCode = findViewById(R.id.iv_image_code);
        tvOkSao = findViewById(R.id.tv_ok_sao);
        Bitmap qrImage = QRCodeUtil.createQRCodeBitmap(mLoginCode, 650, 650, "UTF-8",
                "H", "1", Color.BLACK, Color.WHITE, null, 0.2F, null);
        ivImageCode.setImageBitmap(qrImage);
        tvOkSao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmListener!=null)confirmListener.onConfirm();
                dismiss();
            }
        });
    }
}
