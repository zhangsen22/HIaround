package aimi.android.com.modle;

public class PaySetupModelYunShanFu extends BaseBean{
    private YunShanFuPayeeModel cloudPayeeObj;//云闪付收款信息,没设置如下均为""

    public YunShanFuPayeeModel getCloudPayeeObj() {
        return cloudPayeeObj;
    }

    @Override
    public String toString() {
        return "PaySetupModelYunShanFu{" +
                "cloudPayeeObj=" + cloudPayeeObj +
                '}';
    }
}
