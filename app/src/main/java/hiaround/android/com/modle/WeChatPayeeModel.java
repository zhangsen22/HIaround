package hiaround.android.com.modle;

import java.util.List;

public class WeChatPayeeModel {
    private long defaultId;
    private List<WeChatPayeeItemModel> payee;

    public long getDefaultId() {
        return defaultId;
    }

    public List<WeChatPayeeItemModel> getPayee() {
        return payee;
    }

    @Override
    public String toString() {
        return "WeChatPayeeModel{" +
                "defaultId=" + defaultId +
                ", payee=" + payee +
                '}';
    }
}
