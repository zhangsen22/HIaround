package aimi.android.com.modle;

public class MyEntrustinfoItem {

    private long id;//:12312        //挂单id
    private long userid;//:3131     //挂单用户id
    private String nickname;//:"fer" //挂单用户昵称
    private double price;//：6.78    //挂单价格
    private double minNum;//：100    //最小数量
    private double maxNum;//：200    //最大数量
    private boolean supportAli;//：true    //是否支持支付宝,买单不读此项
    private boolean supportWechat;//:false    //是否支持微信支付,买单不读此项
    private boolean supportBank;//:false    //是否支持银行卡支付,买单不读此项
    private long puttime;//：21313131    //挂单时间
    private int status;//:1            //状态 0为当前正常挂单 1为已完成 2为已撤销

    private String usdtTotalMoneyFmt;
    private String usdtPriceFmt ;
    private String usdtNumFmt;

    public long getId() {
        return id;
    }

    public long getUserid() {
        return userid;
    }

    public String getNickname() {
        return nickname;
    }

    public double getPrice() {
        return price;
    }

    public double getMinNum() {
        return minNum;
    }

    public double getMaxNum() {
        return maxNum;
    }

    public boolean isSupportAli() {
        return supportAli;
    }

    public boolean isSupportWechat() {
        return supportWechat;
    }

    public boolean isSupportBank() {
        return supportBank;
    }

    public long getPuttime() {
        return puttime;
    }

    public int getStatus() {
        return status;
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
        return "MyEntrustinfoItem{" +
                "id=" + id +
                ", userid=" + userid +
                ", nickname='" + nickname + '\'' +
                ", price=" + price +
                ", minNum=" + minNum +
                ", maxNum=" + maxNum +
                ", supportAli=" + supportAli +
                ", supportWechat=" + supportWechat +
                ", supportBank=" + supportBank +
                ", puttime=" + puttime +
                ", status=" + status +
                ", usdtTotalMoneyFmt='" + usdtTotalMoneyFmt + '\'' +
                ", usdtPriceFmt='" + usdtPriceFmt + '\'' +
                ", usdtNumFmt='" + usdtNumFmt + '\'' +
                '}';
    }
}
