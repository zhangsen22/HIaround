package hiaround.android.com.modle;

public class PaySetupModelWebChat extends BaseBean{
    private WeChatPayeeModel wechatPayeeObj;//微信收款信息,没设置如下均为""

    public WeChatPayeeModel getWechatPayee() {
        return wechatPayeeObj;
    }

    @Override
    public String toString() {
        return "PaySetupModelWebChat{" +
                "wechatPayeeObj=" + wechatPayeeObj +
                '}';
    }
}
