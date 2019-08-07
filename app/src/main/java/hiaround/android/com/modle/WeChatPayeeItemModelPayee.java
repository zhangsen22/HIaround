package hiaround.android.com.modle;

import android.os.Parcel;
import android.os.Parcelable;

public class WeChatPayeeItemModelPayee implements Parcelable {

    private long id;//:1                //id
    private String name;//："放大",        //微信名，
    private String account;//:"ewrwre",        //微信号，
    private String base64Img;//:"rewr"        //微信收款二维码
    private boolean locked;
    private boolean watchUnbind;// 是否已经解除监控
    private boolean watchStop;//是否已经扫描二维码并且登录了 true:没扫描或者扫了没有登录上     false:已扫描并且登陆上了


    protected WeChatPayeeItemModelPayee(Parcel in) {
        id = in.readLong();
        name = in.readString();
        account = in.readString();
        base64Img = in.readString();
        locked = in.readByte() != 0;
        watchUnbind = in.readByte() != 0;
        watchStop = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(account);
        dest.writeString(base64Img);
        dest.writeByte((byte) (locked ? 1 : 0));
        dest.writeByte((byte) (watchUnbind ? 1 : 0));
        dest.writeByte((byte) (watchStop ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeChatPayeeItemModelPayee> CREATOR = new Creator<WeChatPayeeItemModelPayee>() {
        @Override
        public WeChatPayeeItemModelPayee createFromParcel(Parcel in) {
            return new WeChatPayeeItemModelPayee(in);
        }

        @Override
        public WeChatPayeeItemModelPayee[] newArray(int size) {
            return new WeChatPayeeItemModelPayee[size];
        }
    };

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

    public boolean isWatchUnbind() {
        return watchUnbind;
    }

    @Override
    public String toString() {
        return "WeChatPayeeItemModelPayee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", base64Img='" + base64Img + '\'' +
                ", locked=" + locked +
                ", watchUnbind=" + watchUnbind +
                ", watchStop=" + watchStop +
                '}';
    }
}
