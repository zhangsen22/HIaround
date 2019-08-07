package hiaround.android.com.modle;

import android.os.Parcel;
import android.os.Parcelable;

public class BuyBusinessResponse extends BaseBean implements Parcelable {

    private String tradeId;//:"fdsafds"    //订单号
    private int payCode;//:1234            //付款参考码
    private long currentTime;
    private Object payee;//付款信息json

    public BuyBusinessResponse(String tradeId, int payCode, long currentTime, Object payee) {
        this.tradeId = tradeId;
        this.payCode = payCode;
        this.currentTime = currentTime;
        this.payee = payee;
    }

    protected BuyBusinessResponse(Parcel in) {
        tradeId = in.readString();
        payCode = in.readInt();
        currentTime = in.readLong();
        payee = in.readValue(ClassLoader.getSystemClassLoader());
    }

    public static final Creator<BuyBusinessResponse> CREATOR = new Creator<BuyBusinessResponse>() {
        @Override
        public BuyBusinessResponse createFromParcel(Parcel in) {
            return new BuyBusinessResponse(in);
        }

        @Override
        public BuyBusinessResponse[] newArray(int size) {
            return new BuyBusinessResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tradeId);
        dest.writeInt(payCode);
        dest.writeLong(currentTime);
        dest.writeValue(payee);
    }

    public String getTradeId() {
        return tradeId;
    }

    public int getPayCode() {
        return payCode;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public Object getPayee() {
        return payee;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return "BuyBusinessResponse{" +
                "tradeId='" + tradeId + '\'' +
                ", payCode=" + payCode +
                ", currentTime=" + currentTime +
                ", payee=" + payee +
                '}';
    }
}
