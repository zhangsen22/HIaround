package hiaround.android.com.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.XPopupUtils;
import hiaround.android.com.R;
import hiaround.android.com.app.AccountManager;
import hiaround.android.com.ui.activity.AddMakeStyleActivity;

/**
 * Description:
 * Create by dance, at 2019/5/5
 */
public class PagerBottomPopup1 extends BottomPopupView implements CompoundButton.OnCheckedChangeListener {

    LinearLayout llWebchat;
    LinearLayout llAlpiy;
    LinearLayout llBank;
    private Context context;
    CheckBox cbWebchat;
    CheckBox cbAlpiy;
    CheckBox cbBank;
    TextView tvAddShoukuan;
    private OnSelectListener selectListener;
    private int type = 0;//1为支付宝，2为微信，3为银行账户
    private String text;

    public PagerBottomPopup1(@NonNull Context context, OnSelectListener selectListener) {
        super(context);
        this.selectListener = selectListener;
        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_view_pager1;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        llWebchat = findViewById(R.id.ll_webchat);
        llAlpiy = findViewById(R.id.ll_alpiy);
        llBank = findViewById(R.id.ll_bank);
        cbWebchat = findViewById(R.id.cb_webchat);
        cbAlpiy = findViewById(R.id.cb_alpiy);
        cbBank = findViewById(R.id.cb_bank);
        tvAddShoukuan = findViewById(R.id.tv_add_shoukuan);
        cbWebchat.setOnCheckedChangeListener(this);
        cbAlpiy.setOnCheckedChangeListener(this);
        cbBank.setOnCheckedChangeListener(this);
        tvAddShoukuan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMakeStyleActivity.startThis(context);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (popupInfo.autoDismiss) dismiss();
                    }
                }, 100);
            }
        });
        if (AccountManager.getInstance().isHaveAliPayee()) {
            llAlpiy.setVisibility(VISIBLE);
        } else {
            llAlpiy.setVisibility(GONE);
        }
        if (AccountManager.getInstance().isHaveBankPayee()) {
            llBank.setVisibility(VISIBLE);
        } else {
            llBank.setVisibility(GONE);
        }
        if (AccountManager.getInstance().isHaveWechatPayee()) {
            llWebchat.setVisibility(VISIBLE);
        } else {
            llWebchat.setVisibility(GONE);
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
            case R.id.cb_alipay:
                if (isChecked) {
                    cbWebchat.setChecked(false);
                    cbBank.setChecked(false);
                    type = 1;
                    text = "支付宝";
                } else {
                    type = 0;
                    text = "请选择一种收款方式";
                }
                break;
            case R.id.cb_webchat:
                    if (isChecked) {
                        cbBank.setChecked(false);
                        cbAlpiy.setChecked(false);
                        type = 2;
                        text = "微信支付";
                    } else {
                        type = 0;
                        text = "请选择一种收款方式";
                    }
                break;
            case R.id.cb_idcast:
                    if (isChecked) {
                        cbAlpiy.setChecked(false);
                        cbWebchat.setChecked(false);
                        type = 3;
                        text = "银行卡";
                    } else {
                        type = 0;
                        text = "请选择一种收款方式";
                    }
                break;
        }

        if(selectListener != null){
            selectListener.onSelect(type,text);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (popupInfo.autoDismiss) dismiss();
            }
        }, 500);
    }
}
