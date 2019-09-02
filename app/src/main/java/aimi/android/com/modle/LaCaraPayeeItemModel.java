package aimi.android.com.modle;

import aimi.android.com.ui.adapter.poweradapter.AbsSelect;

public class LaCaraPayeeItemModel extends AbsSelect {

    private LaCaraPayeeItemModelPayee payee;
    private double leftMoney;
    private long leftTimes;

    public LaCaraPayeeItemModelPayee getPayee() {
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
        return "LaCaraPayeeItemModel{" +
                "payee=" + payee +
                ", leftMoney=" + leftMoney +
                ", leftTimes=" + leftTimes +
                '}';
    }
}
