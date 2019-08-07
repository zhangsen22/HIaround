package hiaround.android.com.modle;

import hiaround.android.com.ui.adapter.poweradapter.AbsSelect;

public class BankPayeeItemModel extends AbsSelect {

    private BankPayeeItemModelPayee payee;
    private double leftMoney;
    private long leftTimes;

    public BankPayeeItemModelPayee getPayee() {
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
        return "BankPayeeItemModel{" +
                "payee=" + payee +
                ", leftMoney=" + leftMoney +
                ", leftTimes=" + leftTimes +
                '}';
    }
}
