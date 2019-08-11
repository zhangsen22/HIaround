package hiaround.android.com.modle;

import android.os.Parcel;
import android.os.Parcelable;

public class BuyBusinessResponse extends BaseBean implements Parcelable {

    private String tradeId;//:"fdsafds"    //订单号
    private int payCode;//:1234            //付款参考码
    private long currentTime;
    private String usdtTotalMoneyFmt;
    private String usdtPriceFmt ;
    private String usdtNumFmt;
    private int payType;
    private Object payee;//付款信息json

    public BuyBusinessResponse(String tradeId, int payCode, long currentTime, String usdtTotalMoneyFmt, String usdtPriceFmt, String usdtNumFmt, int payType, Object payee) {
        this.tradeId = tradeId;
        this.payCode = payCode;
        this.currentTime = currentTime;
        this.usdtTotalMoneyFmt = usdtTotalMoneyFmt;
        this.usdtPriceFmt = usdtPriceFmt;
        this.usdtNumFmt = usdtNumFmt;
        this.payType = payType;
        this.payee = payee;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tradeId);
        dest.writeInt(payCode);
        dest.writeInt(payType);
        dest.writeLong(currentTime);
        dest.writeString(usdtTotalMoneyFmt);
        dest.writeString(usdtPriceFmt);
        dest.writeString(usdtNumFmt);
        dest.writeValue(payee);
    }

    @Override
    public int describeContents() {
        return 0;
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

    protected BuyBusinessResponse(Parcel in) {
        tradeId = in.readString();
        payCode = in.readInt();
        payType = in.readInt();
        currentTime = in.readLong();
        usdtTotalMoneyFmt = in.readString();
        usdtPriceFmt = in.readString();
        usdtNumFmt = in.readString();
        payee = in.readValue(ClassLoader.getSystemClassLoader());
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

    public String getUsdtTotalMoneyFmt() {
        return usdtTotalMoneyFmt;
    }

    public String getUsdtPriceFmt() {
        return usdtPriceFmt;
    }

    public String getUsdtNumFmt() {
        return usdtNumFmt;
    }

    public int getPayType() {
        return payType;
    }

    @Override
    public String toString() {
        return "BuyBusinessResponse{" +
                "tradeId='" + tradeId + '\'' +
                ", payCode=" + payCode +
                ", currentTime=" + currentTime +
                ", usdtTotalMoneyFmt='" + usdtTotalMoneyFmt + '\'' +
                ", usdtPriceFmt='" + usdtPriceFmt + '\'' +
                ", usdtNumFmt='" + usdtNumFmt + '\'' +
                ", payType=" + payType +
                ", payee=" + payee +
                '}';
    }

}
