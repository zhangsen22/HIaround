package aimi.android.com.presenter;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.SellLimitResponse;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.SellContract;
import aimi.android.com.presenter.modle.BuyModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SellPresenter implements SellContract.Presenter{

    protected SellContract.View mView;
    private BuyModle mModel;

    public SellPresenter(SellContract.View view, BuyModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void starLoadData() {

    }

    @Override
    public void quickSell(int type, String financePwd, double rmb, long time) {
        mView.showLoading();
        mModel.quickSell(type,financePwd,rmb,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean buyResponse) {
                        mView.quickSellSuccess();
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
    public void sellLimit() {
        mModel.sellLimit().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<SellLimitResponse>() {
                    @Override
                    public void onSuccess(SellLimitResponse sellLimitResponse) {
                        mView.sellLimitSuccess(sellLimitResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                    }
                });
    }
}
