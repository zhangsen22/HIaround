package aimi.android.com.modle;

public class PaySetupModelLaCara extends BaseBean{
    private LaCaraPayeeModel lakalaPayeeObj  ;//拉卡拉收款信息,没设置如下均为""

    public LaCaraPayeeModel getLakalaPayeeObj() {
        return lakalaPayeeObj;
    }

    @Override
    public String toString() {
        return "PaySetupModelLaCara{" +
                "lakalaPayeeObj=" + lakalaPayeeObj +
                '}';
    }
}
