package hiaround.android.com.presenter;

import hiaround.android.com.modle.BuyResponse;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.BuyContract;
import hiaround.android.com.presenter.modle.BuyModle;
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
    public void getBuyRefresh(long minId) {
        mModel.getSellinfo(minId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BuyResponse>() {
                    @Override
                    public void onSuccess(BuyResponse buyResponse) {
                        mView.getBuyRefreshSuccess(buyResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.getBuyRefreshError();
                    }
                });
    }

    @Override
    public void getBuyLoadMore(long minId) {
        mModel.getSellinfo(minId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BuyResponse>() {
                    @Override
                    public void onSuccess(BuyResponse buyResponse) {
                        mView.getBuyLoadMoreSuccess(buyResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.getBuyLoadMoreError();
                    }
                });
    }

    @Override
    public void starLoadData() {

    }
}
