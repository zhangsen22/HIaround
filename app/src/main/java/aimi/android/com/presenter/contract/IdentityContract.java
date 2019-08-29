package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.BaseBean;

public interface IdentityContract {

    interface Presenter extends IBasePresenter {
        //身份认证
        void idCheck(int type,String name,int sex,String birthday,String IDNumber,String IDImageFront,String IDImageBehind,String IDImageWithUser);
    }
    interface View extends IBaseView<Presenter> {
        //身份认证  成功
        void idCheckSuccess(BaseBean baseBean);
    }
}
