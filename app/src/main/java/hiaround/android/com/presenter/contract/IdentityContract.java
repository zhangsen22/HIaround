package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.BaseBean;

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
