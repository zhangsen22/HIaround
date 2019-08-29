package aimi.android.com.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import com.lxj.xpopup.core.CenterPopupView;
import aimi.android.com.R;

/**
 * Description: 微信收款设置弹窗
 */
public class KeFuPopupView extends CenterPopupView {

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
