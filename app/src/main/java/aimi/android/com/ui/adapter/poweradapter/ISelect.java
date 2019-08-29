package aimi.android.com.ui.adapter.poweradapter;

public interface ISelect {
    int SINGLE_MODE = 1;
    int MULTIPLE_MODE = 2;

    boolean isSelected();

    void setSelected(boolean selected);

}
