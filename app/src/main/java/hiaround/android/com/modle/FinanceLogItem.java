package hiaround.android.com.modle;

public class FinanceLogItem {

    private long id;//:32    //记录id
    private int type;//:1    //交易类型 1为充币 2为提币 3为转入 4为转出
    private double num;//：32423.32    //涉及数量
    private int status;//:2    //状态 1为确认中 2为已完成 3为取消
    private long logtime;//：23241    //交易开始时间毫秒值
    private long succtime;//：3231   //交易完成时间，未完成为0

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public double getNum() {
        return num;
    }

    public int getStatus() {
        return status;
    }

    public long getLogtime() {
        return logtime;
    }

    public long getSucctime() {
        return succtime;
    }

    @Override
    public String toString() {
        return "FinanceLogItem{" +
                "id=" + id +
                ", type=" + type +
                ", num=" + num +
                ", status=" + status +
                ", logtime=" + logtime +
                ", succtime=" + succtime +
                '}';
    }
}
