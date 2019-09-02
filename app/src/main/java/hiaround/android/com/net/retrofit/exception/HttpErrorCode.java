package hiaround.android.com.net.retrofit.exception;

import com.growalong.util.util.GALogger;
import hiaround.android.com.BuildConfig;

/**
 * HTTP API 错误代码
 */
public class HttpErrorCode {
    private static final String TAG = HttpErrorCode.class.getSimpleName();
    /**
     * 根据错误代码获取错误字符串
     *
     * @param error
     * @param errordesc 服务端下发的错误描述，可能有些接口没有下发，该参数作为旧版的兼容性
     * @return 返回对应描述的string
     */
    public static String getErrorMessage(int error, String errordesc) {
        GALogger.d(TAG,"error    "+error);
        String resString = errordesc;
        if (error == 1) {
            if(BuildConfig.DEBUG){
                resString = error+" : 需要先登录才能进行此操作";
            }else {
                resString = "需要先登录才能进行此操作";
            }
            return resString;
        }else if(error == 2){
            if(BuildConfig.DEBUG){
                resString = error+" : 资金密码未设置或输入错误";
            }else {
                resString = "资金密码未设置或输入错误";
            }
        }
//        else if(error == 3){
//            if(BuildConfig.DEBUG){
//                resString = error+" : 通讯频率过快";
//            }else {
//                resString = "通讯频率过快";
//            }
//        }
        else if(error == 4){
            if(BuildConfig.DEBUG){
                resString = error+" : 服务器繁忙";
            }else {
                resString = "服务器繁忙";
            }
        }else if(error == 10){
            if(BuildConfig.DEBUG){
                resString = error+" : 资金不足";
            }else {
                resString = "资金不足";
            }
        }else if(error == 100){
            if(BuildConfig.DEBUG){
                resString = error+" : 图片验证码错误";
            }else {
                resString = "图片验证码错误";
            }
        }else if(error == 101){
            if(BuildConfig.DEBUG){
                resString = error+" : 短信验证码错误";
            }else {
                resString = "短信验证码错误";
            }
        }else if(error == 102){
            if(BuildConfig.DEBUG){
                resString = error+" : 邀请码错误";
            }else {
                resString = "邀请码错误";
            }
        }else if(error == 103){
            if(BuildConfig.DEBUG){
                resString = error+" : 手机号码已经注册过";
            }else {
                resString = "手机号码已经注册过";
            }
        }else if(error == 104){
            if(BuildConfig.DEBUG){
                resString = error+" : 手机号码没有被注册过";
            }else {
                resString = "手机号码没有被注册过";
            }
        }else if(error == 105){
            if(BuildConfig.DEBUG){
                resString = error+" : 今日注册已达上限";
            }else {
                resString = "今日注册已达上限";
            }
        }else if(error == 120){
            if(BuildConfig.DEBUG){
                resString = error+" : 登录密码错误";
            }else {
                resString = "登录密码错误";
            }
        }else if(error == 121){
            if(BuildConfig.DEBUG){
                resString = error+" : 账户锁定，无法登录";
            }else {
                resString = "账户锁定，无法登录";
            }
        }else if(error == 122){
            if(BuildConfig.DEBUG){
                resString = error+" : 昵称重复";
            }else {
                resString = "昵称重复";
            }
        }else if(error == 130){
            if(BuildConfig.DEBUG){
                resString = error+" : 此收款方式已设置过,不能重复设置";
            }else {
                resString = "此收款方式已设置过,不能重复设置";
            }
        }else if(error == 132){
            if(BuildConfig.DEBUG){
                resString = error+" : 添加店员二维码失败";
            }else {
                resString = "添加店员二维码失败";
            }
        } else if(error == 140){
            if(BuildConfig.DEBUG){
                resString = error+" : 不能重复验证身份";
            }else {
                resString = "不能重复验证身份";
            }
        }else if(error == 141){
            if(BuildConfig.DEBUG){
                resString = error+" : 还有出售单,不能删除收款方式";
            }else {
                resString = "还有出售单,不能删除收款方式";
            }
        }else if(error == 160){
            if(BuildConfig.DEBUG){
                resString = error+" : 提币量低于最小提币数量";
            }else {
                resString = "提币量低于最小提币数量";
            }
        }else if(error == 161){
            if(BuildConfig.DEBUG){
                resString = error+" : 提币超出每日限额";
            }else {
                resString = "提币超出每日限额";
            }
        }else if(error == 162){
            if(BuildConfig.DEBUG){
                resString = error+" : 提币地址错误";
            }else {
                resString = "提币地址错误";
            }
        }else if(error == 180){
            if(BuildConfig.DEBUG){
                resString = error+" : 注册失败";
            }else {
                resString = "注册失败";
            }
        }else if(error == 181){
            if(BuildConfig.DEBUG){
                resString = error+" : 选择了没有填写信息的收款方式";
            }else {
                resString = "选择了没有填写信息的收款方式";
            }
        }else if(error == 182){
            if(BuildConfig.DEBUG){
                resString = error+" : 挂单数量已经不足";
            }else {
                resString = "挂单数量已经不足";
            }
        }else if(error == 183){
            if(BuildConfig.DEBUG){
                resString = error+" : 订单状态不支持此操作";
            }else {
                resString = "订单状态不支持此操作";
            }
        }else if(error == 184){
            if(BuildConfig.DEBUG){
                resString = error+" : 你的挂单数量已满";
            }else {
                resString = "你的挂单数量已满";
            }
        }else if(error == 185){
            if(BuildConfig.DEBUG){
                resString = error+" : 挂单USDT数量低于最低值";
            }else {
                resString = "挂单USDT数量低于最低值";
            }
        }else if(error == 186){
            if(BuildConfig.DEBUG){
                resString = error+" : 订单价格设置错误";
            }else {
                resString = "订单价格设置错误";
            }
        }else if(error == 187){
            if(BuildConfig.DEBUG){
                resString = error+" : 您今日挂单功能已暂停";
            }else {
                resString = "您今日挂单功能已暂停";
            }
        }else if(error == 188){
            if(BuildConfig.DEBUG){
                resString = error+" : 无法取消该挂单，当前挂单拥有进行中订单，或者申诉。如有其他问题请联系客服";
            }else {
                resString = "无法取消该挂单，当前挂单拥有进行中订单，或者申诉。如有其他问题请联系客服";
            }
        }else if(error == 189){
            if(BuildConfig.DEBUG){
                resString = error+" : 不可对自己的挂单进行购买和出售的操作。如有其他问题请联系客服";
            }else {
                resString = "不可对自己的挂单进行购买和出售的操作。如有其他问题请联系客服";
            }
        }else if(error == 200){
            if(BuildConfig.DEBUG){
                resString = error+" : 交易状态不支持此操作";
            }else {
                resString = "交易状态不支持此操作";
            }
        }else if(error == 201){
            if(BuildConfig.DEBUG){
                resString = error+" : 对方暂时无法接收这种数量的金额";
            }else {
                resString = "对方暂时无法接收这种数量的金额";
            }
        }else if(error == 202){
            if(BuildConfig.DEBUG){
                resString = error+" : 同时只能有一个买单和一个卖单";
            }else {
                resString = "同时只能有一个买单和一个卖单";
            }
        }else if(error == 203){
            if(BuildConfig.DEBUG){
                resString = error+" : 你不是交易双方任何一人，或者交易不是等待放币状态，或者交易付款后不足10分钟，如非以上原因请联系客服";
            }else {
                resString = "你不是交易双方任何一人，或者交易不是等待放币状态，或者交易付款后不足10分钟，如非以上原因请联系客服";
            }
        }else if(error == 500){
            if(BuildConfig.DEBUG){
                resString = error+" : "+resString;
            }else {
                resString = resString;
            }
        } else if(error == 900){
            if(BuildConfig.DEBUG){
                resString = error+" : 修改用户资金失败,没找到用户";
            }else {
                resString = "修改用户资金失败,没找到用户";
            }
        }else if(error == 901){
            if(BuildConfig.DEBUG){
                resString = error+" : 修改用户资金失败,资金不足";
            }else {
                resString = "修改用户资金失败,资金不足";
            }
        }else if(error == 1000){
            if(BuildConfig.DEBUG){
                resString = error+" :没找到操作的用户";
            }else {
                resString = "没找到操作的用户";
            }
        }else if(error == 1001){
            if(BuildConfig.DEBUG){
                resString = error+" : 刷单金额错误";
            }else {
                resString = "刷单金额错误";
            }
        }else if(error == 1002){
            if(BuildConfig.DEBUG){
                resString = error+" : 提币id错误";
            }else {
                resString = "提币id错误";
            }
        }else if(error == 1003){
            if(BuildConfig.DEBUG){
                resString = error+" : 没有可统计的提币";
            }else {
                resString = "没有可统计的提币";
            }
        }else if(error == 1004){
            if(BuildConfig.DEBUG){
                resString = error+" : 请先完成前一个统计";
            }else {
                resString = "请先完成前一个统计";
            }
        }else if(error == 1005){
            if(BuildConfig.DEBUG){
                resString = error+" : 没找到这个交易";
            }else {
                resString = "没找到这个交易";
            }
        }else if(error == 1006){
            if(BuildConfig.DEBUG){
                resString = error+" : 参数输入错误";
            }else {
                resString = "参数输入错误";
            }
        }else if(error == 1007){
            if(BuildConfig.DEBUG){
                resString = error+" : 交易广播错误";
            }else {
                resString = "交易广播错误";
            }
        }else if(error == 1008){
            if(BuildConfig.DEBUG){
                resString = error+" : 刷单配置错误";
            }else {
                resString = "刷单配置错误";
            }
        }else if(error == 1009){
            resString = "网络异常,请检查网络设置";
        } else {
            if(BuildConfig.DEBUG) {
                resString = "服务器繁忙";
            }
        }
        return resString;
    }
}
