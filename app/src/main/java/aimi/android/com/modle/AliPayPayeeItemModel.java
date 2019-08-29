package aimi.android.com.modle;

import aimi.android.com.ui.adapter.poweradapter.AbsSelect;

public class AliPayPayeeItemModel extends AbsSelect {

    private AliPayPayeeItemModelPayee payee;
    private double leftMoney;
    private long leftTimes;

    public AliPayPayeeItemModelPayee getPayee() {
        return payee;
    }

    public double getLeftMoney() {
        return leftMoney;
    }

    public long getLeftTimes() {
        return leftTimes;
    }

    @Override
    public String toString() {
        return "AliPayPayeeItemModel{" +
                "payee=" + payee +
                ", leftMoney=" + leftMoney +
                ", leftTimes=" + leftTimes +
                '}';
    }
}
