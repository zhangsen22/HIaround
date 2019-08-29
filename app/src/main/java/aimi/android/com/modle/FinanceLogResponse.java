package aimi.android.com.modle;

import java.util.List;

public class FinanceLogResponse extends BaseBean{

    private List<FinanceLogItem> financeLog;

    public List<FinanceLogItem> getFinanceLog() {
        return financeLog;
    }

    @Override
    public String toString() {
        return "FinanceLogResponse{" +
                "financeLog=" + financeLog +
                '}';
    }
}
