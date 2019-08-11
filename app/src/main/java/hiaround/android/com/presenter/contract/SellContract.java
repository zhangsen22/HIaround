package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.SellLimitResponse;

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
