package hiaround.android.com.presenter;
import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.modle.WalletResponse;
import hiaround.android.com.net.retrofit.ModelResultObserver;
import hiaround.android.com.net.retrofit.exception.ModelException;
import hiaround.android.com.presenter.contract.EntrustSaleContract;
import hiaround.android.com.presenter.modle.EntrustSaleModle;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class EntrustSalePresenter implements EntrustSaleContract.Presenter{

    private EntrustSaleContract.View mView;
    private EntrustSaleModle mModel;

    public EntrustSalePresenter(EntrustSaleContract.View view, EntrustSaleModle model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }

    @Override
    public void putUpSell(double price, double minNum, double maxNum, boolean supporAli, boolean supportWechat, boolean supportBank, String financePwd, long time) {
        mView.showLoading();
        mModel.putUpSell(price,minNum,maxNum,supporAli,supportWechat,supportBank,financePwd,time).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        mView.putUpSellSuccess(baseBean);
                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void getInfo() {
//        mView.showLoading();
        mModel.getInfo().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModelResultObserver<WalletResponse>() {
                    @Override
                    public void onSuccess(WalletResponse walletResponse) {
                        mView.getInfoSuccess(walletResponse);
//                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(ModelException ex) {
                        super.onFailure(ex);
//                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void starLoadData() {

    }
}
