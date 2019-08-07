package hiaround.android.com.modle;

import java.util.List;

public class InvitationResponse extends BaseBean {

    private List<InvitationItem> list;

    public List<InvitationItem> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "InvitationResponse{" +
                "list=" + list +
                '}';
    }
}
