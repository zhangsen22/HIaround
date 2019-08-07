package hiaround.android.com;

/**
 * Created by yz on 2018/12/7 5:07 PM
 * Describe: mvp view的基类
 */
public interface IBaseView<T extends IBasePresenter> {
     void setPresenter(T presenter);
     void showLoading();
     void hideLoading();
}
