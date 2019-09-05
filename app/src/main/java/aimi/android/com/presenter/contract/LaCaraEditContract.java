package aimi.android.com.presenter.contract;

import aimi.android.com.IBasePresenter;
import aimi.android.com.IBaseView;
import aimi.android.com.modle.LaCaraEditModle;
import aimi.android.com.modle.LaCaraWenChatListModle;

public interface LaCaraEditContract {

    interface Presenter extends IBasePresenter {
        //拉卡拉收款设置
        void laCara(long id, long wechatPaymentId, String account, String base64Img, String financePwd, long time);
        //拉卡拉编辑二维码
        void lakalaImgSetUp(long id,long wechatPaymentId, String base64Img, String financePwd, long time);
        //拉卡拉下拉框获取微信列表
        void getWechatList();
    }
    interface View extends IBaseView<Presenter> {
        //拉卡拉收款设置成功
        void laCaraSuccess(LaCaraEditModle laCaraEditModle);
        //拉卡拉编辑二维码成功
        void lakalaImgSetUpSuccess(LaCaraEditModle laCaraEditModle);
        //拉卡拉下拉框获取微信列表
        void getWechatListSuccess(LaCaraWenChatListModle laCaraWenChatListModle);
    }
}
