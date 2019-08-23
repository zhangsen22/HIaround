package hiaround.android.com.presenter.contract;


import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.modle.PaySetupModelYunShanFu;

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

