package aimi.android.com.presenter;

import aimi.android.com.presenter.contract.LargeAmountContract;
import aimi.android.com.presenter.modle.BuyModle;

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
