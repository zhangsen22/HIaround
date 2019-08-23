package hiaround.android.com.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.qrcode.utils.QRCodeUtil;
import com.growalong.util.util.GsonUtil;
import com.lxj.xpopup.core.CenterPopupView;
import hiaround.android.com.MyApplication;
import hiaround.android.com.R;
import hiaround.android.com.modle.AliPayee;
import hiaround.android.com.modle.WechatPayee;
import hiaround.android.com.modle.YunShanFuPayee;

/**
 * Description: 在中间的二维码对话框
 */
public class CenterErWeiMaPopupView extends CenterPopupView {

    private ImageView ivImageCode;
    private TextView tvAccount;
    private TextView tvPayTypeName;
    private TextView tvDespic;
    private TextView tvPayAllMoney;
    private int type;
    private String payee;
    private String usdtTotalMoneyFmt;

    public CenterErWeiMaPopupView(@NonNull Context context, int type, String payee, String usdtTotalMoneyFmt) {
        super(context);
        this.type = type;
        this.payee = payee;
        this.usdtTotalMoneyFmt = usdtTotalMoneyFmt;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.center_erwei_mapopupview;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ivImageCode = findViewById(R.id.iv_image_code);
        tvAccount = findViewById(R.id.tv_account);
        tvPayAllMoney = findViewById(R.id.tv_pay_all_money);
        tvPayTypeName = findViewById(R.id.tv_pay_type_name);
        tvDespic = findViewById(R.id.tv_despic);
        if(usdtTotalMoneyFmt == null){
            tvPayAllMoney.setVisibility(GONE);
        }else {
            tvPayAllMoney.setVisibility(VISIBLE);
            tvPayAllMoney.setText(usdtTotalMoneyFmt);
        }
        Bitmap bitmapLog = BitmapFactory.decodeResource(MyApplication.appContext.getResources(), R.drawable.ic_launcher_round);
        if (type == 1) {//收款方式,1为支付宝，2为微信，3为银行账户
            tvPayTypeName.setText("请用支付宝扫一扫");
            AliPayee aliPayee = GsonUtil.getInstance().getServerBean(payee, AliPayee.class);
            if (aliPayee != null) {
                String account = aliPayee.getAccount();
                if (!TextUtils.isEmpty(account)) {
                    tvAccount.setVisibility(VISIBLE);
                    tvAccount.setText(account);
                } else {
                    tvAccount.setVisibility(GONE);
                    tvDespic.setText("扫描二维码验证");
                }
                Bitmap qrImage = QRCodeUtil.createQRCodeBitmap(aliPayee.getBase64Img(), 650, 650, "UTF-8",
                        "H", "1", Color.BLACK, Color.WHITE, bitmapLog, 0.2F, null);
                ivImageCode.setImageBitmap(qrImage);
            }
        } else if (type == 2) {
            tvPayTypeName.setText("请用微信扫一扫");
            WechatPayee wechatPayee = GsonUtil.getInstance().getServerBean(payee, WechatPayee.class);
            if (wechatPayee != null) {
                String account = wechatPayee.getAccount();
                if (!TextUtils.isEmpty(account)) {
                    tvAccount.setVisibility(VISIBLE);
                    tvAccount.setText(account);
                } else {
                    tvAccount.setVisibility(GONE);
                    tvDespic.setText("扫描二维码验证");
                }
                Bitmap qrImage = QRCodeUtil.createQRCodeBitmap(wechatPayee.getBase64Img(), 650, 650, "UTF-8",
                        "H", "1", Color.BLACK, Color.WHITE, bitmapLog, 0.2F, null);
                ivImageCode.setImageBitmap(qrImage);
            }
        } else if (type == 3) {
            tvPayTypeName.setText("请用银联扫一扫");
        }else if (type == 4) {
            tvPayTypeName.setText("请用云闪付扫一扫");
            YunShanFuPayee yunShanFuPayee =  GsonUtil.getInstance().getServerBean(payee,YunShanFuPayee.class);
            if(yunShanFuPayee != null){
                String account = yunShanFuPayee.getAccount();
                if(!TextUtils.isEmpty(account)){
                    tvAccount.setVisibility(VISIBLE);
                    tvAccount.setText(account);
                }else {
                    tvAccount.setVisibility(GONE);
                    tvDespic.setText("扫描二维码验证");
                }
                Bitmap qrImage = QRCodeUtil.createQRCodeBitmap(yunShanFuPayee.getBase64Img(), 650, 650, "UTF-8",
                        "H", "1", Color.BLACK, Color.WHITE, bitmapLog, 0.2F, null);
                ivImageCode.setImageBitmap(qrImage);
            }
        }
    }
}
