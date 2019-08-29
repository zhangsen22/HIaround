package aimi.android.com.modle;

import java.util.List;

public class MyEntrustinfoResponse extends BaseBean{
    private List<MyEntrustinfoItem> billInfo;

    public List<MyEntrustinfoItem> getBillInfo() {
        return billInfo;
    }

    @Override
    public String toString() {
        return "MyEntrustinfoResponse{" +
                "billInfo=" + billInfo +
                '}';
    }
}
