package aimi.android.com.modle;

public class RewardDetailItem {

    private long id;//: 20190402        //id
    private String date;//：“2019-04-02”    //日期
    private double value;//：23.32            //奖励数量

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RewardDetailItem{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", value=" + value +
                '}';
    }
}
