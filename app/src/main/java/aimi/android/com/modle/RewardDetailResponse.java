package aimi.android.com.modle;

import java.util.List;

public class RewardDetailResponse extends BaseBean{
    private List<RewardDetailItem> details;

    public List<RewardDetailItem> getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "RewardDetailResponse{" +
                "details=" + details +
                '}';
    }
}
