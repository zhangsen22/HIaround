package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BaseBean;

public interface EntrustBuyContract {

    interface Presenter extends IBasePresenter {
        //委托购买
        void putUpBuy(double price, double minNum, double maxNum, String financePwd, long time);
    }
    interface View extends IBaseView<Presenter> {
        //委托购买
        void putUpBuySuccess(BaseBean baseBean);
    }
}
