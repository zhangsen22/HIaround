package hiaround.android.com.net.retrofit.service;

import hiaround.android.com.app.AccountInfo;
import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.modle.BuyAmountListResponse;
import hiaround.android.com.modle.BuyBusinessResponse;
import hiaround.android.com.modle.BuyResponse;
import hiaround.android.com.modle.DomainModel;
import hiaround.android.com.modle.FinanceLogResponse;
import hiaround.android.com.modle.ImageCodeResponse;
import hiaround.android.com.modle.InvitationResponse;
import hiaround.android.com.modle.LargeAmountResponse;
import hiaround.android.com.modle.MessageCenterResponse;
import hiaround.android.com.modle.MyEntrustinfoResponse;
import hiaround.android.com.modle.MySellOrBuyinfoResponse;
import hiaround.android.com.modle.PaySetupModelAliPay;
import hiaround.android.com.modle.PaySetupModelBank;
import hiaround.android.com.modle.PaySetupModelWebChat;
import hiaround.android.com.modle.RegistResponse;
import hiaround.android.com.modle.RewardDetailResponse;
import hiaround.android.com.modle.RewardLogResponse;
import hiaround.android.com.modle.SellResponse;
import hiaround.android.com.modle.SmsCodeResponse;
import hiaround.android.com.modle.UsdtPriceResponse;
import hiaround.android.com.modle.WalletResponse;
import hiaround.android.com.modle.WebChatEditModle;
import hiaround.android.com.modle.WechatLoginModle;
import hiaround.android.com.modle.SellLimitResponse;
import hiaround.android.com.net.retrofit.ApiConstants;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

/**
 * @ProjectName: videochat
 * @Package: com.growalong.android.net.retrofit.service
 * @ClassName: ApiServices
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2019/4/26 12:03
 * @Version: ${VERSION_NAME}
 */
public interface ApiServices {

    /**
     * 获取多域名接口
     * @return
     */
    @POST(ApiConstants.getDomainName)
    Observable<DomainModel> getDomainName();

    /**
     * 获取图片验证码
     * @param phoneNum
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.getImageCode)
    Observable<ImageCodeResponse> getImageCode(@Field("phoneNum") String phoneNum);

    /**
     * 发送验证码
     * @param phoneNum
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.senSmsCode)
    Observable<SmsCodeResponse> senSenSmsCode(@Field("phoneNum") String phoneNum);

    /**
     * 注册
     * @param phoneNum
     * @param invitedCode
     * @param imageCode
     * @param smsCode
     * @param pwd
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.regist)
    Observable<RegistResponse> regist(@Field("phoneNum") String phoneNum
                                        ,@Field("invitedCode") String invitedCode
                                        ,@Field("imageCode") String imageCode
                                        ,@Field("smsCode") String smsCode
                                        ,@Field("pwd") String pwd);


    /**
     *忘记密码
     * @param phoneNum
     * @param imageCode
     * @param smsCode
     * @param pwd
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.forgetPwd)
    Observable<BaseBean> forgetPwd(@Field("pwd") String pwd,@Field("phoneNum") String phoneNum
            , @Field("imageCode") String imageCode
            , @Field("smsCode") String smsCode);

    /**
     * 登录
     * @param phoneNum
     * @param time
     * @param pwd
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.login)
    Observable<AccountInfo> login(@Field("phoneNum") String phoneNum
            , @Field("pwd") String pwd
            , @Field("time") long time);

    /**
     * 设置昵称
     * @param nickname
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.changeNickname)
    Observable<BaseBean> changeNickname(@Field("nickname") String nickname);

    /**
     * 修改密码
     * @param pwd
     * @param smsCode
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.changePwd)
    Observable<BaseBean> changePwd(@Field("pwd") String pwd
                                    ,@Field("smsCode") String smsCode);

    /**
     * 获取挂取的卖单信息
     * @param minId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.getSellinfo)
    Observable<BuyResponse> getSellinfo(@Field("minId") long minId);

    /**
     * 交易 我要买  获取购买金额列表
     * @return
     */
    @POST(ApiConstants.buyAmountList)
    Observable<BuyAmountListResponse> buyAmountList();

    /**
     * 获取挂取的买单信息
     * @param minId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.getBuyinfo)
    Observable<BuyResponse> getBuyinfo(@Field("minId") long minId);

    /**
     * 获取大额提现订单
     * @param minId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.getHugeBillinfo)
    Observable<LargeAmountResponse> getHugeBillinfo(@Field("minId") long minId);

    /**
     * 查看USDT最新价格
     * @return
     */
    @POST(ApiConstants.usdtPrice)
    Observable<UsdtPriceResponse> usdtPrice();

