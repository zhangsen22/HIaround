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
    private CheckBox cbYunshanfu;
    private OnSelectListener selectListener;
    private int type = 2;//1为支付宝，2为微信，3为银行账户        默认微信
    private String text;

    public PagerBottomPopup(@NonNull Context context, OnSelectListener selectListener) {
        super(context);
        this.selectListener = selectListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_view_pager;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        cbWebchat = findViewById(R.id.cb_webchat);
        cbYunshanfu = findViewById(R.id.cb_yunshanfu);
        cbWebchat.setOnCheckedChangeListener(this);
        cbYunshanfu.setOnCheckedChangeListener(this);
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
                cbWebchat.setChecked(true);
                type = 2;
                text = "微信支付";
                break;
        }
        if(selectListener != null){
            selectListener.onSelect(type,text);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if(popupInfo.autoDismiss)dismiss();
            }
        },500);
    }
}
