package aimi.android.com.presenter;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.PaySetupModelAliPay;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.AliPayListContract;
import aimi.android.com.presenter.modle.AliPayListModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AliPayListPresenter implements AliPayListContract.Presenter{

    private AliPayListContract.View mView;
    private AliPayListModle mModel;

    public AliPayListPresenter(AliPayListContract.View view, AliPayListModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void aliPayListRefresh(int type) {
//        mView.showLoading();
        mModel.paysetupAliPay(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<PaySetupModelAliPay>() {
                    @Override
                    public void onSuccess(PaySetupModelAliPay paySetupModelAliPay) {
                        mView.aliPayListRefreshSuccess(paySetupModelAliPay);
//                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.aliPayListRefreshError();
//                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void aliPayListLoadMore(int type) {
//        mView.showLoading();
        mModel.paysetupAliPay(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<PaySetupModelAliPay>() {
                    @Override
                    public void onSuccess(PaySetupModelAliPay paySetupModelAliPay) {
                        mView.aliPayListLoadMoreSuccess(paySetupModelAliPay);
//                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.aliPayListLoadMoreError();
//                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void setDefaultPayAliPay(int type, long id, String financePwd, long time) {
        mView.showLoading();
        mModel.setDefaultPay(type,id,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.setDefaultPayAliPaySuccess(baseBean);
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
    public void detelePay(int type, long id, String financePwd, long time) {
        mView.showLoading();
        mModel.detelePay(type,id,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.detelePayAliPaySuccess(baseBean);
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
