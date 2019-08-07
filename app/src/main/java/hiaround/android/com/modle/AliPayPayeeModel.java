package hiaround.android.com.modle;

import java.util.List;

public class AliPayPayeeModel {
    private long defaultId;
    private List<AliPayPayeeItemModel> payee;

    public long getDefaultId() {
        return defaultId;
    }

    public List<AliPayPayeeItemModel> getPayee() {
        return payee;
    }

    @Override
    public String toString() {
        return "AliPayPayeeModel{" +
                "defaultId=" + defaultId +
                ", payee=" + payee +
                '}';
    }
}
