package hiaround.android.com.modle;

public class BankPayeeItemModelPayee {
    private long id;//:1                    //id
    private String bankName;//：“”                //银行名称，
    private String subName;//:""                //所在支行，
    private String name;//:""                    //真实姓名，
    private String account;//:""                //银行卡号，
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
        return "BankPayeeItemModelPayee{" +
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
