package aimi.android.com.modle;

public class WeChatPayeeItemModel{

    private WeChatPayeeItemModelPayee payee;
    private double leftMoney;
    private int leftTimes;

    public WeChatPayeeItemModelPayee getPayee() {
        return payee;
    }

    public double getLeftMoney() {
        return leftMoney;
    }

    public int getLeftTimes() {
        return leftTimes;
    }

    @Override
    public String toString() {
        return "WeChatPayeeItemModel{" +
                "payee=" + payee +
                ", leftMoney=" + leftMoney +
                ", leftTimes=" + leftTimes +
                '}';
    }
}
