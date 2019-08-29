package aimi.android.com.presenter;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.SmsCodeResponse;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.BalancePassWordContract;
import aimi.android.com.presenter.modle.BalancePassWordModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BalancePassWordPresenter implements BalancePassWordContract.Presenter{

    private BalancePassWordContract.View mView;
    private BalancePassWordModle mModel;

    public BalancePassWordPresenter(BalancePassWordContract.View view, BalancePassWordModle model){
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
    public void changeFinancePwd(String financePwd, String smsCode) {
        mView.showLoading();
        mModel.changeFinancePwd(financePwd,smsCode).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.hideLoading();
                        mView.changeFinancePwdSuccess(baseBean);
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
