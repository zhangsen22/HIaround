package hiaround.android.com.presenter.contract;


import hiaround.android.com.IBasePresenter;
import hiaround.android.com.IBaseView;
import hiaround.android.com.modle.YnShanFuEditModle;

public interface YunShanFuEditContract {

    interface Presenter extends IBasePresenter {
        //云闪付收款设置
        void yunshanfu(long id, String name, String account, String base64Img, String financePwd, long time);
        //云闪付编辑二维码
        void cloudImgSetUp(long id, String base64Img, String financePwd, long time);
    }
    interface View extends IBaseView<Presenter> {
        //微信收款设置成功
        void yunShanFuSuccess(YnShanFuEditModle ynShanFuEditModle);
        //云闪付编辑二维码成功
        void cloudImgSetUpSuccess(YnShanFuEditModle ynShanFuEditModle);
    }
}
