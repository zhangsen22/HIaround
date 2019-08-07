package hiaround.android.com.modle;

import java.util.List;

public class LargeAmountResponse extends BaseBean{

    private List<LargeAmountItem> billInfo;

    public List<LargeAmountItem> getBillInfo() {
        return billInfo;
    }

    @Override
    public String toString() {
        return "LargeAmountResponse{" +
                "billInfo=" + billInfo +
                '}';
    }
}
