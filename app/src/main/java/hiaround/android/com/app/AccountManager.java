package hiaround.android.com.app;

import android.content.Context;
import android.content.SharedPreferences;
import hiaround.android.com.MyApplication;

/**
 * 管理登陆用户
 */
public class AccountManager {
    private static final String ACCOUNT_KEY = "hiaround_key";
    private static final String ID = "id";
    private static final String INVITEDCODE = "invitedCode";
    private static final String DOWNLOADURL = "downloadUrl";
    private static final String WALLETADDR = "walletAddr";
    private static final String NICKNAME = "nickname";
    private static final String PHONENUMBER = "phonenumber";
    private static final String PASSWORD = "password";
    private static final String HAVEFINANCEPWD = "havefinancePwd";
    private static final String AUTOBUY = "autoBuy";
    private static final String AUTOSELL = "autoSell";
    private static final String IDSTATUS = "IDstatus";
    private static final String HAVEWECHATPAYEE = "haveWechatPayee";
    private static final String HAVEALIPAYEE = "haveAliPayee";
    private static final String HAVEBANKPAYEE = "haveBankPayee";
    private static final String HAVECLOUDPAYEE = "haveCloudPayee";
    private static final String APITYPE = "apiType";
    private static final String ROLETYPE = "roleType";

    //静态内部类
    private static class AccountManagerHoder{
        private static final AccountManager instance = new AccountManager();
    }

    private AccountManager(){
        mSharedPreferences = MyApplication.appContext.getSharedPreferences(ACCOUNT_KEY, Context.MODE_PRIVATE);
    }

    //第一次调用getInstance方法时，才会去加载SingleHolder类，继而实例化instance
    public static final AccountManager getInstance(){
        return AccountManagerHoder.instance;
    }

    private SharedPreferences mSharedPreferences;
    private AccountInfo mAccountInfo;


    /**
     * 保存用户信息到本地
     * @param accountInfo
     */
    public void saveAccountInfoFormModel(AccountInfo accountInfo) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putLong(ID, accountInfo.getId());
        edit.putString(INVITEDCODE, accountInfo.getInvitedCode());
        edit.putString(DOWNLOADURL, accountInfo.getDownloadUrl());
        edit.putString(WALLETADDR, accountInfo.getWalletaddr());
        edit.putString(NICKNAME, accountInfo.getNickname());
        edit.putBoolean(HAVEFINANCEPWD, accountInfo.isHavefinancePwd());
        edit.putBoolean(AUTOBUY, accountInfo.isAutoBuy());
        edit.putBoolean(AUTOSELL, accountInfo.isAutoSell());
        edit.putInt(IDSTATUS, accountInfo.getIDstatus());
        edit.putBoolean(HAVEWECHATPAYEE, accountInfo.isHaveWechatPayee());
        edit.putBoolean(HAVEALIPAYEE, accountInfo.isHaveAliPayee());
        edit.putBoolean(HAVEBANKPAYEE, accountInfo.isHaveBankPayee());
        edit.putBoolean(HAVECLOUDPAYEE, accountInfo.isHaveCloudPayee());
        edit.putString(PHONENUMBER, accountInfo.getPhoneNumber());
        edit.putString(PASSWORD, accountInfo.getPassword());
        edit.putInt(APITYPE,accountInfo.getApiType());
        edit.putInt(ROLETYPE,accountInfo.getRoleType());
        edit.apply();
        mAccountInfo = getAccountInfoFormLocate();
    }


    public void logout() {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.remove(ID);
        edit.remove(INVITEDCODE);
        edit.remove(DOWNLOADURL);
        edit.remove(WALLETADDR);
        edit.remove(NICKNAME);
        edit.remove(HAVEFINANCEPWD);
        edit.remove(AUTOBUY);
        edit.remove(AUTOSELL);
        edit.remove(IDSTATUS);
        edit.remove(HAVEWECHATPAYEE);
        edit.remove(HAVEALIPAYEE);
        edit.remove(HAVEBANKPAYEE);
        edit.remove(HAVECLOUDPAYEE);
        edit.remove(PHONENUMBER);
        edit.remove(PASSWORD);
        edit.remove(APITYPE);
        edit.remove(ROLETYPE);
        edit.apply();
        mAccountInfo = null;
    }

    private AccountInfo getAccountInfoFormLocate() {
        mAccountInfo = new AccountInfo();
        mAccountInfo.setId(mSharedPreferences.getLong(ID, 0));
        mAccountInfo.setInvitedCode(mSharedPreferences.getString(INVITEDCODE, ""));
        mAccountInfo.setDownloadUrl(mSharedPreferences.getString(DOWNLOADURL, ""));
        mAccountInfo.setWalletaddr(mSharedPreferences.getString(WALLETADDR, ""));
        mAccountInfo.setNickname(mSharedPreferences.getString(NICKNAME, ""));
        mAccountInfo.setHavefinancePwd(mSharedPreferences.getBoolean(HAVEFINANCEPWD, false));
        mAccountInfo.setAutoBuy(mSharedPreferences.getBoolean(AUTOBUY, false));
        mAccountInfo.setAutoSell(mSharedPreferences.getBoolean(AUTOSELL, false));
        mAccountInfo.setHaveWechatPayee(mSharedPreferences.getBoolean(HAVEWECHATPAYEE, false));
        mAccountInfo.setHaveAliPayee(mSharedPreferences.getBoolean(HAVEALIPAYEE, false));
        mAccountInfo.setHaveBankPayee(mSharedPreferences.getBoolean(HAVEBANKPAYEE, false));
        mAccountInfo.setHaveCloudPayee(mSharedPreferences.getBoolean(HAVECLOUDPAYEE, false));
        mAccountInfo.setIDstatus(mSharedPreferences.getInt(IDSTATUS, -1));
        mAccountInfo.setPhoneNumber(mSharedPreferences.getString(PHONENUMBER, ""));
        mAccountInfo.setPassword(mSharedPreferences.getString(PASSWORD, ""));
        mAccountInfo.setApiType(mSharedPreferences.getInt(APITYPE, 0));
        mAccountInfo.setRoleType(mSharedPreferences.getInt(ROLETYPE, 0));
        return mAccountInfo;
    }

    /**
     * 获取用户信息，包括userId,userName,UserHead,sessionId
     *
     * @return 这里返回的AccountInfo对象不可能为空
     */
    public AccountInfo getAccountInfo() {
        if (mAccountInfo == null) {
            mAccountInfo = getAccountInfoFormLocate();
        }
        return mAccountInfo;
    }

    /**
     * 判断是否已经登录
     *
     * @return true:已经登录，false:未登录
     */
    public boolean isLogin() {
        return getAccountInfo().getId() > 0;
    }

