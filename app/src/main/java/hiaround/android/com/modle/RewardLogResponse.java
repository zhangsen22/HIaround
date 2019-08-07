package hiaround.android.com.modle;

import android.os.Parcel;
import android.os.Parcelable;

public class RewardLogResponse extends BaseBean implements Parcelable {
    private int firstTG;//:100        //一级推广人数
    private int secondTG;//:200    //二级推广人数
    private int firstAgentTG;//:300    //一级代理总数,普通用户此项为0
    private int secondAgentTG;//:500    //二级代理总数,普通用户此项为0
    private int generalTG;//:800  //总代推广人数,非总代此项为0
    private double totTradeReward;//:1000 //总交易奖励数量
    private double lastTradeReward;//:1000 //最后一天的交易奖励数量
    private double totTGReward;//:1000    //总推广奖励数量
    private double lastTGReward;//:100 //最后一天的推广奖励数量
    private double totAgentReward;//:10000 //总代理奖励数量
    private double lastAgentReward;//:1000 //最后一天的代理奖励数量
    private double totBillReward;//:1000    //总挂单奖励数量
    private double lastBillReward;//:1000    //最后一次挂单奖励数量

    protected RewardLogResponse(Parcel in) {
        firstTG = in.readInt();
        secondTG = in.readInt();
        firstAgentTG = in.readInt();
        secondAgentTG = in.readInt();
        generalTG = in.readInt();
        totTradeReward = in.readDouble();
        lastTradeReward = in.readDouble();
        totTGReward = in.readDouble();
        lastTGReward = in.readDouble();
        totAgentReward = in.readDouble();
        lastAgentReward = in.readDouble();
        totBillReward = in.readDouble();
        lastBillReward = in.readDouble();
    }

    public static final Creator<RewardLogResponse> CREATOR = new Creator<RewardLogResponse>() {
        @Override
        public RewardLogResponse createFromParcel(Parcel in) {
            return new RewardLogResponse(in);
        }

        @Override
        public RewardLogResponse[] newArray(int size) {
            return new RewardLogResponse[size];
        }
    };

    public int getFirstTG() {
        return firstTG;
    }

    public int getSecondTG() {
        return secondTG;
    }

    public int getFirstAgentTG() {
        return firstAgentTG;
    }

    public int getSecondAgentTG() {
        return secondAgentTG;
    }

    public int getGeneralTG() {
        return generalTG;
    }

    public double getTotTradeReward() {
        return totTradeReward;
    }

    public double getLastTradeReward() {
        return lastTradeReward;
    }

    public double getTotTGReward() {
        return totTGReward;
    }

    public double getLastTGReward() {
        return lastTGReward;
    }

    public double getTotAgentReward() {
        return totAgentReward;
    }

    public double getLastAgentReward() {
        return lastAgentReward;
    }

    public double getTotBillReward() {
        return totBillReward;
    }

    public double getLastBillReward() {
        return lastBillReward;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(firstTG);
        dest.writeInt(secondTG);
        dest.writeInt(firstAgentTG);
        dest.writeInt(secondAgentTG);
        dest.writeInt(generalTG);
        dest.writeDouble(totTradeReward);
        dest.writeDouble(lastTradeReward);
        dest.writeDouble(totTGReward);
        dest.writeDouble(lastTGReward);
        dest.writeDouble(totAgentReward);
        dest.writeDouble(lastAgentReward);
        dest.writeDouble(totBillReward);
        dest.writeDouble(lastBillReward);
    }

    @Override
    public String toString() {
        return "RewardLogResponse{" +
                "firstTG=" + firstTG +
                ", secondTG=" + secondTG +
                ", firstAgentTG=" + firstAgentTG +
                ", secondAgentTG=" + secondAgentTG +
                ", generalTG=" + generalTG +
                ", totTradeReward=" + totTradeReward +
                ", lastTradeReward=" + lastTradeReward +
                ", totTGReward=" + totTGReward +
                ", lastTGReward=" + lastTGReward +
                ", totAgentReward=" + totAgentReward +
                ", lastAgentReward=" + lastAgentReward +
                ", totBillReward=" + totBillReward +
                ", lastBillReward=" + lastBillReward +
                '}';
    }
}
