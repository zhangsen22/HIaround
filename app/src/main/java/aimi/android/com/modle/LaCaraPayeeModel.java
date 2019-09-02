package aimi.android.com.modle;

import java.util.List;

public class LaCaraPayeeModel {
    private long defaultId;
    private List<LaCaraPayeeItemModel> payee;

    public long getDefaultId() {
        return defaultId;
    }

    public List<LaCaraPayeeItemModel> getPayee() {
        return payee;
    }

    @Override
    public String toString() {
        return "LaCaraPayeeModel{" +
                "defaultId=" + defaultId +
                ", payee=" + payee +
                '}';
    }
}
