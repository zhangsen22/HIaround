package aimi.android.com.presenter;

import aimi.android.com.modle.YnShanFuEditModle;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.YunShanFuLoginContract;
import aimi.android.com.presenter.modle.PaySettingModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class YunShanFuLoginPresenter implements YunShanFuLoginContract.Presenter{

    private YunShanFuLoginContract.View mView;
    private PaySettingModle mModel;

    public YunShanFuLoginPresenter(YunShanFuLoginContract.View view, PaySettingModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void cloudLogin(long paymentId, String cookieUser, String username) {
        mView.showLoading();
        mModel.cloudLogin(paymentId,cookieUser,username).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<YnShanFuEditModle>() {
                    @Override
                    public void onSuccess(YnShanFuEditModle ynShanFuEditModle) {
                        mView.cloudLoginSuccess(ynShanFuEditModle);
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
