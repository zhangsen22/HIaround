package aimi.android.com.net.retrofit;

/**
 * Created by yangxing on 2018/10/31.
 */
public class ApiConstants {
    //app访问的初始地址
//    public static String getGetDomainNameBase = "http://54.254.190.23:8330/";//亚马逊测试环境
            public static String getGetDomainNameBase = "https://a.rr5555.info/";//Aimi线上环境
    public static final String getDomainName = "app/config";
    public static final String getImageCode = "user/imageCode";//获取图片验证码
    public static final String senSmsCode = "user/smsCode";//发送验证码
    public static final String regist = "user/regist";//注册
    public static final String forgetPwd = "user/forgetPwd";//忘记密码
    public static final String login = "user/login";//登录
    public static final String changeNickname = "user/changeNickname";//设置昵称
    public static final String changePwd = "user/changePwd";//修改密码
    public static final String getSellinfo = "trade/bill/getSellinfo";//获取挂取的卖单信息
    public static final String getHugeBillinfo = "api/bill/getHugeBillinfo";//获取大额提现订单
    public static final String getBuyinfo = "trade/bill/getBuyinfo";//获取挂取的买单信息
    public static final String usdtPrice = "trade/usdtPrice";//查看USDT最新价格
    public static final String getInfo = "balance/getInfo";//资产查询
    public static final String putUpSell = "trade/bill/putUpSell";//委托出售
    public static final String putUpBuy = "trade/bill/putUpBuy";//委托购买
    public static final String changeFinancePwd = "user/changeFinancePwd";//修改资金密码
    public static final String financeLog = "balance/financeLog";//获取财务记录
    public static final String rewardLog = "balance/rewardLog";//获取奖励记录
    public static final String transfer = "balance/transfer";//资金划转
    public static final String rewardDetail = "balance/rewardDetail";//奖励详单查询
    public static final String mySellinfo = "trade/mySellinfo";//获取我的卖出交易信息
    public static final String myBuyinfo = "trade/myBuyinfo";//获取我的买入交易信息
    public static final String myBillInfo = "trade/myBillInfo";//获取我的委托单信息
    public static final String cancel = "trade/bill/cancel";//撤销委托
    public static final String appeal = "trade/appeal";//订单申诉
    public static final String ordercancel = "trade/cancel";//取消订单
    public static final String idCheck = "user/IDCheck";//身份认证
    public static final String msgCenter = "user/msgCenter";//获取消息中心消息
    public static final String buy = "trade/buy";//购买
    public static final String manualPay = "trade/manualPay";//玩家点击我已付款
    public static final String sell = "trade/sell";//出售
    public static final String fb_transfer = "trade/transfer";//玩家点击放币
    public static final String withdraw = "balance/withdraw";//提币
    public static final String paysetup = "user/paysetup/getinfo";//获取自己的收款信息
    public static final String bank = "user/paysetup/bank";//银行卡收款设置
    public static final String ali = "user/paysetup/ali";//支付宝收款设置
    public static final String wechat = "user/paysetup/wechat";//微信收款设置
    public static final String detelePay = "user/paysetup/delete";//删除收款设置
    public static final String setDefaultPay = "user/paysetup/setDefault";//设置默认收款设置
    public static final String DOWNLOADAPK = "Aimi.apk";//设置默认收款设置
    public static final String wechatLogin = "user/wechatLogin";//微信登录
    public static final String recommendReward = "trade/recommendReward";//推荐奖励
    public static final String buyAmountList = "trade/buyAmountList";//交易 我要买  获取购买金额列表
    public static final String quickBuy = "trade/quickBuy";//一键购买
    public static final String quickSell = "trade/quickSell";//一键出售
    public static final String sellLimit = "trade/sellLimit";//一键出售区间值
    public static final String cloud = "user/paysetup/cloud";//云闪付收款设置
    public static final String cloudLogin = "user/cloudLogin";//云闪付登陆成功上传参数
    public static final String cloudImgSetUp = "user/paysetup/cloudImgSetUp";//云闪付编辑二维码
}
