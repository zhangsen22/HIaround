package aimi.android.com.app;

import aimi.android.com.modle.BaseBean;

public class AccountInfo extends BaseBean {

    private long id;//：32131                //自己的用户id
    private String invitedCode;//："1234"        //自己的邀请码
    private String downloadUrl;//："http://...    "      //专属下载地址
    private String walletaddr;//："fdsfsd"        //钱包地址
    private String nickname;//:"fdsa"            //昵称，未设置时为""
    private boolean havefinancePwd;//:false    //是否有资金密码
    private boolean autoBuy;//:false            //是否自动买币
    private boolean autoSell;//:false            //是否自动放币
    private int idstatus;//:0                //身份验证状态 0未验证，1等待人工审核 2 已验证 99 验证失败
    private boolean haveWechatPayee;//:true    //是否有微信收款方式
    private boolean haveAliPayee;//:false        //是否有支付宝收款方式
    private boolean haveBankPayee;//:false        //是否有银行收款方式
    private boolean haveCloudPayee;//:false        //是否有云闪付收款方式
    private boolean haveLakalaPayee;//:false        //是否有拉卡拉收款方式
    private int apiType;//api匹配类型,0为普通类型 1为代理商类型
    private int roleType;//角色类型(2=总代/1=代理/0=普通)

    private String phoneNumber;
    private String password;

    public String getWalletaddr() {
        return walletaddr;
    }

    public void setWalletaddr(String walletaddr) {
        this.walletaddr = walletaddr;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvitedCode() {
        return invitedCode;
    }

    public void setInvitedCode(String invitedCode) {
        this.invitedCode = invitedCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isHavefinancePwd() {
        return havefinancePwd;
    }

    public void setHavefinancePwd(boolean havefinancePwd) {
        this.havefinancePwd = havefinancePwd;
    }

    public boolean isAutoBuy() {
        return autoBuy;
    }

    public void setAutoBuy(boolean autoBuy) {
        this.autoBuy = autoBuy;
    }

    public boolean isAutoSell() {
        return autoSell;
    }

    public void setAutoSell(boolean autoSell) {
        this.autoSell = autoSell;
    }

    public int getIDstatus() {
        return idstatus;
    }

    public void setIDstatus(int IDstatus) {
        this.idstatus = IDstatus;
    }

    public boolean isHaveWechatPayee() {
        return haveWechatPayee;
    }

    public void setHaveWechatPayee(boolean haveWechatPayee) {
        this.haveWechatPayee = haveWechatPayee;
    }

    public boolean isHaveAliPayee() {
        return haveAliPayee;
    }

    public void setHaveAliPayee(boolean haveAliPayee) {
        this.haveAliPayee = haveAliPayee;
    }

    public boolean isHaveBankPayee() {
        return haveBankPayee;
    }

    public void setHaveBankPayee(boolean haveBankPayee) {
        this.haveBankPayee = haveBankPayee;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public int getApiType() {
        return apiType;
    }

    public void setApiType(int apiType) {
        this.apiType = apiType;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public boolean isHaveCloudPayee() {
        return haveCloudPayee;
    }

    public void setHaveCloudPayee(boolean haveCloudPayee) {
        this.haveCloudPayee = haveCloudPayee;
    }
    public boolean isHaveLakalaPayee() {
        return haveLakalaPayee;
    }

    public void setHaveLakalaPayee(boolean haveLakalaPayee) {
        this.haveLakalaPayee = haveLakalaPayee;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "id=" + id +
                ", invitedCode='" + invitedCode + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", walletaddr='" + walletaddr + '\'' +
                ", nickname='" + nickname + '\'' +
                ", havefinancePwd=" + havefinancePwd +
                ", autoBuy=" + autoBuy +
                ", autoSell=" + autoSell +
                ", idstatus=" + idstatus +
                ", haveWechatPayee=" + haveWechatPayee +
                ", haveAliPayee=" + haveAliPayee +
                ", haveBankPayee=" + haveBankPayee +
                ", haveCloudPayee=" + haveCloudPayee +
                ", haveLakalaPayee=" + haveLakalaPayee +
                ", apiType=" + apiType +
                ", roleType=" + roleType +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
