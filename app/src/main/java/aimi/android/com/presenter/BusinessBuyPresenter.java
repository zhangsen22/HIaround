package aimi.android.com.presenter;

import aimi.android.com.modle.BuyBusinessResponse;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.BusinessBuyContract;
import aimi.android.com.presenter.modle.BusinessBuyModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BusinessBuyPresenter implements BusinessBuyContract.Presenter{
    private BusinessBuyContract.View mView;
    private BusinessBuyModle mModel;

    public BusinessBuyPresenter(BusinessBuyContract.View view, BusinessBuyModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void buy(long billId, double num, int type) {
        mView.showLoading();
        mModel.buy(billId,num,type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BuyBusinessResponse>() {
                    @Override
                    public void onSuccess(BuyBusinessResponse buyBusinessResponse) {
                        mView.buySuccess(buyBusinessResponse,type);
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
