package hiaround.android.com.modle;

import java.util.List;

public class BankPayeeModel {

    private long defaultId;
    private List<BankPayeeItemModel> payee;

    public long getDefaultId() {
        return defaultId;
    }

    public List<BankPayeeItemModel> getPayee() {
        return payee;
    }

    @Override
    public String toString() {
        return "BankPayeeModel{" +
                "defaultId=" + defaultId +
                ", payee=" + payee +
                '}';
    }
}
