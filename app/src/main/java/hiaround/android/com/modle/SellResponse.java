package hiaround.android.com.modle;

import android.os.Parcel;
import android.os.Parcelable;

public class SellResponse extends BaseBean implements Parcelable {
    private String tradeId;//:"fdsafds"        //订单号
    private int payCode;//:1234            //付款参考码
    private long creatTime;

    public SellResponse(String tradeId, int payCode, long creatTime) {
        this.tradeId = tradeId;
        this.payCode = payCode;
        this.creatTime = creatTime;
    }

    protected SellResponse(Parcel in) {
        tradeId = in.readString();
        payCode = in.readInt();
        creatTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tradeId);
        dest.writeInt(payCode);
        dest.writeLong(creatTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SellResponse> CREATOR = new Creator<SellResponse>() {
        @Override
        public SellResponse createFromParcel(Parcel in) {
            return new SellResponse(in);
        }

        @Override
        public SellResponse[] newArray(int size) {
            return new SellResponse[size];
        }
    };

    public String getTradeId() {
        return tradeId;
    }

    public int getPayCode() {
        return payCode;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    @Override
    public String toString() {
        return "SellResponse{" +
                "tradeId='" + tradeId + '\'' +
                ", payCode=" + payCode +
                ", creatTime=" + creatTime +
                '}';
    }
}
