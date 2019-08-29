package aimi.android.com.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import com.lxj.xpopup.core.CenterPopupView;
import aimi.android.com.R;

/**
 * Description: 微信收款设置弹窗
 */
public class WenChatBindingPopupView extends CenterPopupView {


    public WenChatBindingPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.webchat_binding_mapopupview;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
    }
}
