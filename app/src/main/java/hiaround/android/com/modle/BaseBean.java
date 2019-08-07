package hiaround.android.com.modle;

//      {
//        "id": 1,
//        "state": {
//        "code": 0,
//        "msg": "操作成功"
//        },
//        "data": {},
//        "etag": null,
//        "time": 142323213123
//        }



public class BaseBean {
    private int ret; //0代表成功，其他见下方错误码
    private String message;

    public int getRet() {
        return ret;
    }

    public String getMsgg() {
        return message;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "ret=" + ret +
                ", message='" + message + '\'' +
                '}';
    }
}
