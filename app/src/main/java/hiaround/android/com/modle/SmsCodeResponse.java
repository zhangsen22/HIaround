package hiaround.android.com.modle;

public class SmsCodeResponse extends BaseBean{
    private String key;

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "SmsCodeResponse{" +
                "key='" + key + '\'' +
                '}';
    }
}
