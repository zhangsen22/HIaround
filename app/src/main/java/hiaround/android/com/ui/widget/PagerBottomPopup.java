package hiaround.android.com.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.XPopupUtils;
import hiaround.android.com.R;

/**
 * Description:
 * Create by dance, at 2019/5/5
 */
public class PagerBottomPopup extends BottomPopupView implements CompoundButton.OnCheckedChangeListener {

    private CheckBox cbWebchat;
    private CheckBox cbIdCast;
    private OnSelectListener selectListener;
    private int type;//1为支付宝，2为微信，3为银行账户        默认微信
    private String text;

    public PagerBottomPopup(@NonNull Context context,int payType, OnSelectListener selectListener) {
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
        cbWebchat.setOnCheckedChangeListener(this);
        cbIdCast.setOnCheckedChangeListener(this);
        if(type == 2){
            cbWebchat.setChecked(true);
            cbIdCast.setChecked(false);
        }else if(type == 3){
            cbWebchat.setChecked(false);
            cbIdCast.setChecked(true);
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
            case R.id.cb_webchat:
                if (isChecked) {
                    cbIdCast.setChecked(false);
                    type = 2;
                    text = "微信支付";
                } else {
                    cbIdCast.setChecked(true);
                    type = 3;
                    text = "银行卡支付";
                }
                break;
            case R.id.cb_idcast:
                if (isChecked) {
                    cbWebchat.setChecked(false);
                    type = 3;
                    text = "银行卡支付";
                } else {
                    cbWebchat.setChecked(true);
                    type = 2;
                    text = "微信支付";
                }
                break;
        }
        if(selectListener != null){
            selectListener.onSelect(type,text);
        }
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(popupInfo.autoDismiss)dismiss();
//            }
//        },500);
    }
}
