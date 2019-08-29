package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.SellLimitResponse;

public interface SellContract {

    interface Presenter extends IBasePresenter {
        void quickSell(int type,String financePwd,double rmb,long time);
        void sellLimit();
    }
    interface View extends IBaseView<Presenter> {
        void quickSellSuccess();
        void sellLimitSuccess(SellLimitResponse sellLimitResponse);
    }
}
