package hiaround.android.com.presenter;

import hiaround.android.com.modle.RewardDetailResponse;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.RewardDetailContract;
import hiaround.android.com.presenter.modle.RewardDetailModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RewardDetailPresenter implements RewardDetailContract.Presenter{

    private RewardDetailContract.View mView;

    private RewardDetailModle mModel;

    public RewardDetailPresenter(RewardDetailContract.View view, RewardDetailModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void rewardDetailRefresh(int type, long minId) {
//        mView.showLoading();
        mModel.rewardDetail(type,minId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<RewardDetailResponse>() {
                    @Override
                    public void onSuccess(RewardDetailResponse rewardDetailResponse) {
//                        mView.hideLoading();
                        mView.rewardDetailRefreshSuccess(rewardDetailResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
//                        mView.hideLoading();
                        mView.rewardDetailRefreshError();
                    }
                });
    }

    @Override
    public void rewardDetailLoadMore(int type, long minId) {
        mModel.rewardDetail(type,minId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<RewardDetailResponse>() {
                    @Override
                    public void onSuccess(RewardDetailResponse rewardDetailResponse) {
                        mView.rewardDetailLoadMoreSuccess(rewardDetailResponse);
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.rewardDetailLoadMoreError();
                    }
                });
    }

    @Override
    public void starLoadData() {

    }
}
