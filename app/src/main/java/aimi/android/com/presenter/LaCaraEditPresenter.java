package aimi.android.com.presenter;

import aimi.android.com.modle.LaCaraEditModle;
import aimi.android.com.modle.LaCaraWenChatListModle;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.LaCaraEditContract;
import aimi.android.com.presenter.modle.PaySettingModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LaCaraEditPresenter implements LaCaraEditContract.Presenter{

    private LaCaraEditContract.View mView;
    private PaySettingModle mModel;

    public LaCaraEditPresenter(LaCaraEditContract.View view, PaySettingModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void laCara(long id, long wechatPaymentId, String account, String base64Img, String financePwd, long time) {
        mView.showLoading();
        mModel.lakala(id,wechatPaymentId,account,base64Img,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<LaCaraEditModle>() {
                    @Override
                    public void onSuccess(LaCaraEditModle laCaraEditModle) {
                        mView.laCaraSuccess(laCaraEditModle);
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
    public void lakalaImgSetUp(long id,long wechatPaymentId, String base64Img, String financePwd, long time) {
        mView.showLoading();
        mModel.lakalaImgSetUp(id,wechatPaymentId,base64Img,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<LaCaraEditModle>() {
                    @Override
                    public void onSuccess(LaCaraEditModle laCaraEditModle) {
                        mView.lakalaImgSetUpSuccess(laCaraEditModle);
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
    public void getWechatList() {
        mModel.getWechatList().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<LaCaraWenChatListModle>() {
                    @Override
                    public void onSuccess(LaCaraWenChatListModle laCaraWenChatListModle) {
                        mView.getWechatListSuccess(laCaraWenChatListModle);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                    }
                });
    }

    @Override
    public void starLoadData() {

    }

}
