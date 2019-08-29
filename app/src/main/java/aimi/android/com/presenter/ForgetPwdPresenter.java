package aimi.android.com.presenter;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.ImageCodeResponse;
import aimi.android.com.modle.SmsCodeResponse;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.ForgetPwdContract;
import aimi.android.com.presenter.modle.ForgetPwdModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ForgetPwdPresenter implements ForgetPwdContract.Presenter {

    protected ForgetPwdContract.View mView;

    private ForgetPwdModle mModel;

    public ForgetPwdPresenter(ForgetPwdContract.View view, ForgetPwdModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
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
    public void forgetPwd(String pwd, String phoneNum, String imageCode, String smsCode) {
        mView.showLoading();
        mModel.forgetPwd(pwd,phoneNum,imageCode,smsCode).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.hideLoading();
                        mView.forgetPwdSuccess(baseBean);
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
