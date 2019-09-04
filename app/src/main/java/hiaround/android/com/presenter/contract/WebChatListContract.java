package hiaround.android.com.presenter.contract;

import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.modle.PaySetupModelWebChat;
import hiaround.android.com.modle.WeChatPayeeItemModelPayee;
import hiaround.android.com.modle.WebChatEditModle;

public interface WebChatListContract {

    interface Presenter extends IBasePresenter {
        void webChatListRefresh(int type);
        void webChatListLoadMore(int type);
        void setDefaultPayWebChat(int type, long id, String financePwd, long time);
        void detelePay(int type,long id,String financePwd,long time);
        //微信收款设置
        void reWechat(long id, WeChatPayeeItemModelPayee payee);
    }
    interface View extends IBaseView<Presenter> {
        void webChatListRefreshSuccess(PaySetupModelWebChat paySetupModelWebChat);
        void webChatListRefreshError();
        void webChatListLoadMoreSuccess(PaySetupModelWebChat paySetupModelWebChat);
        void webChatListLoadMoreError();
        void setDefaultPayWebChatSuccess(BaseBean baseBean);
        void deteleWebChatSuccess(BaseBean baseBean);
        //微信收款设置成功
        void reWechatSuccess(WebChatEditModle webChatEditModle, WeChatPayeeItemModelPayee payee);
    }
}