//
//        public AccountManager setPassWord(String passWord) {
//            mAccountInfo.setPassword(passWord);
//            mSharedPreferences.edit().putString(PASSWORD, passWord).apply();
//            return this;
//    }
    public long getUserId() {
        return getAccountInfo().getId();
    }

    public int getIDstatus() {
        return getAccountInfo().getIDstatus();
    }

    public String getNickname() {
        return getAccountInfo().getNickname();
    }

    public String getPhoneNumber() {
        return getAccountInfo().getPhoneNumber();
    }

    public String getInvitedCode() {
        return getAccountInfo().getInvitedCode();
    }

    public String getDownloadUrl() {
        return getAccountInfo().getDownloadUrl();
    }

    public String getPassWord() {
        return getAccountInfo().getPassword();
    }

    public String getWalletAddr() {
        return getAccountInfo().getWalletaddr();
    }

    public boolean isHaveAliPayee() {
        return getAccountInfo().isHaveAliPayee();
    }

    public boolean isHaveWechatPayee() {
        return getAccountInfo().isHaveWechatPayee();
    }

    public boolean isHaveBankPayee() {
        return getAccountInfo().isHaveBankPayee();
    }

    public int getApiType() {
        return getAccountInfo().getApiType();
    }

    public int getRoleType() {
        return getAccountInfo().getRoleType();
    }

    public boolean isHaveCloudPayee() {
        return getAccountInfo().isHaveCloudPayee();
    }


    public AccountManager setNickname(String nickname) {
        getAccountInfo().setNickname(nickname);
        mSharedPreferences.edit().putString(NICKNAME, nickname).apply();
        return this;
    }

    public AccountManager setHaveWechatPayee(boolean haveWechatPayee) {
        getAccountInfo().setHaveWechatPayee(haveWechatPayee);
        mSharedPreferences.edit().putBoolean(HAVEWECHATPAYEE, haveWechatPayee).apply();
        return this;
    }

    public AccountManager setHaveAliPayee(boolean haveAliPayee) {
        getAccountInfo().setHaveAliPayee(haveAliPayee);
        mSharedPreferences.edit().putBoolean(HAVEALIPAYEE, haveAliPayee).apply();
        return this;
    }

    public AccountManager setHaveBankPayee(boolean haveBankPayee) {
        getAccountInfo().setHaveBankPayee(haveBankPayee);
        mSharedPreferences.edit().putBoolean(HAVEBANKPAYEE, haveBankPayee).apply();
        return this;
    }

    public AccountManager setHaveCloudPayee(boolean haveCloudPayee) {
        getAccountInfo().setHaveCloudPayee(haveCloudPayee);
        mSharedPreferences.edit().putBoolean(HAVECLOUDPAYEE, haveCloudPayee).apply();
        return this;
    }

    public AccountManager setIDstatus(int IDstatus) {
        getAccountInfo().setIDstatus(IDstatus);
        mSharedPreferences.edit().putInt(IDSTATUS, IDstatus).apply();
        return this;
    }
}
