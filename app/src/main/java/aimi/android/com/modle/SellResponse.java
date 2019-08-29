package aimi.android.com.modle;

import android.os.Parcel;
import android.os.Parcelable;

public class SellResponse extends BaseBean implements Parcelable {
    private String tradeId;//:"fdsafds"        //订单号
    private int payCode;//:1234            //付款参考码
    private long creatTime;
    private String usdtTotalMoneyFmt;
    private String usdtPriceFmt ;
    private String usdtNumFmt;

    public SellResponse(String tradeId, int payCode, long creatTime, String usdtTotalMoneyFmt, String usdtPriceFmt, String usdtNumFmt) {
        this.tradeId = tradeId;
        this.payCode = payCode;
        this.creatTime = creatTime;
        this.usdtTotalMoneyFmt = usdtTotalMoneyFmt;
        this.usdtPriceFmt = usdtPriceFmt;
        this.usdtNumFmt = usdtNumFmt;
    }

    protected SellResponse(Parcel in) {
        tradeId = in.readString();
        payCode = in.readInt();
        creatTime = in.readLong();
        usdtTotalMoneyFmt = in.readString();
        usdtPriceFmt = in.readString();
        usdtNumFmt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tradeId);
        dest.writeInt(payCode);
        dest.writeLong(creatTime);
        dest.writeString(usdtTotalMoneyFmt);
        dest.writeString(usdtPriceFmt);
        dest.writeString(usdtNumFmt);
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

    public String getUsdtTotalMoneyFmt() {
        return usdtTotalMoneyFmt;
    }

    public String getUsdtPriceFmt() {
        return usdtPriceFmt;
    }

    public String getUsdtNumFmt() {
        return usdtNumFmt;
    }

    @Override
    public String toString() {
        return "SellResponse{" +
                "tradeId='" + tradeId + '\'' +
                ", payCode=" + payCode +
                ", creatTime=" + creatTime +
                ", usdtTotalMoneyFmt='" + usdtTotalMoneyFmt + '\'' +
                ", usdtPriceFmt='" + usdtPriceFmt + '\'' +
                ", usdtNumFmt='" + usdtNumFmt + '\'' +
                '}';
    }
}
