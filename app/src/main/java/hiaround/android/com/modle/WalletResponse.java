package hiaround.android.com.modle;

public class WalletResponse extends BaseBean{
    //USDT
    private double walletNum;//：22            // 钱包可用资产
    private double walletFreezeNum;//:11        //钱包冻结资产
    //INF
    private double hotNum;//:33                //交易可用账户资产
    private double hotFreezeNum;//:44            //交易账户冻结资产

    public double getWalletNum() {
        return walletNum;
    }

    public double getWalletFreezeNum() {
        return walletFreezeNum;
    }

    public double getHotNum() {
        return hotNum;
    }

    public double getHotFreezeNum() {
        return hotFreezeNum;
    }

    @Override
    public String toString() {
        return "WalletResponse{" +
                "walletNum=" + walletNum +
                ", walletFreezeNum=" + walletFreezeNum +
                ", hotNum=" + hotNum +
                ", hotFreezeNum=" + hotFreezeNum +
                '}';
    }
}
