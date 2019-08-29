package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.PaySetupModelAliPay;

public interface AliPayListContract {

    interface Presenter extends IBasePresenter {
        void aliPayListRefresh(int type);
        void aliPayListLoadMore(int type);
        void setDefaultPayAliPay(int type, long id, String financePwd, long time);
        void detelePay(int type,long id,String financePwd,long time);
    }
    interface View extends IBaseView<Presenter> {
        void aliPayListRefreshSuccess(PaySetupModelAliPay paySetupModelAliPay);
        void aliPayListRefreshError();
        void aliPayListLoadMoreSuccess(PaySetupModelAliPay paySetupModelAliPay);
        void aliPayListLoadMoreError();
        void setDefaultPayAliPaySuccess(BaseBean baseBean);
        void detelePayAliPaySuccess(BaseBean baseBean);
    }
}