package aimi.android.com.presenter;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.PaySetupModelLaCara;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.LaCaraListContract;
import aimi.android.com.presenter.modle.LaCaraListModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LaCaraListPresenter implements LaCaraListContract.Presenter{

    private LaCaraListContract.View mView;
    private LaCaraListModle mModel;

    public LaCaraListPresenter(LaCaraListContract.View view, LaCaraListModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void laCaraListRefresh(int type) {
        mModel.paysetupLaCara(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<PaySetupModelLaCara>() {
                    @Override
                    public void onSuccess(PaySetupModelLaCara paySetupModelLaCara) {
                        mView.laCaraListRefreshSuccess(paySetupModelLaCara);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.laCaraListRefreshError();
                    }
                });
    }

    @Override
    public void laCaraListLoadMore(int type) {
        mModel.paysetupLaCara(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<PaySetupModelLaCara>() {
                    @Override
                    public void onSuccess(PaySetupModelLaCara paySetupModelLaCara) {
                        mView.laCaraListLoadMoreSuccess(paySetupModelLaCara);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.laCaraListLoadMoreError();
                    }
                });
    }

    @Override
    public void setDefaultPaylaCara(int type, long id, String financePwd, long time) {
        mView.showLoading();
        mModel.setDefaultPay(type,id,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.setDefaultPayLaCaraSuccess(baseBean);
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
                        mView.deteleLaCaraSuccess(baseBean);
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