    /**
     * 资产查询
     * @return
     */
    @POST(ApiConstants.getInfo)
    Observable<WalletResponse> getInfo();

    /**
     * 委托出售
     * @param price
     * @param minNum
     * @param maxNum
     * @param supporAli
     * @param supportWechat
     * @param supportBank
     * @param financePwd
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.putUpSell)
    Observable<BaseBean> putUpSell(@Field("price") double price
            , @Field("minNum") double minNum
            , @Field("maxNum") double maxNum
            , @Field("supporAli") boolean supporAli
            , @Field("supportWechat") boolean supportWechat
            , @Field("supportBank") boolean supportBank
            , @Field("financePwd") String financePwd
            , @Field("time") long time);

    /**
     * 委托购买
     * @param price
     * @param minNum
     * @param maxNum
     * @param financePwd
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.putUpBuy)
    Observable<BaseBean> putUpBuy(@Field("price") double price
            , @Field("minNum") double minNum
            , @Field("maxNum") double maxNum
            , @Field("financePwd") String financePwd
            , @Field("time") long time);

    /**
     * 修改资金密码
     * @param financePwd
     * @param smsCode
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.changeFinancePwd)
    Observable<BaseBean> changeFinancePwd(@Field("financePwd") String financePwd, @Field("smsCode") String smsCode);

    /**
     * 获取财务记录
     * @param minId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.financeLog)
    Observable<FinanceLogResponse> financeLog(@Field("minId") long minId);

    /**
     * 获取奖励记录
     * @return
     */
    @POST(ApiConstants.rewardLog)
    Observable<RewardLogResponse> rewardLog();

