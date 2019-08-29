package aimi.android.com.modle;

public class PaySetupModelAliPay extends BaseBean{
    private AliPayPayeeModel aliPayeeObj;//支付宝收款信息,没设置如下均为""

    public AliPayPayeeModel getAliPayeeObj() {
        return aliPayeeObj;
    }

    @Override
    public String toString() {
        return "PaySetupModelAliPay{" +
                "aliPayeeObj=" + aliPayeeObj +
                '}';
    }
}
