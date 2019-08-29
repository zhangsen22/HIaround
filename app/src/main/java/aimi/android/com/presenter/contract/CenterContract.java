package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;

public interface CenterContract {
    interface Presenter extends IBasePresenter {
        //设置昵称
        void changeNickname(String nickname);
    }
    interface View extends IBaseView<Presenter> {
        //设置昵称  成功
        void changeNicknameSuccess(String nickname);
    }
}
