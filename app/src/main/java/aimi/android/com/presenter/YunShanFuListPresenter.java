package aimi.android.com.presenter;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.PaySetupModelYunShanFu;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.YunShanFuListContract;
import aimi.android.com.presenter.modle.YunShanFuListModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class YunShanFuListPresenter implements YunShanFuListContract.Presenter{

    private YunShanFuListContract.View mView;
    private YunShanFuListModle mModel;

    public YunShanFuListPresenter(YunShanFuListContract.View view, YunShanFuListModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void yunShanFuListRefresh(int type) {
        mModel.paysetupYunShanFu(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<PaySetupModelYunShanFu>() {
                    @Override
                    public void onSuccess(PaySetupModelYunShanFu paySetupModelYunShanFu) {
                        mView.yunShanFuListRefreshSuccess(paySetupModelYunShanFu);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.yunShanFuListRefreshError();
                    }
                });
    }

    @Override
    public void yunShanFuListLoadMore(int type) {
        mModel.paysetupYunShanFu(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<PaySetupModelYunShanFu>() {
                    @Override
                    public void onSuccess(PaySetupModelYunShanFu paySetupModelYunShanFu) {
                        mView.yunShanFuListLoadMoreSuccess(paySetupModelYunShanFu);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.yunShanFuListLoadMoreError();
                    }
                });
    }

    @Override
    public void setDefaultPayyunShanFu(int type, long id, String financePwd, long time) {
        mView.showLoading();
        mModel.setDefaultPay(type,id,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.setDefaultPayYunShanFuSuccess(baseBean);
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
                        mView.deteleYunShanFuSuccess(baseBean);
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
