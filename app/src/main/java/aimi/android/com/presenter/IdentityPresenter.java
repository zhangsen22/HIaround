package aimi.android.com.presenter;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.net.retrofit.ModelResultObserver;
import aimi.android.com.net.retrofit.exception.ModelException;
import aimi.android.com.presenter.contract.IdentityContract;
import aimi.android.com.presenter.modle.IdentityModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class IdentityPresenter implements IdentityContract.Presenter{

    private IdentityContract.View mView;
    private IdentityModle mModel;

    public IdentityPresenter(IdentityContract.View view, IdentityModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void idCheck(int type, String name, int sex, String birthday, String IDNumber, String IDImageFront, String IDImageBehind, String IDImageWithUser) {
        mView.showLoading();
        mModel.idCheck(type,name,sex,birthday,IDNumber,IDImageFront,IDImageBehind,IDImageWithUser).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.idCheckSuccess(baseBean);
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
