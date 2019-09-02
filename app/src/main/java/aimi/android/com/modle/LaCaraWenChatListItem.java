package aimi.android.com.modle;

public class LaCaraWenChatListItem {

    //            "paymentId":"支付id",
//                "isDefault":"true",
//                "account":"账号名称",
//                "locked":"true",
//                "watchStop":"flase",
//                "watchUnbind":""
    private long paymentId;//:1                //id
    private boolean isDefault;
    private String account;//:"ewrwre",        //账号名称，
    private boolean locked;
    private boolean watchUnbind;// 是否已经解除监控
    private boolean watchStop;//是否已经扫描二维码并且登录了 true:没扫描或者扫了没有登录上     false:已扫描并且登陆上了

    public long getPaymentId() {
        return paymentId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getAccount() {
        return account;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isWatchUnbind() {
        return watchUnbind;
    }

    public boolean isWatchStop() {
        return watchStop;
    }

    @Override
    public String toString() {
        return "LaCaraWenChatListItem{" +
                "paymentId=" + paymentId +
                ", isDefault=" + isDefault +
                ", account='" + account + '\'' +
                ", locked=" + locked +
                ", watchUnbind=" + watchUnbind +
                ", watchStop=" + watchStop +
                '}';
    }
}
