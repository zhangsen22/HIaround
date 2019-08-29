package aimi.android.com.modle;

public class RegistResponse extends BaseBean{
    private long id;//：32131                //自己的用户id
    private String invitedCode;//："1234"        //自己的邀请码
    private String downloadUrl;//："http://...    "      //专属下载地址
    private String walletAddr;//："fdsfsd"        //钱包地址

    public long getId() {
        return id;
    }

    public String getInvitedCode() {
        return invitedCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getWalletAddr() {
        return walletAddr;
    }

    @Override
    public String toString() {
        return "RegistResponse{" +
                "id=" + id +
                ", invitedCode='" + invitedCode + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", walletAddr='" + walletAddr + '\'' +
                '}';
    }
}
