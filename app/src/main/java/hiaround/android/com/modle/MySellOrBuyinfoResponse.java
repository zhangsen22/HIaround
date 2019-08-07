package hiaround.android.com.modle;

import java.util.List;

public class MySellOrBuyinfoResponse extends BaseBean{
    private List<MySellOrBuyinfoItem> info;

    public List<MySellOrBuyinfoItem> getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "MySellOrBuyinfoResponse{" +
                "info=" + info +
                '}';
    }
}
