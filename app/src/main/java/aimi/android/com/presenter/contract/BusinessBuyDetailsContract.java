package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BaseBean;

public interface BusinessBuyDetailsContract {

    interface Presenter extends IBasePresenter {
        void ordercancel(String tradeId);
        void manualPay(String tradeId);
    }
    interface View extends IBaseView<Presenter> {
        void ordercancelSuccess(BaseBean baseBean);
        void manualPaySuccess(BaseBean baseBean);
        void goOrderMySellComplete();//订单已付款  订单完成   跳转订单我的卖出页面
    }
}
