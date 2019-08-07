package hiaround.android.com.presenter;

import hiaround.android.com.modle.InvitationResponse;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.InvitationContract;
import hiaround.android.com.presenter.modle.InvitationModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class InvitationPresenter implements InvitationContract.Presenter{

    private InvitationContract.View mView;
    private InvitationModle mModel;

    public InvitationPresenter(InvitationContract.View view, InvitationModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void recommendReward(long upUserId) {
        mModel.recommendReward(upUserId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<InvitationResponse>() {
                    @Override
                    public void onSuccess(InvitationResponse invitationResponse) {
                        mView.recommendRewardSuccess(invitationResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.recommendRewardError();
                    }
                });
    }

    @Override
    public void starLoadData() {

    }
}
