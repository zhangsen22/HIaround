package aimi.android.com.presenter;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.WalletResponse;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.TiBiContract;
import aimi.android.com.presenter.modle.TiBiModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class TiBiPresenter implements TiBiContract.Presenter{

    private TiBiContract.View mView;
    private TiBiModle mModel;

    public TiBiPresenter(TiBiContract.View view, TiBiModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void withdraw(String addr, double num, String financePwd, long time) {
        mView.showLoading();
        mModel.withdraw(addr,num,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.withdrawSuccess(baseBean);
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
