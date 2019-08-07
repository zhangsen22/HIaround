package hiaround.android.com.modle;

public class WechatLoginModle extends BaseBean {
    private long paymentId;
    private String loginCode;

    public long getPaymentId() {
        return paymentId;
    }

    public String getLoginCode() {
        return loginCode;
    }

    @Override
    public String toString() {
        return "WechatLoginModle{" +
                "paymentId=" + paymentId +
                ", loginCode='" + loginCode + '\'' +
                '}';
    }
}
