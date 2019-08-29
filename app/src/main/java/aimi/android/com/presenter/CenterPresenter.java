package aimi.android.com.presenter;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.CenterContract;
import aimi.android.com.presenter.modle.CenterModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CenterPresenter implements CenterContract.Presenter{

    private CenterContract.View mView;
    private CenterModle mModel;

    public CenterPresenter(CenterContract.View view, CenterModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void changeNickname(final String nickname) {
        mView.showLoading();
        mModel.changeNickname(nickname).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.changeNicknameSuccess(nickname);
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
