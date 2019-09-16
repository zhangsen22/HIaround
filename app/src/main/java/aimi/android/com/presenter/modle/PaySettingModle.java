package aimi.android.com.presenter.modle;

import aimi.android.com.modle.BaseBean;
import aimi.android.com.modle.LaCaraEditModle;
import aimi.android.com.modle.LaCaraWenChatListModle;
import aimi.android.com.modle.WebChatEditModle;
import aimi.android.com.modle.WechatLoginModle;
import aimi.android.com.modle.YnShanFuEditModle;
import aimi.android.com.net.retrofit.BaseRetrofitClient;
import aimi.android.com.net.retrofit.exception.ModelExceptionMap;
import aimi.android.com.net.retrofit.exception.ServerExceptionMap;
import aimi.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class PaySettingModle{

    /**
     * 银行卡收款设置
     * @param bankName
     * @param subName
     * @param name
     * @param account
     * @param dailyLimit
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<BaseBean> bank(long id,long wechatPaymentId, String bankName, String subName, String name, String account, double dailyLimit, String financePwd, long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .bank(id,wechatPaymentId,bankName,subName,name,account,dailyLimit,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }

    /**
     * 支付宝收款设置
     * @param name
     * @param account
     * @param base64Img
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<BaseBean> ali(long id,String name,String account,String base64Img,String financePwd,long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .ali(id,name,account,base64Img,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }

    /**
     * 微信收款设置
     * @param name
     * @param account
     * @param base64Img
     * @param empBase64Img
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<WebChatEditModle> wechat(long id, String name, String account, String base64Img, String empBase64Img, String financePwd, long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .wechat(id,name,account,base64Img,empBase64Img,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<WebChatEditModle>())
                .onErrorResumeNext(new ModelExceptionMap<WebChatEditModle>());
    }

    /**
     * 云闪付收款设置
     * @param name
     * @param account
     * @param base64Img
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<YnShanFuEditModle> cloud(long id, String name, String account, String base64Img, String financePwd, long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .cloud(id,name,account,base64Img,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<YnShanFuEditModle>())
                .onErrorResumeNext(new ModelExceptionMap<YnShanFuEditModle>());
    }

    /**
     * 拉卡拉收款设置
     * @param wechatPaymentId
     * @param account
     * @param base64Img
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<LaCaraEditModle> lakala(long id,long wechatPaymentId, String account, String base64Img, String financePwd, long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .lakala(id,wechatPaymentId,account,base64Img,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<LaCaraEditModle>())
                .onErrorResumeNext(new ModelExceptionMap<LaCaraEditModle>());
    }

    /**
     * 云闪付编辑二维码
     * @param id
     * @param base64Img
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<YnShanFuEditModle> cloudImgSetUp(long id, String base64Img, String financePwd, long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .cloudImgSetUp(id,base64Img,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<YnShanFuEditModle>())
                .onErrorResumeNext(new ModelExceptionMap<YnShanFuEditModle>());
    }

    /**
     * 拉卡拉编辑二维码
     * @param id
     * @param base64Img
     * @param financePwd
     * @param time
     * @return
     */
    public Observable<LaCaraEditModle> lakalaImgSetUp(long id,long wechatPaymentId, String base64Img, String financePwd, long time){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .lakalaImgSetUp(id,wechatPaymentId,base64Img,financePwd,time)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<LaCaraEditModle>())
                .onErrorResumeNext(new ModelExceptionMap<LaCaraEditModle>());
    }

    /**
     * 微信登录
     * @param paymentId
     * @return
     */
    public Observable<WechatLoginModle> wechatLogin(long paymentId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .wechatLogin(paymentId,System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<WechatLoginModle>())
                .onErrorResumeNext(new ModelExceptionMap<WechatLoginModle>());
    }

    /**
     * 云闪付登陆成功上传参数
     * @param paymentId
     * @return
     */
    public Observable<YnShanFuEditModle> cloudLogin(long paymentId, String cookieUser , String username){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .cloudLogin(paymentId,cookieUser,username)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<YnShanFuEditModle>())
                .onErrorResumeNext(new ModelExceptionMap<YnShanFuEditModle>());
    }

    /**
     * 拉卡拉下拉框获取微信列表
     */
    public Observable<LaCaraWenChatListModle> getWechatList(){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .getWechatList()
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<LaCaraWenChatListModle>())
                .onErrorResumeNext(new ModelExceptionMap<LaCaraWenChatListModle>());
    }
}
