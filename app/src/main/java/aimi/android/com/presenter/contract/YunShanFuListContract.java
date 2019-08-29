package aimi.android.com.presenter.contract;


import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.PaySetupModelYunShanFu;

public interface YunShanFuListContract {

    interface Presenter extends IBasePresenter {
        void yunShanFuListRefresh(int type);
        void yunShanFuListLoadMore(int type);
        void setDefaultPayyunShanFu(int type, long id, String financePwd, long time);
        void detelePay(int type, long id, String financePwd, long time);
    }
    interface View extends IBaseView<Presenter> {
        void yunShanFuListRefreshSuccess(PaySetupModelYunShanFu paySetupModelYunShanFu);
        void yunShanFuListRefreshError();
        void yunShanFuListLoadMoreSuccess(PaySetupModelYunShanFu paySetupModelYunShanFu);
        void yunShanFuListLoadMoreError();
        void setDefaultPayYunShanFuSuccess(BaseBean baseBean);
        void deteleYunShanFuSuccess(BaseBean baseBean);
    }
}

