package aimi.android.com.modle;

public class InvitationItem {

    private long userId;
    private String phone;
    private String nickname;
    private double money;

    public long getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getNickname() {
        return nickname;
    }

    public double getMoney() {
        return money;
    }

    @Override
    public String toString() {
        return "InvitationItem{" +
                "userId=" + userId +
                ", phone='" + phone + '\'' +
                ", nickname='" + nickname + '\'' +
                ", money=" + money +
                '}';
    }
}
