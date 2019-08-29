package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BaseBean;

public interface BusinessSellDetailsContract {

    interface Presenter extends IBasePresenter {
        //订单申诉
        void appeal(String tradeId);
        //玩家点击放币
        void fb_transfer(String tradeId);
    }
    interface View extends IBaseView<Presenter> {
        //订单申诉成功
        void appealSuccess(BaseBean baseBean);
        //玩家点击放币成功
        void fb_transferSuccess(BaseBean baseBean);
    }
}
