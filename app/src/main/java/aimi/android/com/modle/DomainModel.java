package aimi.android.com.modle;

import java.util.List;

public class DomainModel extends BaseBean{
    private List<String> gateway;
    private String version;
    private boolean wxPayLock;//是否锁定微信支付  true:锁定  不让看  不显示  false:相反
    private List<String> downLoad;//h5域名和apk下载地址域名

    public List<String> getGateway() {
        return gateway;
    }

    public String getVersion() {
        return version;
    }

    public boolean isWxPayLock() {
        return wxPayLock;
    }

    public List<String> getDownLoad() {
        return downLoad;
    }

    @Override
    public String toString() {
        return "DomainModel{" +
                "gateway=" + gateway +
                ", version='" + version + '\'' +
                ", wxPayLock=" + wxPayLock +
                ", downLoad=" + downLoad +
                '}';
    }
}
