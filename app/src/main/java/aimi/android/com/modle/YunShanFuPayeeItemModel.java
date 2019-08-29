package aimi.android.com.modle;

import aimi.android.com.ui.adapter.poweradapter.AbsSelect;

public class YunShanFuPayeeItemModel extends AbsSelect {

    private YunShanFuPayeeItemModelPayee payee;
    private double leftMoney;
    private long leftTimes;

    public YunShanFuPayeeItemModelPayee getPayee() {
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
        return "YunShanFuPayeeItemModel{" +
                "payee=" + payee +
                ", leftMoney=" + leftMoney +
                ", leftTimes=" + leftTimes +
                '}';
    }
}
