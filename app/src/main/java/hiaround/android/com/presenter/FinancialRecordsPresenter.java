package hiaround.android.com.presenter;

import hiaround.android.com.modle.FinanceLogResponse;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.FinancialRecordsContract;
import hiaround.android.com.presenter.modle.FinancialRecordsModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FinancialRecordsPresenter implements FinancialRecordsContract.Presenter{

    private FinancialRecordsContract.View mView;
    private FinancialRecordsModle mModel;

    public FinancialRecordsPresenter(FinancialRecordsContract.View view, FinancialRecordsModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void financeLogRefresh(long minId) {
//        mView.showLoading();
        mModel.financeLog(minId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<FinanceLogResponse>() {
                    @Override
                    public void onSuccess(FinanceLogResponse financeLogResponse) {
                        mView.financeLogRefreshSuccess(financeLogResponse);
//                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.financeLogRefreshError();
//                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void financeLogLoadMore(long minId) {
        mModel.financeLog(minId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<FinanceLogResponse>() {
                    @Override
                    public void onSuccess(FinanceLogResponse financeLogResponse) {
                        mView.financeLogLoadMoreSuccess(financeLogResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.financeLogLoadMoreError();
                    }
                });
    }

    @Override
    public void starLoadData() {

    }
}
