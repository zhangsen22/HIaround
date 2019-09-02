package aimi.android.com.modle;

import java.util.List;

public class BuyAmountListResponse extends BaseBean {
    private String unit;
    private List<String> wechatList;
    private List<String> cloudQuickPayList;
    private List<String> bankList;

    private List<String> lakalaList;

    public List<String> getWechatList() {
        return wechatList;
    }

    public List<String> getCloudQuickPayList() {
        return cloudQuickPayList;
    }

    public String getUnit() {
        return unit;
    }

    public List<String> getBankList() {
        return bankList;
    }

    public List<String> getLakalaList() {
        return lakalaList;
    }

    @Override
    public String toString() {
        return "BuyAmountListResponse{" +
                "unit='" + unit + '\'' +
                ", wechatList=" + wechatList +
                ", cloudQuickPayList=" + cloudQuickPayList +
                ", bankList=" + bankList +
                ", lakalaList=" + lakalaList +
                '}';
    }
}
