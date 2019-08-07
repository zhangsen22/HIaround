package hiaround.android.com.presenter;

import hiaround.android.com.modle.WalletResponse;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.RansferOfFundsContract;
import hiaround.android.com.presenter.modle.RansferOfFundsModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RansferOfFundsPresenter implements RansferOfFundsContract.Presenter{

    private RansferOfFundsContract.View mView;
    private RansferOfFundsModle mModel;

    public RansferOfFundsPresenter(RansferOfFundsContract.View view, RansferOfFundsModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void transfer(int type, double num, String financePwd, long time) {
        mView.showLoading();
        mModel.transfer(type,num,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<WalletResponse>() {
                    @Override
                    public void onSuccess(WalletResponse walletResponse) {
                        mView.transferSuccess(walletResponse);
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
