package hiaround.android.com.modle;

import java.util.List;

public class BuyAmountListResponse extends BaseBean {

    private List<String> wechatList;

    private List<String> cloudQuickPayList;

    public List<String> getWechatList() {
        return wechatList;
    }

    public List<String> getCloudQuickPayList() {
        return cloudQuickPayList;
    }

    @Override
    public String toString() {
        return "BuyAmountListResponse{" +
                "wechatList=" + wechatList +
                ", cloudQuickPayList=" + cloudQuickPayList +
                '}';
    }
}
