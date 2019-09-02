package aimi.android.com.presenter.contract;


import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.PaySetupModelLaCara;

public interface LaCaraListContract {

    interface Presenter extends IBasePresenter {
        void laCaraListRefresh(int type);
        void laCaraListLoadMore(int type);
        void setDefaultPaylaCara(int type, long id, String financePwd, long time);
        void detelePay(int type, long id, String financePwd, long time);
    }
    interface View extends IBaseView<Presenter> {
        void laCaraListRefreshSuccess(PaySetupModelLaCara paySetupModelLaCara);
        void laCaraListRefreshError();
        void laCaraListLoadMoreSuccess(PaySetupModelLaCara paySetupModelLaCara);
        void laCaraListLoadMoreError();
        void setDefaultPayLaCaraSuccess(BaseBean baseBean);
        void deteleLaCaraSuccess(BaseBean baseBean);
    }
}

