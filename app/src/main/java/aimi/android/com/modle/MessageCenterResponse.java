package aimi.android.com.modle;

import java.util.List;

public class MessageCenterResponse extends BaseBean{
    private List<MessageCenterItem> msg;

    public List<MessageCenterItem> getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "MessageCenterResponse{" +
                "msg=" + msg +
                '}';
    }
}
