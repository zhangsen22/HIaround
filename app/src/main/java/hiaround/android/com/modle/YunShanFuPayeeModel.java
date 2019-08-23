package hiaround.android.com.modle;

import java.util.List;

public class YunShanFuPayeeModel {
    private long defaultId;
    private List<YunShanFuPayeeItemModel> payee;

    public long getDefaultId() {
        return defaultId;
    }

    public List<YunShanFuPayeeItemModel> getPayee() {
        return payee;
    }

    @Override
    public String toString() {
        return "YunShanFuPayeeModel{" +
                "defaultId=" + defaultId +
                ", payee=" + payee +
                '}';
    }
}
