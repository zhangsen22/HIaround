package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.FinanceLogResponse;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class FinancialRecordsModle {

    /**
     * 获取财务记录
     * @param minId
     * @return
     */
    public Observable<FinanceLogResponse> financeLog(long minId){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .financeLog(minId)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<FinanceLogResponse>())
                .onErrorResumeNext(new ModelExceptionMap<FinanceLogResponse>());
    }
}
