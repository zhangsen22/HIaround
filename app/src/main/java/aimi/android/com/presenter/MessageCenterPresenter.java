package aimi.android.com.presenter;

import aimi.android.com.modle.MessageCenterResponse;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.MessageCenterContract;
import aimi.android.com.presenter.modle.MessageCenterModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MessageCenterPresenter implements MessageCenterContract.Presenter{

    private MessageCenterContract.View mView;

    private MessageCenterModle mModel;

    public MessageCenterPresenter(MessageCenterContract.View view, MessageCenterModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void msgCenterRefresh(long minId) {
//        mView.showLoading();
        mModel.msgCenter(minId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<MessageCenterResponse>() {
                    @Override
                    public void onSuccess(MessageCenterResponse messageCenterResponse) {
//                        mView.hideLoading();
                        mView.msgCenterRefreshSuccess(messageCenterResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
//                        mView.hideLoading();
                        mView.msgCenterRefreshError();
                    }
                });
    }

    @Override
    public void msgCenterLoadMore(long minId) {
        mModel.msgCenter(minId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<MessageCenterResponse>() {
                    @Override
                    public void onSuccess(MessageCenterResponse messageCenterResponse) {
                        mView.msgCenterLoadMoreSuccess(messageCenterResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.msgCenterLoadMoreError();
                    }
                });
    }

    @Override
    public void starLoadData() {

    }
}
