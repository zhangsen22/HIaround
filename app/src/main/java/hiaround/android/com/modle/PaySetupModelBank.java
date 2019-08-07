package hiaround.android.com.modle;

public class PaySetupModelBank extends BaseBean{
    private BankPayeeModel bankPayeeObj;//支付宝收款信息,没设置如下均为""

    public BankPayeeModel getBankPayeeObj() {
        return bankPayeeObj;
    }

    @Override
    public String toString() {
        return "PaySetupModelBank{" +
                "bankPayeeObj=" + bankPayeeObj +
                '}';
    }
}
