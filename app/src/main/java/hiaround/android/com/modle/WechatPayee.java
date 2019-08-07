package hiaround.android.com.modle;

public class WechatPayee {

    private long id;//: 1,
    private String name;//: "啊咯无卡",
    private String account;//: "123654789",
    private String base64Img;//: "https://qr.alipay.com/tsx01806lbewkpfkrystw9d",
    private boolean locked;//: false,
    private boolean watchStop;//: false

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getBase64Img() {
        return base64Img;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isWatchStop() {
        return watchStop;
    }

    @Override
    public String toString() {
        return "WechatPayee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", base64Img='" + base64Img + '\'' +
                ", locked=" + locked +
                ", watchStop=" + watchStop +
                '}';
    }
}
