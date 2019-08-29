package aimi.android.com.presenter;

import aimi.android.com.modle.BuyAmountListResponse;
import aimi.android.com.modle.BuyBusinessResponse;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.BuyContract;
import aimi.android.com.presenter.modle.BuyModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BuyPresenter implements BuyContract.Presenter{

    private BuyContract.View mView;
    private BuyModle mModel;

    public BuyPresenter(BuyContract.View view, BuyModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void starLoadData() {

    }

    @Override
    public void buyAmountList() {
        mModel.buyAmountList().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BuyAmountListResponse>() {
                    @Override
                    public void onSuccess(BuyAmountListResponse buyResponse) {
                        mView.buyAmountListSuccess(buyResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.buyAmountListError();
                    }
                });
    }

    @Override
    public void quickBuy(String amount, int type) {
        mView.showLoading();
        mModel.quickBuy(amount,type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BuyBusinessResponse>() {
                    @Override
                    public void onSuccess(BuyBusinessResponse buyResponse) {
                        mView.quickBuySuccess(buyResponse);
                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.hideLoading();
                    }
                });
    }
}
