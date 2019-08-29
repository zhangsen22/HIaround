package aimi.android.com.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.XPopupUtils;
import aimi.android.com.R;

/**
 * Description:
 * Create by dance, at 2019/5/5
 */
public class PagerBottomPopup extends BottomPopupView implements View.OnClickListener {

    private CheckBox cbYunshanfu;
    private CheckBox cbWebchat;
    private CheckBox cbIdCast;
    private OnSelectListener selectListener;
    private int type;//1为支付宝，2为微信，3为银行账户，4为云闪付     默认微信
    private String text;

    public PagerBottomPopup(@NonNull Context context, int payType, OnSelectListener selectListener) {
        super(context);
        this.selectListener = selectListener;
        this.type = payType;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_view_pager;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        cbWebchat = findViewById(R.id.cb_webchat);
        cbIdCast = findViewById(R.id.cb_idcast);
        cbYunshanfu = findViewById(R.id.cb_yunshanfu);
        cbWebchat.setOnClickListener(this);
        cbIdCast.setOnClickListener(this);
        cbYunshanfu.setOnClickListener(this);
        if (type == 2) {
            cbWebchat.setChecked(true);
            cbIdCast.setChecked(false);
            cbYunshanfu.setChecked(false);
        } else if (type == 3) {
            cbWebchat.setChecked(false);
            cbIdCast.setChecked(true);
            cbYunshanfu.setChecked(false);
        }else if (type == 4) {
            cbWebchat.setChecked(false);
            cbIdCast.setChecked(false);
            cbYunshanfu.setChecked(true);
        }
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .85f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_webchat:
                    cbWebchat.setChecked(true);
                    cbIdCast.setChecked(false);
                    cbYunshanfu.setChecked(false);
                    type = 2;
                    text = "微信支付";
                break;
            case R.id.cb_idcast:
                    cbIdCast.setChecked(true);
                    cbWebchat.setChecked(false);
                    cbYunshanfu.setChecked(false);
                    type = 3;
                    text = "银行卡支付";
                break;
            case R.id.cb_yunshanfu:
                    cbYunshanfu.setChecked(true);
                    cbWebchat.setChecked(false);
                    cbIdCast.setChecked(false);
                    type = 4;
                    text = "云闪付支付";
                break;
        }
        if (selectListener != null) {
            selectListener.onSelect(type, text);
        }
    }
}
