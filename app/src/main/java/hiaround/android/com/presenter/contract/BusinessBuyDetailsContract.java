package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.BaseBean;

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
