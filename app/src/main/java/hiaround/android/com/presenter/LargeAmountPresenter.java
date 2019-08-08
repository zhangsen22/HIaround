package hiaround.android.com.presenter;

import hiaround.android.com.modle.LargeAmountResponse;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.LargeAmountContract;
import hiaround.android.com.presenter.modle.BuyModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LargeAmountPresenter implements LargeAmountContract.Presenter{

    private LargeAmountContract.View mView;
    private BuyModle mModel;

    public LargeAmountPresenter(LargeAmountContract.View view, BuyModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void getHugeBillinfoRefresh(long minId) {

    }

    @Override
    public void getHugeBillinfoLoadMore(long minId) {

    }

    @Override
    public void starLoadData() {

    }
}
