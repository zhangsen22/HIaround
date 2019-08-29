package aimi.android.com.modle;

public class MessageCenterItem {

    private long id;//: 20190402        //id
    private String date;//：“2019-04-02”    //日期
    private String content;//："fdsafdsa"    //消息内容

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "MessageCenterItem{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
