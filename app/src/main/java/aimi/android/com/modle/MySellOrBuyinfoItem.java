package aimi.android.com.modle;

import android.os.Parcel;
import android.os.Parcelable;

public class MySellOrBuyinfoItem implements Parcelable {

    private long id;//:12312        //挂单id
    private String tradeid;//:"fdsafds"    //订单号
    private double price;//:12.21            //单价
    private double num;//:2.1                //数量
    private int status;//:1            //订单状态,见下面表格
    private int payType;//:1                //收款方式,1为支付宝，2为微信，3为银行账户
    private long createTime;//:1        //订单创建时间
    private long payTime;//:            //付款时间
    private int payCode;//:1234            //付款参考码
    private int tradeSource;//1: 卖  其他:充
    private String usdtTotalMoneyFmt;
    private String usdtPriceFmt ;
    private String usdtNumFmt;
    private Object payee;//付款信息json

    protected MySellOrBuyinfoItem(Parcel in) {
        id = in.readLong();
        tradeid = in.readString();
        price = in.readDouble();
        num = in.readDouble();
        status = in.readInt();
        payType = in.readInt();
        createTime = in.readLong();
        payTime = in.readLong();
        payCode = in.readInt();
        tradeSource = in.readInt();
        payee = in.readValue(ClassLoader.getSystemClassLoader());
        usdtTotalMoneyFmt = in.readString();
        usdtPriceFmt = in.readString();
        usdtNumFmt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(tradeid);
        dest.writeDouble(price);
        dest.writeDouble(num);
        dest.writeInt(status);
        dest.writeInt(payType);
        dest.writeLong(createTime);
        dest.writeLong(payTime);
        dest.writeInt(payCode);
        dest.writeInt(tradeSource);
        dest.writeValue(payee);
        dest.writeString(usdtTotalMoneyFmt);
        dest.writeString(usdtPriceFmt);
        dest.writeString(usdtNumFmt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MySellOrBuyinfoItem> CREATOR = new Creator<MySellOrBuyinfoItem>() {
        @Override
        public MySellOrBuyinfoItem createFromParcel(Parcel in) {
            return new MySellOrBuyinfoItem(in);
        }

        @Override
        public MySellOrBuyinfoItem[] newArray(int size) {
            return new MySellOrBuyinfoItem[size];
        }
    };

    public double getPrice() {
        return price;
    }

    public double getNum() {
        return num;
    }

    public int getStatus() {
        return status;
    }

    public long getId() {
        return id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getPayTime() {
        return payTime;
    }

    public int getPayCode() {
        return payCode;
    }


    public int getTradeSource() {
        return tradeSource;
    }

    public String getTradeid() {
        return tradeid;
    }

    public int getPayType() {
        return payType;
    }

    public Object getPayee() {
        return payee;
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
        return "MySellOrBuyinfoItem{" +
                "id=" + id +
                ", tradeid='" + tradeid + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", status=" + status +
                ", payType=" + payType +
                ", createTime=" + createTime +
                ", payTime=" + payTime +
                ", payCode=" + payCode +
                ", tradeSource=" + tradeSource +
                ", usdtTotalMoneyFmt='" + usdtTotalMoneyFmt + '\'' +
                ", usdtPriceFmt='" + usdtPriceFmt + '\'' +
                ", usdtNumFmt='" + usdtNumFmt + '\'' +
                ", payee=" + payee +
                '}';
    }


}
