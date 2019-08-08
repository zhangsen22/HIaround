package hiaround.android.com.presenter;

import hiaround.android.com.modle.BuyResponse;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.SellContract;
import hiaround.android.com.presenter.modle.BuyModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SellPresenter implements SellContract.Presenter{

    protected SellContract.View mView;
    private BuyModle mModel;

    public SellPresenter(SellContract.View view, BuyModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void getSellRefresh(long minId) {

    }

    @Override
    public void getSellLoadMore(long minId) {

    }

    @Override
    public void starLoadData() {

    }
}
