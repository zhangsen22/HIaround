package aimi.android.com.modle;

import java.util.List;

public class LaCaraWenChatListModle extends BaseBean {
//    {"ret":0,"list":[
//
//        {
//            "paymentId":"支付id",
//                "isDefault":"true",
//                "account":"账号名称",
//                "locked":"true",
//                "watchStop":"flase",
//                "watchUnbind":""
//
//        }
//]}

    private List<LaCaraWenChatListItem> list;

    public List<LaCaraWenChatListItem> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "LaCaraWenChatListModle{" +
                "list=" + list +
                '}';
    }
}
