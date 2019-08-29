package aimi.android.com.presenter;

import aimi.android.com.modle.WalletResponse;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.BusinessContainerContract;
import aimi.android.com.presenter.modle.BusinessContainerModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BusinessContainerPresenter implements BusinessContainerContract.Presenter{

    private BusinessContainerContract.View mView;
    private BusinessContainerModle mModel;

    public BusinessContainerPresenter(BusinessContainerContract.View view, BusinessContainerModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void getInfo() {
//        mView.showLoading();
        mModel.getInfo().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<WalletResponse>() {
                    @Override
                    public void onSuccess(WalletResponse walletResponse) {
                        mView.getInfoSuccess(walletResponse);
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
