package aimi.android.com.modle;

public class AliPayPayeeItemModelPayee {

    private long id;//:3                //id
    private String name;//："放大",        //姓名，
    private String account;//:"ewrwre",        //支付宝账号，
    private String accountid;
    private String base64Img;//:"rewr"        //支付宝收款二维码
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
        return "AliPayPayeeItemModelPayee{" +
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
