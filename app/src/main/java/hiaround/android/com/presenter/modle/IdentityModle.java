package hiaround.android.com.presenter.modle;

import hiaround.android.com.modle.BaseBean;
import hiaround.android.com.net.retrofit.BaseRetrofitClient;
import hiaround.android.com.net.retrofit.exception.ModelExceptionMap;
import hiaround.android.com.net.retrofit.exception.ServerExceptionMap;
import hiaround.android.com.net.retrofit.service.ApiServices;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class IdentityModle {

    /**
     * 身份认证
     * @param type
     * @param name
     * @param sex
     * @param birthday
     * @param IDNumber
     * @param IDImageFront
     * @param IDImageBehind
     * @param IDImageWithUser
     * @return
     */
    public Observable<BaseBean> idCheck(int type,String name,int sex,String birthday,String IDNumber,String IDImageFront,String IDImageBehind,String IDImageWithUser){
        return BaseRetrofitClient.getInstance().create(ApiServices.class)
                .idCheck(type,name,sex,birthday,IDNumber,IDImageFront,IDImageBehind,IDImageWithUser)
                .subscribeOn(Schedulers.io())
                .map(new ServerExceptionMap<BaseBean>())
                .onErrorResumeNext(new ModelExceptionMap<BaseBean>());
    }
}
