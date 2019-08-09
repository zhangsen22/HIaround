package hiaround.android.com.modle;

import java.util.List;

public class BuyAmountListResponse extends BaseBean {
    private String unit;
    private List<String> wechatList;
    private List<String> cloudQuickPayList;

    public List<String> getWechatList() {
        return wechatList;
    }

    public List<String> getCloudQuickPayList() {
        return cloudQuickPayList;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "BuyAmountListResponse{" +
                "unit='" + unit + '\'' +
                ", wechatList=" + wechatList +
                ", cloudQuickPayList=" + cloudQuickPayList +
                '}';
    }
}
