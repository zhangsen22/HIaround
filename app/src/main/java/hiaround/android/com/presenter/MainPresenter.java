package hiaround.android.com.presenter;

import hiaround.android.com.modle.UsdtPriceResponse;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.MainContract;
import hiaround.android.com.presenter.modle.MainModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainPresenter implements MainContract.Presenter{

    private MainContract.View mView;
    private MainModle mModel;

    public MainPresenter(MainContract.View view, MainModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void usdtPrice() {
//        mView.showLoading();
        mModel.usdtPrice().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<UsdtPriceResponse>() {
                    @Override
                    public void onSuccess(UsdtPriceResponse usdtPriceResponse) {
                        mView.usdtPriceSuccess(usdtPriceResponse);
//                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
//                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void starLoadData() {

    }
}
