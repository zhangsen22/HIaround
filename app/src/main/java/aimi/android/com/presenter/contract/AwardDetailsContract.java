package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.RewardLogResponse;

public interface AwardDetailsContract {
    interface Presenter extends IBasePresenter {
        //获取奖励记录
        void rewardLog();
    }
    interface View extends IBaseView<Presenter> {
        //获取奖励记录成功
        void rewardLogSuccess(RewardLogResponse rewardLogResponse);
    }
}
