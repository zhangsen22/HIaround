package hiaround.android.com.modle;

public class SellLimitResponse extends BaseBean{

    private double sellMinVal;
    private double sellMaxVal;
    private String unit;

    public double getSellMinVal() {
        return sellMinVal;
    }

    public double getSellMaxVal() {
        return sellMaxVal;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "SellLimitResponse{" +
                "sellMinVal=" + sellMinVal +
                ", sellMaxVal=" + sellMaxVal +
                ", unit='" + unit + '\'' +
                '}';
    }
}
