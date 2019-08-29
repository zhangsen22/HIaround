package aimi.android.com.modle;

public class AliPayee {

    private long id;//: 20,
    private String name;//: "都哭不出来",
    private String account;//: "栟榈恶魔用",
    private String accountid;//: "1236547890987412",
    private String base64Img;//: "wxp://f2f0fueJ8BjVCUce3bOzoztQ9iKL9VgjOPHg",
    private boolean locked;//: false,
    private boolean watchStop;//: false

    public AliPayee(String account, String base64Img) {
        this.account = account;
        this.base64Img = base64Img;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getAccountid() {
        return accountid;
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
        return "AliPayee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", accountid='" + accountid + '\'' +
                ", base64Img='" + base64Img + '\'' +
                ", locked=" + locked +
                ", watchStop=" + watchStop +
                '}';
    }
}
