package hiaround.android.com.presenter;

import hiaround.android.com.modle.WalletResponse;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.PropertyContract;
import hiaround.android.com.presenter.modle.PropertyModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PropertyPresenter implements PropertyContract.Presenter{

    private PropertyContract.View mView;
    private PropertyModle mModel;

    public PropertyPresenter(PropertyContract.View view, PropertyModle model){
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
