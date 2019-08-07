package hiaround.android.com.presenter;

import hiaround.android.com.app.AccountInfo;
import hiaround.android.com.modle.ImageCodeResponse;
import hiaround.android.com.modle.SmsCodeResponse;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.RegistContract;
import hiaround.android.com.presenter.modle.RegistModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RegistPresenter implements RegistContract.Presenter {

    protected RegistContract.View mView;

    private RegistModle mModel;

    public RegistPresenter(RegistContract.View view, RegistModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void getImageCode(String phoneNum) {
        mView.showLoading();
        mModel.getImageCode(phoneNum).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<ImageCodeResponse>() {
                    @Override
                    public void onSuccess(ImageCodeResponse imageCodeResponse) {
                        mView.hideLoading();
                        mView.getImageCodeSuccess(imageCodeResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void senSenSmsCode(String phoneNum) {
        mView.showLoading();
        mModel.senSenSmsCode(phoneNum).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<SmsCodeResponse>() {
                    @Override
                    public void onSuccess(SmsCodeResponse smsCodeResponse) {
                        mView.hideLoading();
                        mView.senSenSmsCodeSuccess(smsCodeResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void registerAndLogin(String phoneNum, String invitedCode, String imageCode, String smsCode, String pwd, long time, String password) {
        mView.showLoading();
        mModel.registerAndLogin(phoneNum,invitedCode,imageCode,smsCode,pwd,time,password).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<AccountInfo>() {
                    @Override
                    public void onSuccess(AccountInfo result) {
                        mView.hideLoading();
                        mView.registerAndLoginSuccess(result);
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