    /**
     * 资金划转
     * @param type
     * @param num
     * @param financePwd
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.transfer)
    Observable<WalletResponse> transfer(@Field("type") int type
                                            ,@Field("num") double num
                                            ,@Field("financePwd") String financePwd
                                            ,@Field("time") long time);

    /**
     * 奖励详单查询
     * @param type
     * @param minId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.rewardDetail)
    Observable<RewardDetailResponse> rewardDetail(@Field("type") int type, @Field("minId") long minId);

    /**
     * 获取我的卖出交易信息
     * @param type
     * @param minId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.mySellinfo)
    Observable<MySellOrBuyinfoResponse> mySellinfo(@Field("type") int type, @Field("minId") long minId);

    /**
     * 获取我的买入交易信息
     * @param type
     * @param minId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.myBuyinfo)
    Observable<MySellOrBuyinfoResponse> myBuyinfo(@Field("type") int type, @Field("minId") long minId);

    /**
     * 获取我的委托单信息
     * @param type
     * @param minId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.myBillInfo)
    Observable<MyEntrustinfoResponse> myBillInfo(@Field("type") int type, @Field("minId") long minId);

    /**
     * 撤销委托
     * @param billId
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.cancel)
    Observable<BaseBean> cancel(@Field("billId") long billId, @Field("type") int type);

    /**
     * 订单申诉
     * @param tradeId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.appeal)
    Observable<BaseBean> appeal(@Field("tradeId") String tradeId);

    /**
     * 取消订单
     * @param tradeId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.ordercancel)
    Observable<BaseBean> ordercancel(@Field("tradeId") String tradeId);

    /**
     * 身份认证
     * @param type
     * @param name
     * @param sex
     * @param birthday
     * @param IDNumber
     * @param IDImageFront
     * @param IDImageBehind
     * @param IDImageWithUser
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.idCheck)
    Observable<BaseBean> idCheck(@Field("type") int type
                                  ,@Field("name") String name
            ,@Field("sex") int sex
            ,@Field("birthday") String birthday
            ,@Field("IDNumber") String IDNumber
            ,@Field("IDImageFront") String IDImageFront
            ,@Field("IDImageBehind") String IDImageBehind
            ,@Field("IDImageWithUser") String IDImageWithUser);

    /**
     * 获取消息中心消息
     * @param minId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.msgCenter)
    Observable<MessageCenterResponse> msgCenter(@Field("minId") long minId);

    /**
     * 购买
     * @param billId
     * @param num
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.buy)
    Observable<BuyBusinessResponse> buy(@Field("billId") long billId, @Field("num") double num, @Field("type") int type);

    /**
     * 一键购买
     * @param amount
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.quickBuy)
    Observable<BuyBusinessResponse> quickBuy(@Field("amount") String amount, @Field("type") int type);

    /**
     * 玩家点击我已付款
     * @param tradeId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.manualPay)
    Observable<BaseBean> manualPay(@Field("tradeId") String tradeId);

    /**
     * 出售
     * @param billId
     * @param num
     * @param type
     * @param financePwd
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.sell)
    Observable<SellResponse> sell(@Field("billId") long billId, @Field("num") double num, @Field("type") int type
            , @Field("financePwd") String financePwd, @Field("time") long time);

    /**
     * 一键出售
     * @param type
     * @param financePwd
     * @param rmb
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.quickSell)
    Observable<BaseBean> quickSell( @Field("type") int type
            , @Field("financePwd") String financePwd, @Field("rmb") double rmb,@Field("time") long time);

    /**
     * 玩家点击放币
     * @param tradeId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.fb_transfer)
    Observable<BaseBean> fb_transfer(@Field("tradeId") String tradeId);

    /**
     * 提币
     * @param addr
     * @param num
     * @param financePwd
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.withdraw)
    Observable<BaseBean> withdraw(@Field("addr") String addr
                                ,@Field("num") double num
                                ,@Field("financePwd") String financePwd
                                ,@Field("time") long time);

    /**
     * 获取自己的收款信息
     * 银行卡
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.paysetup)
    Observable<PaySetupModelBank> paysetupBank(@Field("type") int type);

    /**
     * 获取自己的收款信息
     * 支付宝
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.paysetup)
    Observable<PaySetupModelAliPay> paysetupAliPay(@Field("type") int type);

    /**
     * 获取自己的收款信息
     * 微信
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.paysetup)
    Observable<PaySetupModelWebChat> paysetupWebChat(@Field("type") int type);

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
    @FormUrlEncoded
    @POST(ApiConstants.bank)
    Observable<BaseBean> bank(@Field("id") long id
            ,@Field("bankName") String bankName
            ,@Field("subName") String subName
            ,@Field("name") String name
            ,@Field("account") String account
            ,@Field("dailyLimit") double dailyLimit
            ,@Field("financePwd") String financePwd
            ,@Field("time") long time);

    /**
     * 支付宝收款设置
     * @param name
     * @param account
     * @param base64Img
     * @param financePwd
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.ali)
    Observable<BaseBean> ali(@Field("id") long id
            ,@Field("name") String name
            ,@Field("account") String account
            ,@Field("base64Img") String base64Img
            ,@Field("financePwd") String financePwd
            ,@Field("time") long time);

    /**
     * 微信收款设置
     * @param name
     * @param account
     * @param base64Img
     * @param financePwd
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.wechat)
    Observable<WebChatEditModle> wechat(@Field("id") long id
            , @Field("name") String name
            , @Field("account") String account
            , @Field("base64Img") String base64Img
            , @Field("empBase64Img") String empBase64Img
            , @Field("financePwd") String financePwd
            , @Field("time") long time);

    /**
     * 删除收款设置
     * @param type
     * @param id
     * @param financePwd
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.detelePay)
    Observable<BaseBean> detelePay(@Field("type") int type
                                ,@Field("id") long id
                                ,@Field("financePwd") String financePwd
                                ,@Field("time") long time);

    /**
     * 设置默认收款设置
     * @param type
     * @param id
     * @param financePwd
     * @param time
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.setDefaultPay)
    Observable<BaseBean> setDefaultPay(@Field("type") int type
            ,@Field("id") long id
            ,@Field("financePwd") String financePwd
            ,@Field("time") long time);

    /**
     * 下载apk
     * @return
     */
    @Streaming
    @GET(ApiConstants.DOWNLOADAPK)
    Observable<ResponseBody> download();

    /**
     * 微信登录
     * @param paymentId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.wechatLogin)
    Observable<WechatLoginModle> wechatLogin(@Field("paymentId") long paymentId, @Field("time") long time);

    /**
     * 推荐奖励
     * @param upUserId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConstants.recommendReward)
    Observable<InvitationResponse> recommendReward(@Field("upUserId") long upUserId);

    /**
     * 一键出售区间值
     * @return
     */
    @POST(ApiConstants.sellLimit)
    Observable<SellLimitResponse> sellLimit();
}
