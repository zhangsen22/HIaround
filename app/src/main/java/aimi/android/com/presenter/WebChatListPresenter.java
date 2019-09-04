package aimi.android.com.presenter;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.PaySetupModelWebChat;
import aimi.android.com.modle.WeChatPayeeItemModelPayee;
import aimi.android.com.modle.WebChatEditModle;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.WebChatListContract;
import aimi.android.com.presenter.modle.WebChatListModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class WebChatListPresenter implements WebChatListContract.Presenter{

    private WebChatListContract.View mView;
    private WebChatListModle mModel;

    public WebChatListPresenter(WebChatListContract.View view, WebChatListModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void webChatListRefresh(int type) {
//        mView.showLoading();
        mModel.paysetupWebChat(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<PaySetupModelWebChat>() {
                    @Override
                    public void onSuccess(PaySetupModelWebChat paySetupModelWebChat) {
                        mView.webChatListRefreshSuccess(paySetupModelWebChat);
//                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.webChatListRefreshError();
//                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void webChatListLoadMore(int type) {
//        mView.showLoading();
        mModel.paysetupWebChat(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<PaySetupModelWebChat>() {
                    @Override
                    public void onSuccess(PaySetupModelWebChat paySetupModelWebChat) {
                        mView.webChatListLoadMoreSuccess(paySetupModelWebChat);
//                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.webChatListLoadMoreError();
//                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void setDefaultPayWebChat(int type, long id, String financePwd, long time) {
        mView.showLoading();
        mModel.setDefaultPay(type,id,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.setDefaultPayWebChatSuccess(baseBean);
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
                        mView.deteleWebChatSuccess(baseBean);
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
    public void reWechat(long id, WeChatPayeeItemModelPayee payee) {
        mView.showLoading();
        mModel.reWechat(id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<WebChatEditModle>() {
                    @Override
                    public void onSuccess(WebChatEditModle webChatEditModle) {
                        mView.reWechatSuccess(webChatEditModle,payee);
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
