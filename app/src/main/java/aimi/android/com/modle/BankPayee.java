package aimi.android.com.modle;

public class BankPayee {

    private long id;//: 7,
    private String bankName;//: "9699",
    private String subName;//: "96999999999",
    private String name;//: "9699",
    private String account;//: "96999999999",
    private boolean locked;//: false,
    private boolean watchStop;//: false

    public long getId() {
        return id;
    }

    public String getBankName() {
        return bankName;
    }

    public String getSubName() {
        return subName;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isWatchStop() {
        return watchStop;
    }

    @Override
    public String toString() {
        return "BankPayee{" +
                "id=" + id +
                ", bankName='" + bankName + '\'' +
                ", subName='" + subName + '\'' +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", locked=" + locked +
                ", watchStop=" + watchStop +
                '}';
    }
}
