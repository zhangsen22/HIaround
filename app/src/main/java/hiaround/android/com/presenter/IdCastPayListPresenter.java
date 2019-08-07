package hiaround.android.com.presenter;

import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.modle.PaySetupModelBank;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.IdCastPayListContract;
import hiaround.android.com.presenter.modle.IdCastPayListModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class IdCastPayListPresenter implements IdCastPayListContract.Presenter{

    private IdCastPayListContract.View mView;
    private IdCastPayListModle mModel;

    public IdCastPayListPresenter(IdCastPayListContract.View view, IdCastPayListModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void paysetupBankRefresh(int type) {
//        mView.showLoading();
        mModel.paysetupBank(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<PaySetupModelBank>() {
                    @Override
                    public void onSuccess(PaySetupModelBank paySetupModelBank) {
                        mView.paysetupBankRefreshSuccess(paySetupModelBank);
//                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.paysetupBankRefreshError();
//                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void paysetupBankLoadMore(int type) {
//        mView.showLoading();
        mModel.paysetupBank(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<PaySetupModelBank>() {
                    @Override
                    public void onSuccess(PaySetupModelBank paySetupModelBank) {
                        mView.paysetupBankLoadMoreSuccess(paySetupModelBank);
//                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.paysetupBankLoadMoreError();
//                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void setDefaultPayIdCast(int type, long id, String financePwd, long time) {
        mView.showLoading();
        mModel.setDefaultPay(type,id,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.setDefaultPayIdCastSuccess(baseBean);
                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void detelePay(int type, long id, String financePwd, long time) {
        mView.showLoading();
        mModel.detelePay(type,id,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.detelePayBankSuccess(baseBean);
                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void starLoadData() {

    }
}
