package aimi.android.com.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.XPopupUtils;
import aimi.android.com.R;
import aimi.android.com.app.AccountManager;
import aimi.android.com.ui.activity.AddMakeStyleActivity;

/**
 * Description:
 * Create by dance, at 2019/5/5
 */
public class PagerBottomPopup1 extends BottomPopupView implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    TextView tvWebchat;
    TextView tvAlpiy;
    TextView tvBank;
    TextView tvYunshanfu;
    CheckBox cbYunshanfu;
    private Context context;
    CheckBox cbWebchat;
    CheckBox cbAlpiy;
    CheckBox cbBank;
    private OnSelectListener selectListener;
    private int type = 0;//1为支付宝，2为微信，3为银行账，4为云闪付
    private String text;
    private int payType;

    public PagerBottomPopup1(@NonNull Context context, int payType, OnSelectListener selectListener) {
        super(context);
        this.selectListener = selectListener;
        this.context = context;
        this.payType = payType;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_view_pager1;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvWebchat = findViewById(R.id.tv_webchat);
        tvAlpiy = findViewById(R.id.tv_alpiy);
        tvBank = findViewById(R.id.tv_bank);
        cbWebchat = findViewById(R.id.cb_webchat);
        cbAlpiy = findViewById(R.id.cb_alpiy);
        cbBank = findViewById(R.id.cb_bank);
        tvYunshanfu = findViewById(R.id.tv_yunshanfu);
        cbYunshanfu = findViewById(R.id.cb_yunshanfu);
        cbWebchat.setOnCheckedChangeListener(this);
        cbAlpiy.setOnCheckedChangeListener(this);
        cbBank.setOnCheckedChangeListener(this);
        cbYunshanfu.setOnCheckedChangeListener(this);
        tvWebchat.setOnClickListener(this);
        tvAlpiy.setOnClickListener(this);
        tvBank.setOnClickListener(this);
        tvYunshanfu.setOnClickListener(this);
        if (payType == 0) {
            cbAlpiy.setChecked(false);
            cbBank.setChecked(false);
            cbWebchat.setChecked(false);
            cbYunshanfu.setChecked(false);
        } else if (payType == 1) {
            cbAlpiy.setChecked(true);
            cbBank.setChecked(false);
            cbWebchat.setChecked(false);
            cbYunshanfu.setChecked(false);
        } else if (payType == 2) {
            cbAlpiy.setChecked(false);
            cbBank.setChecked(false);
            cbWebchat.setChecked(true);
            cbYunshanfu.setChecked(false);
        } else if (payType == 3) {
            cbAlpiy.setChecked(false);
            cbBank.setChecked(true);
            cbWebchat.setChecked(false);
            cbYunshanfu.setChecked(false);
        }else if (payType == 4) {
            cbAlpiy.setChecked(false);
            cbBank.setChecked(false);
            cbWebchat.setChecked(false);
            cbYunshanfu.setChecked(true);
        }
        if (AccountManager.getInstance().isHaveAliPayee()) {
            cbAlpiy.setVisibility(VISIBLE);
            tvAlpiy.setVisibility(GONE);
        } else {
            cbAlpiy.setVisibility(GONE);
            tvAlpiy.setVisibility(VISIBLE);
        }
        if (AccountManager.getInstance().isHaveBankPayee()) {
            cbBank.setVisibility(VISIBLE);
            tvBank.setVisibility(GONE);
        } else {
            cbBank.setVisibility(GONE);
            tvBank.setVisibility(VISIBLE);
        }
        if (AccountManager.getInstance().isHaveWechatPayee()) {
            cbWebchat.setVisibility(VISIBLE);
            tvWebchat.setVisibility(GONE);
        } else {
            cbWebchat.setVisibility(GONE);
            tvWebchat.setVisibility(VISIBLE);
        }
        if (AccountManager.getInstance().isHaveCloudPayee()) {
            cbYunshanfu.setVisibility(VISIBLE);
            tvYunshanfu.setVisibility(GONE);
        } else {
            cbYunshanfu.setVisibility(GONE);
            tvYunshanfu.setVisibility(VISIBLE);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_alpiy:
                if (isChecked) {
                    cbWebchat.setChecked(false);
                    cbBank.setChecked(false);
                    cbYunshanfu.setChecked(false);
                    type = 1;
                    text = "支付宝";
                } else {
                    type = 0;
                    text = "";
                }
                break;
            case R.id.cb_webchat:
                if (isChecked) {
                    cbBank.setChecked(false);
                    cbAlpiy.setChecked(false);
                    cbYunshanfu.setChecked(false);
                    type = 2;
                    text = "微信支付";
                } else {
                    type = 0;
                    text = "";
                }
                break;
            case R.id.cb_bank:
                if (isChecked) {
                    cbAlpiy.setChecked(false);
                    cbWebchat.setChecked(false);
                    cbYunshanfu.setChecked(false);
                    type = 3;
                    text = "银行卡";
                } else {
                    type = 0;
                    text = "";
                }
                break;
            case R.id.cb_yunshanfu:
                if (isChecked) {
                    cbAlpiy.setChecked(false);
                    cbWebchat.setChecked(false);
                    cbBank.setChecked(false);
                    type = 4;
                    text = "云闪付";
                } else {
                    type = 0;
                    text = "";
                }
                break;
        }

        if (selectListener != null) {
            selectListener.onSelect(type, text);
        }
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (popupInfo.autoDismiss) dismiss();
//            }
//        }, 500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_webchat:
            case R.id.tv_alpiy:
            case R.id.tv_bank:
            case R.id.tv_yunshanfu:
                AddMakeStyleActivity.startThis(context);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (popupInfo.autoDismiss) dismiss();
                    }
                }, 100);
        }
    }
}
