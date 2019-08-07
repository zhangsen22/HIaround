package hiaround.android.com.modle;

import java.util.List;

public class BuyResponse extends BaseBean{
    private List<BuyItem> billInfo;

    public List<BuyItem> getBillInfo() {
        return billInfo;
    }

    @Override
    public String toString() {
        return "BuyResponse{" +
                "billInfo=" + billInfo +
                '}';
    }
}
